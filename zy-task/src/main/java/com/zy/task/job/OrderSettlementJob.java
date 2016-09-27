package com.zy.task.job;

import com.zy.common.exception.ConcurrentException;
import com.zy.entity.mal.Order;
import com.zy.service.OrderService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

import static com.zy.entity.mal.Order.OrderStatus.已发货;
import static com.zy.entity.mal.Order.OrderStatus.已完成;
import static com.zy.entity.mal.Order.OrderStatus.已支付;
import static com.zy.model.query.OrderQueryModel.builder;

/**
 * Created by freeman on 16/9/8.
 */
public class OrderSettlementJob implements Job {

	private Logger logger = LoggerFactory.getLogger(OrderSettlementJob.class);

	@Autowired
	private OrderService orderService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("begin...");
		orderService.findAll(builder().orderStatusEQ(已完成).isSettledUpEQ(false).build())
				.stream()
				.map(order -> order.getId())
				.forEach(this::settleUp);

		orderService.findAll(builder().orderStatusIN(new Order.OrderStatus[]{已支付,已发货,已完成}).isProfitSettledUpEQ(false).build())
				.stream()
				.map(order -> order.getId())
				.forEach(this::settleUpProfit);

		logger.info("end...");
	}

	private void settleUp(Long id) {
		try {
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {

			}
			this.orderService.settleUp(id);
			logger.info("结算 {} 成功", id);
		} catch (ConcurrentException e) {
			settleUp(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}	private void settleUpProfit(Long id) {
		try {
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {

			}
			this.orderService.settleUpProfit(id);
			logger.info("结算 settleUpProfit {} 成功", id);
		} catch (ConcurrentException e) {
			settleUpProfit(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
