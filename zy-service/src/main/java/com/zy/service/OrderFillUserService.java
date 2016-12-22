package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.mal.OrderFillUser;
import com.zy.model.query.OrderFillUserQueryModel;

import java.util.List;

public interface OrderFillUserService {

	void create(OrderFillUser orderFillUser);

	void delete(Long id);

	List<OrderFillUser> findAll(OrderFillUserQueryModel orderFillUserQueryModel);

	Page<OrderFillUser> findPage(OrderFillUserQueryModel orderFillUserQueryModel);
}
