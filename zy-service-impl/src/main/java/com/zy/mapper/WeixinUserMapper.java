package com.gc.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.gc.entity.usr.WeixinUser;
import com.gc.model.query.WeixinUserQueryModel;


public interface WeixinUserMapper {

	int insert(WeixinUser weixinUser);

	int update(WeixinUser weixinUser);

	int merge(@Param("weixinUser") WeixinUser weixinUser, @Param("fields")String... fields);

	int delete(Long id);

	WeixinUser findOne(Long id);

	List<WeixinUser> findAll(WeixinUserQueryModel weixinUserQueryModel);

	long count(WeixinUserQueryModel weixinUserQueryModel);

	WeixinUser findByOpenId(String openId);

	WeixinUser findByUserId(Long userId);

}