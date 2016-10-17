package com.zy.admin.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderReportVo implements Serializable {
	
	private String nickname;  //服务商昵称
	
	private String phone;  //服务商手机
	
	private String rootName;  //系统名称
	
	private String parentNickname;  //直属特级昵称
	
	@Getter
	@Setter
	public static class OrderReportVoItem {
		
		private String timeLabel;  //时间 yyyy-MM or yyyy-MM-dd
		
		private String quantity;  //进货量

	}

}
