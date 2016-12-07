package com.zy.model;

import java.math.BigDecimal;
import java.util.Date;

import com.zy.entity.usr.User.UserRank;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinanceReportVo {

	@Field(label = "服务商id", order = 10)
	private Long userId;

	@Field(label = "服务商", order = 20)
	private String userNickname;  //服务商昵称

	@Field(label = "手机号", order = 30)
	private String userPhone;  //服务商手机

	@Field(label = "积分充值", order = 40)
	private BigDecimal depositAmount;  //积分充值

	@Field(label = "积分体现", order = 50)
	private BigDecimal withdrawAmount;  //积分体现

	@Field(label = "积分支付", order = 60)
	private BigDecimal paymentAmount;  //积分支付

	@Field(label = "积分收益", order = 70)
	private BigDecimal profitAmount;  //积分收益

	@Field(label = "积分余额", order = 80)
	private BigDecimal accountAmount;  //积分余额
	
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
