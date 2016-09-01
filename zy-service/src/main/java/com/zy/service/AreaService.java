package com.zy.service;

import java.util.List;

import com.zy.entity.sys.Area;
import com.zy.model.dto.AreaDto;
import com.zy.model.query.AreaQueryModel;

public interface AreaService {

	void create(Area area);
	
	Area merge(Area area, String... fields);
	
	List<Area> findAllOrderByOrderNumberAsc();
	
	void init();

	Area findOne(Long id);
	
	AreaDto findOneDto(Long id);
	List<Area> findAll(AreaQueryModel areaQueryModel);
}
