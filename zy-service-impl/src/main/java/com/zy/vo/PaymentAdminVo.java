package com.gc.vo;

import com.gc.entity.fnc.CurrencyType;
import com.gc.entity.fnc.PayType;
import com.gc.entity.fnc.Payment.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class PaymentAdminVo implements Serializable {
	/* 原生 */
	private Long id;
	private String sn;
	private Long userId;
	private String title;
	private PayType payType;
	private String bizName;
	private String bizSn;
	private Date createdTime;
	private Date expiredTime;
	private Date paidTime;
	private PaymentStatus paymentStatus;
	private CurrencyType currencyType1;
	private BigDecimal amount1;
	private CurrencyType currencyType2;
	private BigDecimal amount2;
	private BigDecimal refund1;
	private BigDecimal refund2;
	private Date refundedTime;
	private String refundRemark;
	private String cancelRemark;
	private String remark;

	/* 扩展 */
	private UserAdminSimpleVo user;

}