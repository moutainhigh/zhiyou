package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.mal.Order;
import com.zy.model.dto.OrderCreateDto;
import com.zy.model.query.OrderQueryModel;

public interface OrderService {

	Order create(OrderCreateDto orderCreateDto);
	
	void pay(Long id);
	
	void cancel(Long id);
	
	Page<Order> findPage(OrderQueryModel orderQueryModel);
	
	Order findOne(Long id);
	
	Order findBySn(String sn);
	
	void deliver(Long id, boolean userLogistics, String logisticsName, String logisticsSn); // 发货
	
	void receive(Long id); // 确认收货
}
