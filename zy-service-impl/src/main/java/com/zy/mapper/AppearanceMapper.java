package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.usr.Appearance;
import com.zy.model.query.AppearanceQueryModel;


public interface AppearanceMapper {

	int insert(Appearance appearance);

	int update(Appearance appearance);

	int merge(@Param("appearance") Appearance appearance, @Param("fields")String... fields);

	int delete(Long id);

	Appearance findOne(Long id);

	List<Appearance> findAll(AppearanceQueryModel appearanceQueryModel);

	long count(AppearanceQueryModel appearanceQueryModel);

	Appearance findByUserId(@Param("userId") Long userId);

}