package com.zy.task.job;

import com.zy.common.exception.ConcurrentException;
import com.zy.service.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

/**
 * Created by husky on 17/02/07
 */
public class OrderSettlementMonthlyJob implements Job {

    private Logger logger = LoggerFactory.getLogger(OrderSettlementMonthlyJob.class);

    @Autowired
    private OrderService orderService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("begin...");
        settleUp(StringUtils.left(LocalDate.now().toString(), 7));
        logger.info("end...");
    }

    private void settleUp(String yearAndMonth) {
        try {
            this.orderService.settleUpMonthly(yearAndMonth);
            logger.info("月结算 {} 成功", yearAndMonth);
        } catch (ConcurrentException e) {
            try {TimeUnit.SECONDS.sleep(5);} catch (InterruptedException e1) {}
            settleUp(yearAndMonth);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
