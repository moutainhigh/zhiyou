package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.usr.UserLog;


public interface UserLogMapper {

	int insert(UserLog userLog);

	int update(UserLog userLog);

	int merge(@Param("userLog") UserLog userLog, @Param("fields")String... fields);

	int delete(Long id);

	UserLog findOne(Long id);

	List<UserLog> findAll();

}