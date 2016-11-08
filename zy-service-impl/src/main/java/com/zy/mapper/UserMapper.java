package com.zy.mapper;


import com.zy.entity.usr.User;
import com.zy.model.query.UserQueryModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface UserMapper {

	int insert(User user);

	int update(User user);

	int merge(@Param("user") User user, @Param("fields")String... fields);

	int delete(Long id);

	User findOne(Long id);

	List<User> findAll(UserQueryModel userQueryModel);

	long count(UserQueryModel userQueryModel);

	User findByPhone(String phone);

	User findByOpenId(String openId);

	User findByCode(String code);

}