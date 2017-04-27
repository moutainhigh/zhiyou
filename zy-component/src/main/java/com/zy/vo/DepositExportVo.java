package com.zy.vo;

import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.fnc.Deposit.DepositStatus;
import com.zy.entity.fnc.PayType;
import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DepositExportVo implements Serializable {
	/* 原生 */
	@Field(label = "标题", order = 15)
	private String title;
	@Field(label = "充值单号", order = 20)
	private String sn;
	@Field(label = "充值货币1", order = 25)
	private CurrencyType currencyType1;
	@Field(label = "支付方式", order = 40)
	private PayType payType;
	@Field(label = "充值单状态", order = 60)
	private DepositStatus depositStatus;
	@Field(label = "备注", order = 65)
	private String remark;
	@Field(label = "银行汇款截图", order = 70)
	private String offlineImage;
	@Field(label = "银行汇款备注", order = 75)
	private String offlineMemo;

	/* 扩展 */
	@Field(label = "昵称", order = 5)
	private String userNickname;
	@Field(label = "手机", order = 10)
	private String userPhone;
	@Field(label = "充值货币1金额", order = 30)
	private String amount1Label;
	@Field(label = "支付成功时间", order = 45)
	private String paidTimeLabel;
	@Field(label = "创建时间", order = 50)
	private String createdTimeLabel;
	@Field(label = "过期时间", order = 55)
	private String expiredTimeLabel;


}