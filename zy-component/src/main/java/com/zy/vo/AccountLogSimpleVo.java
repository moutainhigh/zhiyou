package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.fnc.AccountLog.InOut;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class AccountLogSimpleVo implements Serializable {
	/* 原生 */
	@Field(label = "id主键")
	private Long id;
	@Field(label = "资金走向")
	private InOut inOut;
	@Field(label = "对应单据标题")
	private String title;
	@Field(label = "所属用户")
	private Long userId;
	@Field(label = "交易金额")
	private BigDecimal transAmount;
	@Field(label = "交易完成后金额")
	private BigDecimal afterAmount;

	/* 扩展 */
	@Field(label = " 交易产生时间")
	private String transTimeLabel;

}