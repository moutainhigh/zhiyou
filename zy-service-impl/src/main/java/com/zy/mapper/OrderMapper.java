package com.zy.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zy.entity.mal.Order;
import com.zy.entity.mal.Order.OrderStatus;
import com.zy.model.dto.OrderSumDto;
import com.zy.model.query.OrderQueryModel;


public interface OrderMapper {

	int insert(Order order);

	int update(Order order);

	int merge(@Param("order") Order order, @Param("fields")String... fields);

	int delete(Long id);

	Order findOne(Long id);

	List<Order> findAll(OrderQueryModel orderQueryModel);

	long count(OrderQueryModel orderQueryModel);

	Order findBySn(String sn);

	OrderSumDto orderSum(OrderStatus[] orderStatusIN);

}