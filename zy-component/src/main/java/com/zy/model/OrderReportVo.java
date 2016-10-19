package com.zy.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.zy.entity.usr.User.UserRank;

@Getter
@Setter
public class OrderReportVo implements Serializable {
	
	private String nickname;  //服务商昵称
	
	private String phone;  //服务商手机
	
	private String rootName;  //系统名称
	
	private String v4UserNickname;  //直属特级昵称
	
	private List<OrderReportVo.OrderReportVoItem> orderReportVoItems = new ArrayList<OrderReportVo.OrderReportVoItem>();
	
	@Getter
	@Setter
	public static class OrderReportVoItem {
		
		private String timeLabel;  //时间 yyyy-MM or yyyy-MM-dd
		
		private Long quantity;  //进货量

	}
	
	@Getter
	@Setter
	public static class OrderReportVoQueryModel {
		private Long provinceIdEQ;
		private Long cityIdEQ;
		private Long districtIdEQ;
		private String rootRootNameLK;
		private String v4UserNicknameLK;
		private String phoneEQ;
		private String nicknameLK;
		private UserRank userRankEQ;
		private Integer pageNumber;
		private Integer pageSize;

	}

}
