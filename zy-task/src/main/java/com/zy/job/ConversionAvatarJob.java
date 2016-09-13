package com.zy.job;


import com.zy.entity.usr.User;
import com.zy.job.extend.AliyunOssSupport;
import com.zy.model.Constants;
import com.zy.model.query.UserQueryModel;
import com.zy.service.UserService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;


/**
 * Created by freeman on 16/8/8.
 */
@Component
public class ConversionAvatarJob implements Job {

    Logger logger = LoggerFactory.getLogger(ConversionAvatarJob.class);

    @Autowired
    private CloseableHttpClient closeableHttpClient;
    @Autowired
    private AliyunOssSupport aliyunOssSupport;
    @Autowired
    private UserService userService;


    private String getAvatar(HttpEntity entity, String contentType, long contentLength) throws IOException {
        return aliyunOssSupport.putPublicObject(Constants.ALIYUN_BUCKET_NAME_IMAGE, "avatar/", entity.getContent(), contentLength,
                contentType, Constants.ALIYUN_URL_IMAGE);
    }


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("begin...{}", LocalDateTime.now());
        userService.findAll(UserQueryModel.builder()
                .registerTimeGTE(org.apache.commons.lang3.time.DateUtils
                        .addDays(new Date(),-2))
                .build())
                .parallelStream()
                .forEach(this::doHandle);
        logger.info("end...{}", LocalDateTime.now());
    }

    private void doHandle(User user) {
        if (user.getAvatar().startsWith(Constants.ALIYUN_URL_IMAGE)) return;
        HttpGet get = new HttpGet(user.getAvatar());
        try (CloseableHttpResponse res = closeableHttpClient.execute(get);) {
            HttpEntity entity = res.getEntity();
            String contentType = entity.getContentType().getValue();
            long contentLength = entity.getContentLength();
            String avatar = getAvatar(entity, contentType, contentLength);
            userService.modifyAvatar(user.getId(), avatar);
            logger.info("upload to aliyun success url = [{}]",avatar);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
