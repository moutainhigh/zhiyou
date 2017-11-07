package com.zy.mapper;


import com.zy.entity.mergeusr.MergeUserView;
import com.zy.model.query.MergeUserViewQueryModel;
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

	long countByActive(MergeUserViewQueryModel mergeUserViewQueryModel);

	List<MergeUserView> findByNotActive(MergeUserViewQueryModel mergeUserViewQueryModel);

}