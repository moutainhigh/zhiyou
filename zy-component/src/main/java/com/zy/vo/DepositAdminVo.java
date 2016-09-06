package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.fnc.Deposit.DepositStatus;
import com.zy.entity.fnc.PayType;
import com.zy.entity.fnc.CurrencyType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class DepositAdminVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "用户id")
	private Long userId;
	@Field(label = "充值单id")
	private Long paymentId;
	@Field(label = "标题")
	private String title;
	@Field(label = "充值单号")
	private String sn;
	@Field(label = "充值货币1")
	private CurrencyType currencyType1;
	@Field(label = "充值货币1金额")
	private BigDecimal amount1;
	@Field(label = "充值货币2")
	private CurrencyType currencyType2;
	@Field(label = "充值货币2金额")
	private BigDecimal amount2;
	@Field(label = "充值总金额")
	private BigDecimal totalAmount;
	@Field(label = "外部单据是否已经创建")
	private Boolean isOuterCreated;
	@Field(label = "外部sn")
	private String outerSn;
	@Field(label = "二维码")
	private String qrCodeUrl;
	@Field(label = "微信openid")
	private String weixinOpenId;
	@Field(label = "支付方式")
	private PayType payType;
	@Field(label = "支付成功时间")
	private Date paidTime;
	@Field(label = "创建时间")
	private Date createdTime;
	@Field(label = "过期时间")
	private Date expiredTime;
	@Field(label = "提现单状态")
	private DepositStatus depositStatus;

	/* 扩展 */
	@Field(label = "用户id")
	private UserAdminSimpleVo user;

}