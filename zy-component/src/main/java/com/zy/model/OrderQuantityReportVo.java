package com.zy.model;

import java.io.Serializable;
import java.util.Date;

import com.zy.entity.usr.User.UserRank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderQuantityReportVo implements Serializable {

	private String nickname;  //服务商昵称
	
	private String phone;  //服务商手机
	
	private Long orderedSum;  //已下单   --待支付、已支付、已发货、已收货、已退款、已取消
	
	private Long paidSum;  //已支付  --已发货、已收货、已退款
	
	private Long deliveredSum;  //已发货
	
	private Long receivedSum;  //已收货
	
	private Long refundedSum;  //已退款
	
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
