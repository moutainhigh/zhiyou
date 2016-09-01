package com.zy.service;

import com.zy.entity.usr.UserSetting;

public interface UserSettingService {

	UserSetting createIfAbsent(Long userId);
	void modifyIsReceiveTaskSms(Long userId, boolean isReceiveTaskSms);

}
