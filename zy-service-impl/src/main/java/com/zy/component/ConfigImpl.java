package com.gc.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.gc.entity.sys.Setting;
import com.gc.mapper.SettingMapper;
import com.gc.model.Constants;

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
