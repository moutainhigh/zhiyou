package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.mergeusr.MergeUserView;
import com.zy.model.query.MergeUserViewQueryModel;


public interface MergeUserViewMapper {

	int insert(MergeUserView mergeUserView);

	int update(MergeUserView mergeUserView);

	int merge(@Param("mergeUserView") MergeUserView mergeUserView, @Param("fields")String... fields);

	int delete(Long id);

	MergeUserView findOne(Long id);

	List<MergeUserView> findAll(MergeUserViewQueryModel mergeUserViewQueryModel);

	long count(MergeUserViewQueryModel mergeUserViewQueryModel);

	MergeUserView findByCode(String code);

}