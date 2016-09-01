package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.mal.OrderItem;


public interface OrderItemMapper {

	int insert(OrderItem orderItem);

	int update(OrderItem orderItem);

	int merge(@Param("orderItem") OrderItem orderItem, @Param("fields")String... fields);

	int delete(Long id);

	OrderItem findOne(Long id);

	List<OrderItem> findAll();

	List<OrderItem> findByOrderId(Long orderId);

}