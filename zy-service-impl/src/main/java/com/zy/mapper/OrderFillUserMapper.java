package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.mal.OrderFillUser;
import com.zy.model.query.OrderFillUserQueryModel;


public interface OrderFillUserMapper {

	int insert(OrderFillUser orderFillUser);

	int update(OrderFillUser orderFillUser);

	int merge(@Param("orderFillUser") OrderFillUser orderFillUser, @Param("fields")String... fields);

	int delete(Long id);

	OrderFillUser findOne(Long id);

	List<OrderFillUser> findAll(OrderFillUserQueryModel orderFillUserQueryModel);

	long count(OrderFillUserQueryModel orderFillUserQueryModel);

}