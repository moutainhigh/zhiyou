package com.zy.model;

import com.zy.entity.usr.User.UserRank;
import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class FinanceReportVo {

	@Field(label = "服务商id", order = 10)
	private Long userId;

	@Field(label = "服务商", order = 20)
	private String userNickname;  //服务商昵称

	@Field(label = "手机号", order = 30)
	private String userPhone;  //服务商手机

	@Field(label = "U币充值", order = 40)
	private BigDecimal depositAmount;  //U币充值

	@Field(label = "U币赠送", order = 50)
	private BigDecimal transferAmount;  //U币赠送/转让 transfer

	@Field(label = "U币支付", order = 60)
	private BigDecimal paymentAmount;  //U币支付

	@Field(label = "U币余额", order = 80)
	private BigDecimal accountAmount;  //U币余额

	@Field(label = "积分收益", order = 85)
	private BigDecimal profitPointAmount;  //积分收益

	@Field(label = "差价收益", order = 90)
	private BigDecimal differencePointAmount;  //差价收益

	@Field(label = "积分提现", order = 95)
	private BigDecimal withdrawPointAmount;  //积分提现

	@Field(label = "积分支付", order = 100)
	private BigDecimal paymentPointAmount;  //积分支付

	@Field(label = "积分余额", order = 105)
	private BigDecimal accountPointAmount;  //积分余额
	
	@Getter
	@Setter
	public static class FinanceReportVoQueryModel {
		private String nicknameLK;
		private String phoneEQ;
		private UserRank userRankEQ;
		private Date timeGTE;
		private Date timeLT;
		private Integer pageNumber;
		private Integer pageSize;

	}
	
}
