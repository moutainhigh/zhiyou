package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.fnc.PayType;
import com.zy.entity.fnc.Payment.PaymentStatus;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.fnc.Payment.PaymentType;
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
	@Field(label = "支付单类型")
	private PaymentType paymentType;
	@Field(label = "关联业务id")
	private Long refId;
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
	@Field(label = "外部sn")
	private String outerSn;
	@Field(label = "银行汇款截图")
	private String offlineImage;
	@Field(label = "银行汇款备注")
	private String offlineMemo;

	/* 扩展 */
	@Field(label = "用户id")
	private UserAdminSimpleVo user;
	@Field(label = "下单时间")
	private String createdTimeLabel;
	@Field(label = "过期时间")
	private String expiredTimeLabel;
	@Field(label = "支付时间")
	private String paidTimeLabel;
	@Field(label = "支付状态")
	private String paymentStatusStyle;
	@Field(label = "退款时间")
	private String refundedTimeLabel;
	@Field(label = "银行汇款截图")
	private String offlineImageThumbnail;

}