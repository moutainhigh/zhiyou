package com.zy.job;

import com.zy.model.query.PaymentQueryModel;
import com.zy.service.PaymentService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by freeman on 16/9/7.
 */
public class PaymentCancelJob implements Job {

	@Autowired
	private PaymentService paymentService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
			paymentService.findAll(PaymentQueryModel.builder().expiredTimeLT(new Date()).build())
					.parallelStream()
					.map(payment -> payment.getId())
					.forEach(paymentService::cancel);
	}
}
