package com.zy.mapper;


import com.zy.entity.mergeusr.MergeUserView;
import com.zy.model.dto.UserDto;
import com.zy.model.query.MergeUserViewQueryModel;
import com.zy.model.query.UserQueryModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface MergeUserViewMapper {

	int insert(MergeUserView mergeUserView);

	int update(MergeUserView mergeUserView);

	int merge(@Param("mergeUserView") MergeUserView mergeUserView, @Param("fields")String... fields);

	int delete(Long id);

	MergeUserView findOne(Long id);

	List<MergeUserView> findAll(MergeUserViewQueryModel mergeUserViewQueryModel);

	long count(MergeUserViewQueryModel mergeUserViewQueryModel);

	MergeUserView findByCode(String code);

	List<MergeUserView> findSupAll(Map<String, Object> dataMap);

	long countByActive(UserQueryModel userQueryModel);

	List<MergeUserView> findByNotActive(UserQueryModel userQueryModel);

	List<UserDto> findMergeUserViewAll(UserQueryModel userQueryModel);

	long countMergeUserViewAll(UserQueryModel userQueryModel);

	long countByNotActive(UserQueryModel userQueryModel);

	List<MergeUserView> findAddpeople(UserQueryModel userQueryModel);

}