package com.zy.service.impl;

import com.zy.Config;
import com.zy.common.exception.ValidationException;
import com.zy.component.AbstractConfig;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.sys.Setting;
import com.zy.entity.usr.User.UserType;
import com.zy.mapper.SettingMapper;
import com.zy.model.Constants;
import com.zy.service.SettingService;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import static com.zy.common.util.ValidateUtils.NOT_BLANK;
import static com.zy.common.util.ValidateUtils.validate;

@Service
@Validated
public class SettingServiceImpl implements SettingService {

	@Autowired
	private SettingMapper settingMapper;

	@Override
	public Setting find() {
		return settingMapper.findOne(Constants.SETTING_SETTING_ID);
	}

	@Override
	public void merge(@NotNull Setting setting,@NotNull String... fields) {
		setting.setId(Constants.SETTING_SETTING_ID);
		settingMapper.merge(setting, fields);
	}

	@Override
	public String checkScript(@NotBlank String script, @NotBlank String field) {
		validate(script, NOT_BLANK, "script is blank");
		switch (field) {
		case "withdrawFeeRateScript":
			return checkWithdrawFeeRateScript(script);
		default:
			throw new ValidationException("field is not script");
		}
	}

	private String checkWithdrawFeeRateScript(@NotBlank String script) {
		StringBuilder sb = new StringBuilder();
		Setting setting = new Setting();
		setting.setWithdrawFeeRateScript(script);
		Config config = new AbstractConfig() {
			@Override
			protected Setting getSetting() {
				return setting;
			}
		};
		sb.append("提现费率").append(config.getWithdrawFeeRate(UserType.代理, CurrencyType.现金)).append("</br>");
		return sb.toString();
	}


}
