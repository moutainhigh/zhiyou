package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.usr.UserInfo;
import com.zy.model.query.UserInfoQueryModel;

import java.util.List;

public interface UserInfoService {

	UserInfo findOne(Long id);

	Page<UserInfo> findPage(UserInfoQueryModel userInfoQueryModel);

	List<UserInfo> findAll(UserInfoQueryModel userInfoQueryModel);

	UserInfo findByUserId(Long userId);

	UserInfo create(UserInfo userInfo);

	void modify(UserInfo userInfo);

	void adminModify(UserInfo userInfo);

	long count(UserInfoQueryModel userInfoQueryModel);

	void confirm(Long id, boolean isSuccess, String confirmRemark);


}
