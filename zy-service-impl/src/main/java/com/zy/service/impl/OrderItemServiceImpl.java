package com.gc.service.impl;

import com.gc.entity.mal.OrderItem;
import com.gc.mapper.OrderItemMapper;
import com.gc.service.OrderItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import java.util.List;

@Service
@Validated
public class OrderItemServiceImpl implements OrderItemService {

	@Autowired
	private OrderItemMapper orderItemMapper;

	@Override
	public List<OrderItem> findByOrderId(@NotNull Long orderId) {
		return orderItemMapper.findByOrderId(orderId);
	}

}
