package com.zy.vo;

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
	private Long id;
	private InOut inOut;
	private String title;
	private Long userId;
	private AccountLogType accountLogType;
	private Long refId;
	private String refSn;
	private CurrencyType currencyType;
	private Date transTime;
	private BigDecimal beforeAmount;
	private BigDecimal transAmount;
	private BigDecimal afterAmount;

	/* 扩展 */
	private UserAdminSimpleVo user;

}