package com.zy.task.job;

import com.zy.common.exception.ConcurrentException;
import com.zy.service.TeamProvinceReportService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;


/**
 * Created by liang on 2017/8/29.  处理 省份服务商报表
 */
public class TeamProvinceReportJob implements Job {

    private Logger logger = LoggerFactory.getLogger(TeamProvinceReportJob.class);

    @Autowired
    private TeamProvinceReportService teamProvinceReportService;

    /**
     * job 开始
     * @param jobExecutionContext
     * @throws JobExecutionException
     */

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("TeamProvinceReportJob begin.......");
        this.disposeTeamProvinceReport();
        logger.info("TeamProvinceReportJob end.......");
    }

    //开始处理逻辑
    private void disposeTeamProvinceReport(){
        try {
            teamProvinceReportService.provinceActive();
            logger.info("省份服务商活跃报表");
        } catch (ConcurrentException e) {
                try {
                    TimeUnit.SECONDS.sleep(2);} catch (InterruptedException e1) {}
                    this.disposeTeamProvinceReport();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
    }
}
