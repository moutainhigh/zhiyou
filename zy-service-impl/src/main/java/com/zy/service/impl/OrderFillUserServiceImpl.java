package com.zy.service.impl;

import com.zy.common.model.query.Page;
import com.zy.entity.mal.OrderFillUser;
import com.zy.entity.usr.User;
import com.zy.mapper.OrderFillUserMapper;
import com.zy.mapper.UserMapper;
import com.zy.model.query.OrderFillUserQueryModel;
import com.zy.service.OrderFillUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@Service
@Validated
public class OrderFillUserServiceImpl implements OrderFillUserService{

	@Autowired
	public OrderFillUserMapper orderFillUserMapper;

	@Autowired
	public UserMapper userMapper;

	@Override
	public void create(@NotNull OrderFillUser orderFillUser) {
		validate(orderFillUser);
		Long userId = orderFillUser.getUserId();
		User user = userMapper.findOne(userId);
		validate(user, NOT_NULL, "user id " + userId + " not fount");
		List<OrderFillUser> all = orderFillUserMapper.findAll(OrderFillUserQueryModel.builder().userIdEQ(userId).build());
		if(!all.isEmpty()) {
			return;
		}
		orderFillUserMapper.insert(orderFillUser);
	}

	@Override
	public void delete(@NotNull Long id, Long userId) {
		OrderFillUser orderFillUser = new OrderFillUser();
		orderFillUser.setUpdateId(userId);
		orderFillUser.setUpdateTime(new Date());
		orderFillUserMapper.delete(orderFillUser);
	}

	@Override
	public List<OrderFillUser> findAll(@NotNull OrderFillUserQueryModel orderFillUserQueryModel) {
		return orderFillUserMapper.findAll(orderFillUserQueryModel);
	}

	@Override
	public Page<OrderFillUser> findPage(@NotNull OrderFillUserQueryModel orderFillUserQueryModel) {
		if(orderFillUserQueryModel.getPageNumber() == null)
			orderFillUserQueryModel.setPageNumber(0);
		if(orderFillUserQueryModel.getPageSize() == null)
			orderFillUserQueryModel.setPageSize(20);
		long total = orderFillUserMapper.count(orderFillUserQueryModel);
		List<OrderFillUser> data = orderFillUserMapper.findAll(orderFillUserQueryModel);
		Page<OrderFillUser> page = new Page<>();
		page.setPageNumber(orderFillUserQueryModel.getPageNumber());
		page.setPageSize(orderFillUserQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}
}
