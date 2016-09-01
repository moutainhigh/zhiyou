package com.zy.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.zy.entity.sys.Setting;
import com.zy.mapper.SettingMapper;
import com.zy.model.Constants;

@Component
@Validated
public class ConfigImpl extends AbstractConfig {

	@Autowired
	private SettingMapper settingMapper;

	@Override
	protected Setting getSetting() {
		return settingMapper.findOne(Constants.SETTING_SETTING_ID);
	}

}
