package com.zy.job;

import com.zy.entity.sys.ConfirmStatus;
import com.zy.service.ReportService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import static com.zy.model.query.ReportQueryModel.builder;

/**
 * Created by freeman on 16/9/8.
 */
public class ReportStatementJob implements Job {

	@Autowired
	private ReportService reportService;


	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		reportService.findAll(builder().isSettledUpEQ(false).confirmStatusEQ(ConfirmStatus.已通过).build())
				.parallelStream()
				.map(report -> report.getId())
				.forEach(reportService::settleUp);
	}
}
