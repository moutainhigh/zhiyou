package com.zy.task.job;

import com.zy.common.exception.ConcurrentException;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.service.ReportService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

import static com.zy.model.query.ReportQueryModel.builder;

/**
 * Created by freeman on 16/9/8.
 */
public class ReportStatementJob implements Job {

	private Logger logger = LoggerFactory.getLogger(ReportStatementJob.class);

	@Autowired
	private ReportService reportService;


	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("begin...");
		reportService.findAll(builder().isSettledUpEQ(false).confirmStatusEQ(ConfirmStatus.已通过).build())
				.stream()
				.map(report -> report.getId())
				.forEach(this::settleUp);
		logger.info("end...");

	}

	private void settleUp(Long id) {
		try {
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {

			}
			this.reportService.settleUp(id);
			logger.info("settleUp {} success",id);
		} catch (ConcurrentException e) {
			settleUp(id);
		}catch (Exception e){
			logger.error(e.getMessage(),e);
		}
	}
}
