package com.gc.service;

import java.util.List;

import com.gc.entity.mal.OrderItem;

public interface OrderItemService {

	List<OrderItem> findByOrderId(Long orderId);
}
