package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.mergeusr.MergeUserLog;

import java.util.Map;
import com.zy.model.dto.UserTeamCountDto;

public interface MergeUserLogMapper {

	int insert(MergeUserLog mergeUserLog);

	int update(MergeUserLog mergeUserLog);

	int merge(@Param("mergeUserLog") MergeUserLog mergeUserLog, @Param("fields")String... fields);

	int delete(Long id);

	MergeUserLog findOne(Long id);

	List<MergeUserLog> findAll();

	List<UserTeamCountDto> findGByRank(Map<String,Object> dataMap);

}