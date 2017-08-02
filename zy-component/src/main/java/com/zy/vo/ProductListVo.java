package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class ProductListVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "商品名")
	private String title;
	@Field(label = "价格")
	private BigDecimal price;
	@Field(label = "市场价")
	private BigDecimal marketPrice;
	@Field(label = "是否结算")
	private Boolean isSettlement;

	/* 扩展 */
	@Field(label = "主图")
	private String image1Thumbnail;

}