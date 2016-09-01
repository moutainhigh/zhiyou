package com.gc.service;

import java.util.List;

import com.gc.entity.sys.Area;
import com.gc.model.dto.AreaDto;
import com.gc.model.query.AreaQueryModel;

public interface AreaService {

	void create(Area area);
	
	Area merge(Area area, String... fields);
	
	List<Area> findAllOrderByOrderNumberAsc();
	
	void init();

	Area findOne(Long id);
	
	AreaDto findOneDto(Long id);
	List<Area> findAll(AreaQueryModel areaQueryModel);
}
