package com.zy.job;

import com.zy.common.exception.ConcurrentException;
import com.zy.service.TransferService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static com.zy.entity.fnc.Transfer.TransferStatus.待转账;
import static com.zy.model.query.TransferQueryModel.builder;

/**
 * Created by freeman on 16/9/13.
 */
public class TransferJob implements Job {

	private Logger logger = LoggerFactory.getLogger(TransferJob.class);

	@Autowired
	private TransferService transferService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("begin...{}", LocalDateTime.now());
		transferService.findAll(builder().transferStatusEQ(待转账).build())
				.stream()
				.map(transfer -> transfer.getId())
				.forEach(this::transfer);
		logger.info("end...{}", LocalDateTime.now());

	}

	private void transfer(Long transferId) {
		try {
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
			}
			this.transferService.transfer(transferId, "");
			logger.info("transferId {} success", transferId);
		} catch (ConcurrentException e) {
			transfer(transferId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
