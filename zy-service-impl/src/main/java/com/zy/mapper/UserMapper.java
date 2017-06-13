package com.zy.mapper;


import java.util.List;

import com.zy.model.dto.UserTeamCountDto;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.usr.User;
import com.zy.model.query.UserQueryModel;


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

	List<UserTeamCountDto> countByUserId(long userId);

}