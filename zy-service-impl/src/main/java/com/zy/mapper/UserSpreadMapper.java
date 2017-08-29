package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.report.UserSpread;
import com.zy.model.query.UserSpreadQueryModel;


public interface UserSpreadMapper {

	int insert(UserSpread userSpread);

	int update(UserSpread userSpread);

	int merge(@Param("userSpread") UserSpread userSpread, @Param("fields")String... fields);

	int delete(Long id);

	UserSpread findOne(Long id);

	List<UserSpread> findAll(UserSpreadQueryModel userSpreadQueryModel);

	long count(UserSpreadQueryModel userSpreadQueryModel);

}