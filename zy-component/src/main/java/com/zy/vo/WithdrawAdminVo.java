package com.gc.vo;

import com.gc.entity.fnc.CurrencyType;
import com.gc.entity.fnc.Withdraw.WithdrawStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class WithdrawAdminVo implements Serializable {
	/* 原生 */
	private Long id;
	private CurrencyType currencyType;
	private String title;
	private String sn;
	private BigDecimal amount;
	private BigDecimal feeRate;
	private BigDecimal fee;
	private BigDecimal realAmount;
	private BigDecimal outerFee;
	private Date createdTime;
	private Date withdrawedTime;
	private WithdrawStatus withdrawStatus;
	private Boolean isToBankCard;
	private Long bankCardId;
	private String openId;
	private String remark;

	/* 扩展 */
	private UserAdminSimpleVo user;
	private BankCardAdminVo bankCard;

}