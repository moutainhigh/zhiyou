package com.gc.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.gc.entity.usr.UserSetting;


public interface UserSettingMapper {

	int insert(UserSetting userSetting);

	int update(UserSetting userSetting);

	int merge(@Param("userSetting") UserSetting userSetting, @Param("fields")String... fields);

	int delete(Long id);

	UserSetting findOne(Long id);

	List<UserSetting> findAll();

	UserSetting findByUserId(Long userId);

}