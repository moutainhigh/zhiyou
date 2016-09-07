package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemAdminVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "订单id")
	private Long orderId;
	@Field(label = "商品id")
	private Long productId;
	@Field(label = "标题")
	private String title;
	@Field(label = "商品图")
	private String image;
	@Field(label = "原价/市场价")
	private BigDecimal marketPrice;
	@Field(label = "单价")
	private BigDecimal price;
	@Field(label = "数量")
	private Long quantity;
	@Field(label = "子项金额")
	private BigDecimal amount;

	/* 扩展 */
	@Field(label = "商品图")
	private String imageThumbnail;

}