package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.usr.UserInfo;
import com.zy.model.query.UserInfoQueryModel;


public interface UserInfoMapper {

	int insert(UserInfo userInfo);

	int update(UserInfo userInfo);

	int merge(@Param("userInfo") UserInfo userInfo, @Param("fields")String... fields);

	int delete(Long id);

	UserInfo findOne(Long id);

	UserInfo findByIdCardNumber(String idCardNumber);

	List<UserInfo> findAll(UserInfoQueryModel userInfoQueryModel);

	long count(UserInfoQueryModel userInfoQueryModel);

	UserInfo findByUserId(Long userId);

	UserInfo findByUserIdandFlage(Long userId);

}