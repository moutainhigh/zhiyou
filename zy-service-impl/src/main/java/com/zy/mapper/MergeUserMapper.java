package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.mergeusr.MergeUser;
import com.zy.model.query.MergeUserQueryModel;

import com.zy.model.dto.UserTeamCountDto;
import java.util.Map;

public interface MergeUserMapper {

	int insert(MergeUser mergeUser);

	int update(MergeUser mergeUser);

	int merge(@Param("mergeUser") MergeUser mergeUser, @Param("fields")String... fields);

//	int delete(Long id);
//
//	MergeUser findOne(Long id);

	List<MergeUser> findAll(MergeUserQueryModel mergeUserQueryModel);

	long count(MergeUserQueryModel mergeUserQueryModel);

	MergeUser findByUserIdAndProductType(Map<String,Object> map);

	MergeUser findBycodeAndProductType(Map<String,Object> map);

	List<UserTeamCountDto> countByUserId(Map<String,Object> map);

}