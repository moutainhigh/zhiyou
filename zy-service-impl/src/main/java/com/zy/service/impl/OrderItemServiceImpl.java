package com.zy.service.impl;

import com.zy.entity.mal.OrderItem;
import com.zy.mapper.OrderItemMapper;
import com.zy.service.OrderItemService;

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

	@Override
	public List<OrderItem> findAll() {
		return orderItemMapper.findAll();
	}

}
