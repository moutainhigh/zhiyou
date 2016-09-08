package com.zy.job;

import com.zy.service.AccountLogService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import static com.zy.model.query.AccountLogQueryModel.builder;

/**
 * Created by freeman on 16/9/8.
 */
public class AccountLogAckJob implements Job {

	@Autowired
	private AccountLogService accountLogService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		accountLogService.findAll(builder().isAcknowledgedEQ(false).build())
				.stream()
				.map(accountLog -> accountLog.getId())
				.forEach(accountLogService::acknowledge);
	}
}
