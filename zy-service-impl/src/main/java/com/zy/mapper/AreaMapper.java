package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.sys.Area;
import com.zy.model.query.AreaQueryModel;


public interface AreaMapper {

	int insert(Area area);

	int update(Area area);

	int merge(@Param("area") Area area, @Param("fields")String... fields);

	int delete(Long id);

	Area findOne(Long id);

	List<Area> findAll(AreaQueryModel areaQueryModel);

	long count(AreaQueryModel areaQueryModel);

	List<Area> findAllOrderByOrderNumberAsc();

	List<Area> findAllOrderByCodeAsc();

}