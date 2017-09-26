package com.zy.task.job;

import com.zy.common.exception.ConcurrentException;
import com.zy.service.LargeareaDaySalesService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * Created by liang on 2017/9/26. 处理大区日销量报表
 */
public class LargeareaDaySalesJob implements Job {

    private Logger logger = LoggerFactory.getLogger(LargeareaDaySalesJob.class);


    @Autowired
    private LargeareaDaySalesService largeareaDaySalesService;

    /**
     * job 开始
     * @param jobExecutionContext
     * @throws JobExecutionException
     */

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("LargeareaDaySalesJob begin.......");
           this.disposeLargeareaDaySales();
        logger.info("LargeareaDaySalesJob end.......");
    }

    private void disposeLargeareaDaySales(){
        try {

        } catch (ConcurrentException e) {
                try {
                    TimeUnit.SECONDS.sleep(2);} catch (InterruptedException e1) {}
                    this.disposeLargeareaDaySales();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
    }
}
