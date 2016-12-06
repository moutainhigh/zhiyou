package com.zy.model;

import java.math.BigDecimal;
import java.util.Date;

import com.zy.entity.usr.User.UserRank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinanceReportVo {

	private Long userId;
	
	private String userNickname;  //服务商昵称
	
	private String userPhone;  //服务商手机
	
	private BigDecimal depositAmount;  //积分充值
	
	private BigDecimal withdrawAmount;  //积分体现
	
	private BigDecimal paymentAmount;  //积分支付
	
	private BigDecimal profitAmount;  //积分收益
	
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
