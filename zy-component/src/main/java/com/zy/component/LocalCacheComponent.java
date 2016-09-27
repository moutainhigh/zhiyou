package com.zy.component;

import com.zy.entity.mal.Order;
import com.zy.entity.mal.OrderItem;
import com.zy.entity.usr.User;
import com.zy.model.query.OrderQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.OrderItemService;
import com.zy.service.OrderService;
import com.zy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class LocalCacheComponent {

	final Logger logger = LoggerFactory.getLogger(LocalCacheComponent.class);


	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderItemService orderItemService;

	@Autowired
	private UserService userService;

	public List<User> getUsers() {
		return userService.findAll(new UserQueryModel());
	}

	public List<Order> getOrders() {
		return orderService.findAll(new OrderQueryModel());
	}

	public Map<Long, OrderItem> getOrderItemMap() {
		return orderItemService.findAll().stream().collect(Collectors.toMap(v -> v.getOrderId() , v -> v));
	}


}
