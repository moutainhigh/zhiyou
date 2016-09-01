package com.gc.service.impl;

import com.zy.common.model.query.Page;
import com.gc.entity.usr.WeixinUser;
import com.gc.mapper.WeixinUserMapper;
import com.gc.model.query.WeixinUserQueryModel;
import com.gc.service.WeixinUserService;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

import java.util.List;

import static com.zy.common.util.ValidateUtils.validate;

@Service
public class WeixinUserServiceImpl implements WeixinUserService {

	@Autowired
	private WeixinUserMapper weixinUserMapper;

	@Override
	public WeixinUser create(@NotNull WeixinUser weixinUser) {
		validate(weixinUser);
		weixinUserMapper.insert(weixinUser);
		return weixinUserMapper.findOne(weixinUser.getId());
	}

	@Override
	public WeixinUser findOne(@NotNull Long id) {
		return weixinUserMapper.findOne(id);
	}

	@Override
	public Page<WeixinUser> findPage(WeixinUserQueryModel weixinUserQueryModel) {
		if (weixinUserQueryModel.getPageNumber() == null)
			weixinUserQueryModel.setPageNumber(0);
		if (weixinUserQueryModel.getPageSize() == null)
			weixinUserQueryModel.setPageSize(20);
		long total = weixinUserMapper.count(weixinUserQueryModel);
		List<WeixinUser> data = weixinUserMapper.findAll(weixinUserQueryModel);
		Page<WeixinUser> page = new Page<>();
		page.setPageNumber(weixinUserQueryModel.getPageNumber());
		page.setPageSize(weixinUserQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public List<WeixinUser> findAll(@NotNull WeixinUserQueryModel weixinUserQueryModel) {
		weixinUserQueryModel.setPageNumber(null);
		weixinUserQueryModel.setPageSize(null);
		return weixinUserMapper.findAll(weixinUserQueryModel);
	}

	@Override
	public void merge(@NotNull WeixinUser weixinUser,@NotNull String... fields) {
		weixinUserMapper.merge(weixinUser, fields);
	}

	@Override
	public WeixinUser findByOpenId(@NotBlank String openId) {
		return weixinUserMapper.findByOpenId(openId);
	}

	@Override
	public WeixinUser findByUserId(@NotNull Long userId) {
		return weixinUserMapper.findByUserId(userId);
	}


}
