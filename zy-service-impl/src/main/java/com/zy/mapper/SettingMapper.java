package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.sys.Setting;


public interface SettingMapper {

	int insert(Setting setting);

	int update(Setting setting);

	int merge(@Param("setting") Setting setting, @Param("fields")String... fields);

	int delete(Long id);

	Setting findOne(Long id);

	List<Setting> findAll();

}