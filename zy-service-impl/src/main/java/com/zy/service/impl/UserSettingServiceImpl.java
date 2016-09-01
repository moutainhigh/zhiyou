package com.zy.service.impl;

import com.zy.entity.usr.User;
import com.zy.entity.usr.UserSetting;
import com.zy.mapper.UserMapper;
import com.zy.mapper.UserSettingMapper;
import com.zy.service.UserSettingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@Service
@Validated
public class UserSettingServiceImpl implements UserSettingService {

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private UserSettingMapper userSettingMapper;

	@Override
	public UserSetting createIfAbsent(@NotNull Long userId) {
		User user = userMapper.findOne(userId);
		validate(user, NOT_NULL, "user id " + userId + " is not found");
		UserSetting userSetting = userSettingMapper.findByUserId(userId);
		if (userSetting == null) {
			userSetting = new UserSetting();
			userSetting.setUserId(userId);
			userSetting.setIsReceiveTaskSms(true);
			userSettingMapper.insert(userSetting);
		}
		return userSetting;
	}
	
	@Override
	public void modifyIsReceiveTaskSms(@NotNull Long userId, boolean isReceiveTaskSms) {
		User user = userMapper.findOne(userId);
		validate(user, NOT_NULL, "user id " + userId + " is not found");
		UserSetting userSetting = userSettingMapper.findByUserId(userId);
		validate(userSetting, NOT_NULL, "user setting of user " + userId + " is not found");
		userSetting.setIsReceiveTaskSms(isReceiveTaskSms);
		userSettingMapper.update(userSetting);
		
	}

}
