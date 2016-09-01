package com.gc.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class OrderListVo implements Serializable {
	/* 原生 */

	/* 扩展 */
	private String createdTimeLabel;
	private String expiredTimeLabel;
	private String amount;

}