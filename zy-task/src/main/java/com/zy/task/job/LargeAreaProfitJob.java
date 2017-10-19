package com.zy.task.job;

import com.zy.common.exception.ConcurrentException;
import com.zy.common.util.DateUtil;
import com.zy.entity.report.LargeareaMonthSales;
import com.zy.entity.sys.SystemCode;
import com.zy.entity.usr.User;
import com.zy.model.query.UserQueryModel;
import com.zy.service.ProfitService;
import com.zy.service.SystemCodeService;
import com.zy.service.UserService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Author: Xuwq
 * Date: 2017/9/28.
 */
public class LargeAreaProfitJob implements Job{
    private Logger logger = LoggerFactory.getLogger(LargeAreaProfitJob.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ProfitService profitService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("LargeAreaProfitJob begin.......");
        this.largeAreaProfitJob();
        logger.info("LargeAreaProfitJob end.......");
    }

    private void largeAreaProfitJob() {
        try {
            //过滤大区非空特级
            List<User> v4Users = userService.findAll(UserQueryModel.builder().userRankEQ(User.UserRank.V4).build()).stream().filter(v -> v.getLargearea() != null).collect(Collectors.toList());
            profitService.insert(v4Users);
        } catch (ConcurrentException e) {
            try {
                TimeUnit.SECONDS.sleep(2);} catch (InterruptedException e1) {}
            this.largeAreaProfitJob();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
    }
}
