package com.zy.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class OrderDetailVo implements Serializable {
	/* 原生 */
	private String refundRemark;
	private String remark;

	/* 扩展 */
	private String createdTimeLabel;
	private String expiredTimeLabel;
	private String paidTimeLabel;
	private String refundedTimeLabel;
	private String amount;
	private String refund;

}