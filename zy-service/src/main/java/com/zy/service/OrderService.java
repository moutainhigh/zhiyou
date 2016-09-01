package com.gc.service;

import com.zy.common.model.query.Page;
import com.gc.entity.mal.Order;
import com.gc.model.query.OrderQueryModel;

public interface OrderService {
	
	void pay(Long id);
	
	void cancel(Long id);
	
	Page<Order> findPage(OrderQueryModel orderQueryModel);
	
	Order findOne(Long id);
	
	Order findBySn(String sn);
	
}
