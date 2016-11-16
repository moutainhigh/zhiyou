package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.fnc.Withdraw.WithdrawStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class WithdrawListVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "货币类型")
	private CurrencyType currencyType;
	@Field(label = "标题")
	private String title;
	@Field(label = "提现单号")
	private String sn;
	@Field(label = "提现金额")
	private BigDecimal amount;
	@Field(label = "提现手续费率")
	private BigDecimal feeRate;
	@Field(label = "提现手续费")
	private BigDecimal fee;
	@Field(label = "实际到账总金额")
	private BigDecimal realAmount;
	@Field(label = " 外部手续费")
	private BigDecimal outerFee;
	@Field(label = "创建时间")
	private Date createdTime;
	@Field(label = "提现成功时间")
	private Date withdrawedTime;
	@Field(label = "提现单状态")
	private WithdrawStatus withdrawStatus;

	/* 扩展 */
	@Field(label = "提现金额")
	private String amountLabel;
	@Field(label = "提现手续费")
	private String feeLabel;
	@Field(label = "实际到账总金额")
	private String realAmountLabel;
	@Field(label = "创建时间")
	private String createdTimeLabel;
	@Field(label = "提现成功时间")
	private String withdrawedTimeLabel;

}