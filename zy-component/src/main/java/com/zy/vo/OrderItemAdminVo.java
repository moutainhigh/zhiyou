package com.zy.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemAdminVo implements Serializable {
	/* 原生 */
	private Long id;
	private Long orderId;
	private Long productId;
	private String title;
	private String image;
	private BigDecimal marketPrice;
	private BigDecimal price;
	private Long quantity;
	private BigDecimal amount;

	/* 扩展 */
	private String imageThumbnail;

}