package com.zy.job;

import com.zy.service.PaymentService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static com.zy.model.query.PaymentQueryModel.builder;

/**
 * Created by freeman on 16/9/7.
 */
public class PaymentCancelJob implements Job {

	@Autowired
	private PaymentService paymentService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
			paymentService.findAll(builder().expiredTimeLT(new Date()).build())
					.stream()
					.map(payment -> payment.getId())
					.forEach(paymentService::cancel);
	}
}
