package com.zy.job;

import com.zy.common.exception.ConcurrentException;
import com.zy.service.PaymentService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.zy.entity.fnc.Payment.PaymentStatus.待支付;
import static com.zy.model.query.PaymentQueryModel.builder;

/**
 * Created by freeman on 16/9/7.
 */
public class PaymentCancelJob implements Job {

	private Logger logger = LoggerFactory.getLogger(PaymentCancelJob.class);

	@Autowired
	private PaymentService paymentService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("begin...");
		paymentService.findAll(builder().expiredTimeLT(new Date()).paymentStatusEQ(待支付).build())
				.stream()
				.map(payment -> payment.getId())
				.forEach(this::cancel);
		logger.info("end...");


	}

	private void cancel(Long id) {
		try {
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {

			}
			this.paymentService.cancel(id);
			logger.info("取消 {} 成功", id);
		} catch (ConcurrentException e) {
			cancel(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
