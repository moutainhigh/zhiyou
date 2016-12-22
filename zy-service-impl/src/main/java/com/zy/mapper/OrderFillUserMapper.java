package com.zy.mapper;


import com.zy.entity.mal.OrderFillUser;
import com.zy.model.query.OrderFillUserQueryModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface OrderFillUserMapper {

	int insert(OrderFillUser orderFillUser);

	int update(OrderFillUser orderFillUser);

	int merge(@Param("orderFillUser") OrderFillUser orderFillUser, @Param("fields")String... fields);

	int delete(Long id);

	OrderFillUser findOne(Long id);

	List<OrderFillUser> findAll(OrderFillUserQueryModel orderFillUserQueryModel);

	long count(OrderFillUserQueryModel orderFillUserQueryModel);

}