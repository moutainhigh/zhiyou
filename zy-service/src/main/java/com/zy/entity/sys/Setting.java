package com.gc.entity.sys;

import io.gd.generator.annotation.Field;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "sys_setting")
@Getter
@Setter
public class Setting implements Serializable {

	@Id
	@Field(label = "id")
	private Long id;

	@NotNull
	@Field(label = "系统中转账户")
	private Long sysUserId;

	@NotNull
	@Field(label = "佣金账户")
	private Long feeUserId;

	@NotNull
	@Field(label = "发放账户")
	private Long grantUserId;

	@NotBlank
	@Column(length = 1000)
	@Field(label = "提现费率",description = "提现费率 不能小于0.00, 不能超过Constans.SETTING_MAX_WITHDRAW_FEE_RATE")
	private String withdrawFeeRateScript;

	@NotNull
	@Field(label = "是否开发环境")
	private Boolean isDev;

}
