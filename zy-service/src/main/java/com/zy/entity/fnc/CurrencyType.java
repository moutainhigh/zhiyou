package com.zy.entity.fnc;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;

@Type(label = "货币类型")
public enum CurrencyType {

	现金("money", "U币"), 积分("point", "积分"), @Deprecated 金币("coin", "试客币"), ;

	CurrencyType(String code, String alias) {
		this.code = code;
		this.alias = alias;
	}

	@Field(label = "code")
	private final String code;

	@Field(label = "别名")
	private final String alias;

	public String getCode() {
		return code;
	}

	public String getAlias() {
		return alias;
	}

}
