package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.usr.Portrait;
import com.zy.model.query.PortraitQueryModel;


public interface PortraitMapper {

	int insert(Portrait portrait);

	int update(Portrait portrait);

	int merge(@Param("portrait") Portrait portrait, @Param("fields")String... fields);

	int delete(Long id);

	Portrait findOne(Long id);

	List<Portrait> findAll(PortraitQueryModel portraitQueryModel);

	long count(PortraitQueryModel portraitQueryModel);

	Portrait findByUserId(Long userId);

}