package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.usr.UserInfo;
import com.zy.model.query.UserInfoQueryModel;

public interface UserInfoService {

	UserInfo findOne(Long id);

	Page<UserInfo> findPage(UserInfoQueryModel userInfoQueryModel);

	UserInfo findByUserId(Long userId);

	UserInfo create(UserInfo userInfo);

	void modify(UserInfo userInfo);

	long count(UserInfoQueryModel userInfoQueryModel);

	void confirm(Long id, boolean isSuccess, String confirmRemark);

}
