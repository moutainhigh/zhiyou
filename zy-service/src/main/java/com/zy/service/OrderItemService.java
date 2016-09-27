package com.zy.service;

import com.zy.entity.mal.OrderItem;

import java.util.List;

public interface OrderItemService {

	List<OrderItem> findByOrderId(Long orderId);

	List<OrderItem> findAll();
}
