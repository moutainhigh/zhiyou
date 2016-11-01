package com.zy.task.job;

import com.zy.common.exception.ConcurrentException;
import com.zy.entity.fnc.PayType;
import com.zy.model.Constants;
import com.zy.service.PaymentService;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.zy.entity.fnc.PayType.余额;
import static com.zy.entity.fnc.PayType.银行汇款;
import static com.zy.entity.fnc.Payment.PaymentStatus.待支付;
import static com.zy.model.Constants.SETTING_PAYMENT_EXPIRE_IN_MINUTES;
import static com.zy.model.Constants.SETTING_PAYMENT_OFFLINE_EXPIRE_IN_MINUTES;
import static com.zy.model.query.PaymentQueryModel.builder;
import static org.apache.commons.lang3.time.DateUtils.addMinutes;

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
		//处理在线
		Date expiredTimeLT = new Date();

		paymentService.findAll(builder()
				.expiredTimeLT(addMinutes(expiredTimeLT, -SETTING_PAYMENT_EXPIRE_IN_MINUTES))
				.paymentStatusEQ(待支付).payTypeEQ(余额).build())
				.stream()
				.map(payment -> payment.getId())
				.forEach(this::cancel);
		//处理线下
		paymentService.findAll(builder()
				.expiredTimeLT(addMinutes(expiredTimeLT, -SETTING_PAYMENT_OFFLINE_EXPIRE_IN_MINUTES))
				.paymentStatusEQ(待支付).payTypeEQ(银行汇款).build())
				.stream()
				.map(payment -> payment.getId())
				.forEach(this::cancel);



		logger.info("end...");


	}

	private void cancel(Long id) {
		try {
			this.paymentService.cancel(id);
			logger.info("取消 {} 成功", id);
		} catch (ConcurrentException e) {
			try {TimeUnit.SECONDS.sleep(2);} catch (InterruptedException e1) {}
			cancel(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
