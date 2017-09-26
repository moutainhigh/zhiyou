package com.zy.task.job;

import com.zy.common.exception.ConcurrentException;
import com.zy.service.LargeareaMonthSalesService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * Created by liang on 2017/9/26.处理大区月销量报表
 */
public class LargeareaMonthSalesJob implements Job {

    private Logger logger = LoggerFactory.getLogger(LargeareaMonthSalesJob.class);

    @Autowired
    private LargeareaMonthSalesService largeareaMonthSalesService;


    /**
     * job 开始
     * @param jobExecutionContext
     * @throws JobExecutionException
     */

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("LargeareaMonthSalesJob begin.......");
           this.disposeLargeareaMonthSales();
        logger.info("LargeareaMonthSalesJob end.......");
    }

    private void disposeLargeareaMonthSales(){
        try {

        } catch (ConcurrentException e) {
                try {
                    TimeUnit.SECONDS.sleep(2);} catch (InterruptedException e1) {}
                    this.disposeLargeareaMonthSales();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
    }
}
