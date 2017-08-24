package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.report.SalesVolume;
import com.zy.model.query.SalesVolumeQueryModel;


public interface SalesVolumeMapper {

	int insert(SalesVolume salesVolume);

	int update(SalesVolume salesVolume);

	int merge(@Param("salesVolume") SalesVolume salesVolume, @Param("fields")String... fields);

	int delete(Long id);

	SalesVolume findOne(Long id);

	List<SalesVolume> findAll(SalesVolumeQueryModel salesVolumeQueryModel);

	long count(SalesVolumeQueryModel salesVolumeQueryModel);

}