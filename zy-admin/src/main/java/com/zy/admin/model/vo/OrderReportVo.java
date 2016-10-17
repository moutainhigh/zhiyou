package com.zy.admin.model.vo;

public class OrderReportVo {
	
	private String nickname;  //服务商昵称
	
	private String phone;  //服务商手机
	
	private String name;  //系统名称
	
	private String parentNickname;  //直属特级昵称
	
	public static class OrderReportVoItem {
		
		private String timeLabel;  //时间 yyyy-MM or yyyy-MM-dd
		
		private String quantity;  //进货量

		public String getTimeLabel() {
			return timeLabel;
		}

		public void setTimeLabel(String timeLabel) {
			this.timeLabel = timeLabel;
		}

		public String getQuantity() {
			return quantity;
		}

		public void setQuantity(String quantity) {
			this.quantity = quantity;
		}
		
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentNickname() {
		return parentNickname;
	}

	public void setParentNickname(String parentNickname) {
		this.parentNickname = parentNickname;
	}
}
