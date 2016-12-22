package com.zy.entity.sys;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "sys_setting")
@Getter
@Setter
public class Setting implements Serializable {

	@Id
	@Field(label = "id")
	private Long id;

	@NotNull
	@Field(label = "系统账户")
	private Long sysUserId;

	@NotBlank
	@Column(length = 1000)
	@Field(label = "提现费率",description = "提现费率 不能小于0.00, 不能超过Constans.SETTING_MAX_WITHDRAW_FEE_RATE")
	private String withdrawFeeRateScript;

	@NotNull
	@Field(label = "是否开发环境")
	private Boolean isDev;

	@NotNull
	@Field(label = "是否开放提现")
	private Boolean isWithdrawOn;

	@NotNull
	@Field(label = "是否开放补订单")
	private Boolean isOpenOrderFill;

	@Field(label = "补单时间", description = "做为订单创建和下单时间")
	private Date orderFillTime;
}
