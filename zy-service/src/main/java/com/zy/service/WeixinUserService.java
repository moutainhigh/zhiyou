package com.zy.service;

import java.util.List;

import com.zy.common.model.query.Page;
import com.zy.entity.usr.WeixinUser;
import com.zy.model.query.WeixinUserQueryModel;

public interface WeixinUserService {

	WeixinUser create(WeixinUser weixinUser);
	
	WeixinUser findOne(Long id);

	Page<WeixinUser> findPage(WeixinUserQueryModel weixinUserQueryModel);

	List<WeixinUser> findAll(WeixinUserQueryModel weixinUserQueryModel);
	
	void merge(WeixinUser weixinUser, String... fields);
	
	WeixinUser findByOpenId(String openId);
	
	WeixinUser findByUserId(Long userId);
}
