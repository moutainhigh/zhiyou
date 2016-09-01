package com.zy.service;

import java.util.List;

import com.zy.entity.mal.OrderItem;

public interface OrderItemService {

	List<OrderItem> findByOrderId(Long orderId);
}
