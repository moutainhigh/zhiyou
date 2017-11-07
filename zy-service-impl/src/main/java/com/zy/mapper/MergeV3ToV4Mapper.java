package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.mergeusr.MergeV3ToV4;
import com.zy.model.query.MergeV3ToV4QueryModel;


public interface MergeV3ToV4Mapper {

	int insert(MergeV3ToV4 mergeV3ToV4);

	int update(MergeV3ToV4 mergeV3ToV4);

	int merge(@Param("mergeV3ToV4") MergeV3ToV4 mergeV3ToV4, @Param("fields")String... fields);

	int delete(Long id);

	MergeV3ToV4 findOne(Long id);

	List<MergeV3ToV4> findAll(MergeV3ToV4QueryModel mergeV3ToV4QueryModel);

	long count(MergeV3ToV4QueryModel mergeV3ToV4QueryModel);

}