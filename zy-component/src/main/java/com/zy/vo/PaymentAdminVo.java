package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.fnc.PayType;
import com.zy.entity.fnc.Payment.PaymentStatus;
import com.zy.entity.fnc.CurrencyType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class PaymentAdminVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "支付订单号")
	private String sn;
	@Field(label = "用户id")
	private Long userId;
	@Field(label = "标题")
	private String title;
	@Field(label = "支付方式")
	private PayType payType;
	@Field(label = "业务类型")
	private String bizName;
	@Field(label = "业务sn")
	private String bizSn;
	@Field(label = "下单时间")
	private Date createdTime;
	@Field(label = "过期时间")
	private Date expiredTime;
	@Field(label = "支付时间")
	private Date paidTime;
	@Field(label = "支付状态")
	private PaymentStatus paymentStatus;
	@Field(label = " 货币1")
	private CurrencyType currencyType1;
	@Field(label = "货币1支付金额")
	private BigDecimal amount1;
	@Field(label = "货币2")
	private CurrencyType currencyType2;
	@Field(label = " 货币2支付金额")
	private BigDecimal amount2;
	@Field(label = "货币1退款金额")
	private BigDecimal refund1;
	@Field(label = " 货币2退款金额")
	private BigDecimal refund2;
	@Field(label = "退款时间")
	private Date refundedTime;
	@Field(label = "退款备注")
	private String refundRemark;
	@Field(label = "取消备注")
	private String cancelRemark;
	@Field(label = "备注")
	private String remark;

	/* 扩展 */
	@Field(label = "用户id",order = 999)
	private UserAdminSimpleVo user;

}