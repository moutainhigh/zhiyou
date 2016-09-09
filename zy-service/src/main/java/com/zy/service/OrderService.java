package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.mal.Order;
import com.zy.model.dto.OrderCreateDto;
import com.zy.model.dto.OrderDeliverDto;
import com.zy.model.query.OrderQueryModel;

import java.util.List;

public interface OrderService {

	Order create(OrderCreateDto orderCreateDto);
	
	void cancel(Long id);
	
	Page<Order> findPage(OrderQueryModel orderQueryModel);
	
	Order findOne(Long id);
	
	Order findBySn(String sn);
	
	void deliver(OrderDeliverDto orderDeliverDto); // 发货
	
	void receive(Long id); // 确认收货

	void modifyIsPlatformDeliver(Long orderId, boolean isPlatformDeliver); // 设置是否平台发货

	void modifySellerId(Long orderId, Long sellerId); // 设置卖家

	void settleUp(Long orderId);

	List<Order> findAll(OrderQueryModel build);
	
	long count(OrderQueryModel build);
}
