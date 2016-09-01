package com.gc.service;

import com.gc.entity.usr.UserSetting;

public interface UserSettingService {

	UserSetting createIfAbsent(Long userId);
	void modifyIsReceiveTaskSms(Long userId, boolean isReceiveTaskSms);

}
