package com.zy.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDetailVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "商品名")
	private String title;
	@Field(label = "商品详情")
	private String detail;
	@Field(label = "商品编码")
	private String skuCode;
	@Field(label = "价格")
	private BigDecimal price;
	@Field(label = "市场价")
	private BigDecimal marketPrice;

	/* 扩展 */
	@Field(label = "主图")
	private String image1Big;
	@Field(label = "主图")
	private String image1Thumbnail;

}