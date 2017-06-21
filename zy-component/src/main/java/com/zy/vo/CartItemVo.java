package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class CartItemVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "用户id")
	private Long userId;
	@Field(label = "商品id")
	private Long productId;
	@Field(label = "单价")
	private BigDecimal price;
	@Field(label = "数量")
	private Long quantity;
	@Field(label = "是否结算")
	private Boolean isSettlement;

	/* 扩展 */
	@Field(label = "商品id")
	private String productTitle;
	@Field(label = "商品id")
	private String productImageThumbnail;
	@Field(label = "商品id")
	private BigDecimal marketPrice;

}