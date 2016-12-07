package com.zy.model;

import java.io.Serializable;
import java.util.Date;

import com.zy.entity.usr.User.UserRank;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderQuantityReportVo implements Serializable {

	@Field(label = "服务商昵称", order = 10)
	private String nickname;  //服务商昵称

	@Field(label = "服务商手机", order = 20)
	private String phone;  //服务商手机

	@Field(label = "已下单", order = 30)
	private Long orderedSum;  //已下单   --待支付、已支付、已发货、已收货、已退款、已取消

	@Field(label = "已支付", order = 40)
	private Long paidSum;  //已支付  --已发货、已收货、已退款

	@Field(label = "已发货", order = 50)
	private Long deliveredSum;  //已发货

	@Field(label = "已收货", order = 60)
	private Long receivedSum;  //已收货

	@Field(label = "已退款", order = 70)
	private Long refundedSum;  //已退款

	@Field(label = "已取消", order = 80)
	private Long canceledSum;  //已取消

	@Getter
	@Setter
	public static class OrderQuantityReportVoQueryModel {
		
		private String nicknameLK;
		private String phoneEQ;
		private UserRank userRankEQ;
		private Date createdTimeGTE;
		private Date createdTimeLT;
		private Date paidTimeGTE;
		private Date paidTimeLT;
		private Integer pageNumber;
		private Integer pageSize;

	}
	
}
