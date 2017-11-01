package com.zy.mapper;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zy.entity.mergeusr.MergeUser;
import com.zy.model.query.MergeUserQueryModel;


public interface MergeUserMapper {

	int insert(MergeUser mergeUser);

	int update(MergeUser mergeUser);

	int merge(@Param("mergeUser") MergeUser mergeUser, @Param("fields")String... fields);

	int delete(Long id);

	MergeUser findOne(Long id);

	MergeUser findByUserIdAndProductType(Map<String,Object> map);

	List<MergeUser> findAll(MergeUserQueryModel mergeUserQueryModel);

	long count(MergeUserQueryModel mergeUserQueryModel);

}