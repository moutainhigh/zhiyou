package com.gc.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class OrderItemVo implements Serializable {
	/* 原生 */

	/* 扩展 */
	private String imageThumbnail;
	private String price;
	private String amount;

}