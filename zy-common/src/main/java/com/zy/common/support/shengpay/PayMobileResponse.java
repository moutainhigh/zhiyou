package com.zy.common.support.shengpay;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayMobileResponse {

	private String transStatus;

	private String orderNo;

	private String msg;

	public void PayMobileResponse(String transStatus, String orderNo, String msg) {
		this.transStatus = transStatus;
		this.orderNo = orderNo;
		this.msg = msg;
	}
}
