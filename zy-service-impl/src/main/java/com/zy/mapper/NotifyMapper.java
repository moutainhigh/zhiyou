package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.sys.Notify;


public interface NotifyMapper {

	int insert(Notify notify);

	int update(Notify notify);

	int merge(@Param("notify") Notify notify, @Param("fields")String... fields);

	int delete(Long id);

	Notify findOne(Long id);

	List<Notify> findAll();

}