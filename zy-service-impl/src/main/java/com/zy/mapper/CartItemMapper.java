package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.mal.CartItem;
import com.zy.model.query.CartItemQueryModel;


public interface CartItemMapper {

	int insert(CartItem cartItem);

	int update(CartItem cartItem);

	int merge(@Param("cartItem") CartItem cartItem, @Param("fields")String... fields);

	int delete(Long id);

	CartItem findOne(Long id);

	List<CartItem> findAll(CartItemQueryModel cartItemQueryModel);

	long count(CartItemQueryModel cartItemQueryModel);

}