package com.zy.vo;

import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.fnc.Deposit.DepositStatus;
import com.zy.entity.fnc.PayType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class DepositAdminVo implements Serializable {
	/* 原生 */
	private Long id;
	private Long userId;
	private Long paymentId;
	private String title;
	private String sn;
	private CurrencyType currencyType1;
	private BigDecimal amount1;
	private CurrencyType currencyType2;
	private BigDecimal amount2;
	private BigDecimal totalAmount;
	private Boolean isOuterCreated;
	private String outerSn;
	private String qrCodeUrl;
	private String weixinOpenId;
	private PayType payType;
	private Date paidTime;
	private Date createdTime;
	private Date expiredTime;
	private DepositStatus depositStatus;

	/* 扩展 */
	private UserAdminSimpleVo user;

}