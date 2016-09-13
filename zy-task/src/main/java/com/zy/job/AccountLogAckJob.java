package com.zy.job;

import com.zy.common.exception.ConcurrentException;
import com.zy.service.AccountLogService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static com.zy.model.query.AccountLogQueryModel.builder;

/**
 * Created by freeman on 16/9/8.
 */
public class AccountLogAckJob implements Job {

	private Logger logger = LoggerFactory.getLogger(AccountLogAckJob.class);

	@Autowired
	private AccountLogService accountLogService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("begin...{}", LocalDateTime.now());
		accountLogService.findAll(builder().isAcknowledgedEQ(false).build())
				.stream()
				.map(accountLog -> accountLog.getId())
				.forEach(this::acknowledge);
		logger.info("end...{}", LocalDateTime.now());
	}

	private void acknowledge(Long id) {
		try {
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
			}
			this.accountLogService.acknowledge(id);
			logger.info(" acknowledge {} success", id);
		} catch (ConcurrentException e) {
			acknowledge(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}


}
