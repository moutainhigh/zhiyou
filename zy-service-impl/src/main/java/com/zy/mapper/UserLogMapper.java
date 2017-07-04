package com.zy.mapper;


import java.util.List;
import java.util.Map;

import com.zy.model.dto.DepositSumDto;
import com.zy.model.dto.UserTeamCountDto;
import com.zy.model.dto.UserTeamDto;
import com.zy.model.query.UserlongQueryModel;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.usr.UserLog;


public interface UserLogMapper {

	int insert(UserLog userLog);

	int update(UserLog userLog);

	int merge(@Param("userLog") UserLog userLog, @Param("fields")String... fields);

	int delete(Long id);

	UserLog findOne(Long id);

	List<UserLog> findAll();

	long count(Map<String,Object> dataMap);

	List<UserTeamCountDto> findGByRank(Map<String,Object> dataMap);

	List<UserTeamDto> findByRank(UserlongQueryModel userlongQueryModel);

	long countByRank(UserlongQueryModel userlongQueryModel);

	List<DepositSumDto>findRankGroup(UserlongQueryModel userlongQueryModel);
}