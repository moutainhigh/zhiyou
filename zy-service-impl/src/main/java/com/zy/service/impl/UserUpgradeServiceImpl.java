package com.zy.service.impl;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.zy.common.exception.BizException;
import com.zy.common.model.query.Page;
import com.zy.component.UsrComponent;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserRank;
import com.zy.entity.usr.UserUpgrade;
import com.zy.mapper.UserMapper;
import com.zy.mapper.UserUpgradeMapper;
import com.zy.model.BizCode;
import com.zy.model.query.UserUpgradeQueryModel;
import com.zy.service.UserUpgradeService;

@Service
@Validated
public class UserUpgradeServiceImpl implements UserUpgradeService {

	@Autowired
	private UserUpgradeMapper userUpgradeMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private UsrComponent usrComponent;
	
	@Override
	public void upgrade(@NotNull Long userId, @NotNull UserRank fromUserRank, @NotNull UserRank toUserRank) {
		
		User user = userMapper.findOne(userId);
		validate(user, NOT_NULL, "user id" + userId + " not found");
		if(user.getIsFrozen()) {
			throw new BizException(BizCode.ERROR, "用户已被冻结");
		}
		
		usrComponent.upgrade(userId, fromUserRank, toUserRank);

	}

	@Override
	public Page<UserUpgrade> findPage(@NotNull UserUpgradeQueryModel userUpgradeQueryModel) {
		if (userUpgradeQueryModel.getPageNumber() == null)
			userUpgradeQueryModel.setPageNumber(0);
		if (userUpgradeQueryModel.getPageSize() == null)
			userUpgradeQueryModel.setPageSize(20);
		long total = userUpgradeMapper.count(userUpgradeQueryModel);
		List<UserUpgrade> data = userUpgradeMapper.findAll(userUpgradeQueryModel);
		Page<UserUpgrade> page = new Page<>();
		page.setPageNumber(userUpgradeQueryModel.getPageNumber());
		page.setPageSize(userUpgradeQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public List<UserUpgrade> findAll(@NotNull UserUpgradeQueryModel userUpgradeQueryModel) {
		return userUpgradeMapper.findAll(userUpgradeQueryModel);
	}

	@Override
	public long count(@NotNull UserUpgradeQueryModel userUpgradeQueryModel) {
		return userUpgradeMapper.count(userUpgradeQueryModel);
	}

}
