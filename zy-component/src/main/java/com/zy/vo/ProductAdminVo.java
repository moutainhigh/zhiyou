package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ProductAdminVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "商品名")
	private String title;
	@Field(label = "商品详情")
	private String detail;
	@Field(label = "商品编码")
	private String skuCode;

	/* 扩展 */
	@Field(label = "个数")
	private String number;
	@Field(label = "价格")
	private String price;
	@Field(label = "市场价")
	private String marketPrice;
	@Field(label = "库存数量")
	private String stockQuantity;
	@Field(label = "主图")
	private String image1Big;
	@Field(label = "主图")
	private String image1Thumbnail;

}