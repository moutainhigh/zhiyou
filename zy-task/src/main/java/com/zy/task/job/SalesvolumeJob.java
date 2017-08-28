package com.zy.task.job;

import com.zy.common.exception.ConcurrentException;
import com.zy.entity.usr.User;
import com.zy.model.query.UserQueryModel;
import com.zy.service.SalesVolumeService;
import com.zy.service.UserService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zy.entity.usr.User.UserRank.V4;

/**
 * Author: Xuwq
 * Date: 2017/8/28.
 */
public class SalesvolumeJob implements Job {

    private Logger logger = LoggerFactory.getLogger(SalesvolumeJob.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SalesVolumeService salesVolumeService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("begin...");
        UserQueryModel  userserQueryModel = new UserQueryModel();
        userserQueryModel.setUserRankEQ(V4);
        List<User> userList = userService.findAll(userserQueryModel);
        salesvolume(userList);
        logger.info("end...");
    }

    private void salesvolume(List<User> userList) {

        try {
            this.salesVolumeService.salesvolume(userList);
            logger.info("销量统计", userList);
        } catch (ConcurrentException e) {
            try {
                TimeUnit.SECONDS.sleep(2);} catch (InterruptedException e1) {}
            salesvolume(userList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
