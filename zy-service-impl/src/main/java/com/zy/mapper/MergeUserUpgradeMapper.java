package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.mergeusr.MergeUserUpgrade;
import com.zy.model.query.MergeUserUpgradeQueryModel;


public interface MergeUserUpgradeMapper {

	int insert(MergeUserUpgrade mergeUserUpgrade);

	int update(MergeUserUpgrade mergeUserUpgrade);

	int merge(@Param("mergeUserUpgrade") MergeUserUpgrade mergeUserUpgrade, @Param("fields")String... fields);

	int delete(Long id);

	MergeUserUpgrade findOne(Long id);

	List<MergeUserUpgrade> findAll(MergeUserUpgradeQueryModel mergeUserUpgradeQueryModel);

	long count(MergeUserUpgradeQueryModel mergeUserUpgradeQueryModel);

}