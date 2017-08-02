package com.zy.mapper;


import com.zy.entity.sys.SystemCode;
import com.zy.model.query.SystemCodeQueryModel;

import java.util.List;
import java.util.Map;


public interface SystemCodeMapper {

	int insert(SystemCode systemCode);

	int update(SystemCode systemCode);

	SystemCode findOne(Long id);

	SystemCode findByTypeAndName(Map<String,Object> map);

	List<SystemCode> findAll(SystemCodeQueryModel systemCodeQueryModel);

	long count(SystemCodeQueryModel systemCodeQueryModel);

}