package com.gc.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemVo implements Serializable {
	/* 原生 */
	private Long id;
	private Long orderId;
	private Long productId;
	private String title;
	private BigDecimal marketPrice;
	private Long quantity;

	/* 扩展 */
	private String imageThumbnail;
	private String price;
	private String amount;

}