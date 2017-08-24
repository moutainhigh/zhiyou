package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.report.LargeArea;
import com.zy.model.query.LargeAreaQueryModel;


public interface LargeAreaMapper {

	int insert(LargeArea largeArea);

	int update(LargeArea largeArea);

	int merge(@Param("largeArea") LargeArea largeArea, @Param("fields")String... fields);

	int delete(Long id);

	LargeArea findOne(Long id);

	List<LargeArea> findAll(LargeAreaQueryModel largeAreaQueryModel);

	long count(LargeAreaQueryModel largeAreaQueryModel);

}