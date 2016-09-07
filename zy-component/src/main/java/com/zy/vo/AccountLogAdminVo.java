package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.fnc.AccountLog.InOut;
import com.zy.entity.fnc.AccountLog.AccountLogType;
import com.zy.entity.fnc.CurrencyType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class AccountLogAdminVo implements Serializable {
	/* 原生 */
	@Field(label = "id主键")
	private Long id;
	@Field(label = "资金走向")
	private InOut inOut;
	@Field(label = "对应单据标题")
	private String title;
	@Field(label = "所属用户")
	private Long userId;
	@Field(label = "资金账户日志类型")
	private AccountLogType accountLogType;
	@Field(label = "对应单据id")
	private Long refId;
	@Field(label = "对应单据sn")
	private String refSn;
	@Field(label = "币种")
	private CurrencyType currencyType;
	@Field(label = " 交易产生时间")
	private Date transTime;
	@Field(label = "交易前余额")
	private BigDecimal beforeAmount;
	@Field(label = "交易金额")
	private BigDecimal transAmount;
	@Field(label = "交易完成后金额")
	private BigDecimal afterAmount;

	/* 扩展 */
	@Field(label = "所属用户",order = 999)
	private UserAdminSimpleVo user;

}