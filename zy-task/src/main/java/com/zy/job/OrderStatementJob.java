package com.zy.job;

import com.zy.model.query.OrderQueryModel;
import com.zy.service.OrderService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import static com.zy.entity.mal.Order.OrderStatus.已完成;

/**
 * Created by freeman on 16/9/8.
 */
public class OrderStatementJob implements Job {
	@Autowired
	private OrderService orderService;
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		orderService.findAll(OrderQueryModel.builder().orderStatusEQ(已完成).isSettledUpEQ(false).build())
				.parallelStream()
				.map(order -> order.getId())
				.forEach(orderService::settleUp);
	}
}
