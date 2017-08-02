package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.mal.Product.ProductPriceType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
	@Field(label = "商品价格类型")
	private ProductPriceType productPriceType;
	@Field(label = "价格")
	private BigDecimal price;
	@Field(label = "价格脚本")
	private String priceScript;
	@Field(label = "市场价")
	private BigDecimal marketPrice;
	@Field(label = "商品编码")
	private String skuCode;
	@Field(label = "是否上架")
	private Boolean isOn;
	@Field(label = "主图")
	private String image1;
	@Field(label = "创建时间")
	private Date createdTime;
	@Field(label = "是否结算")
	private Boolean isSettlement;

	/* 扩展 */
	@Field(label = "主图")
	private String image1Big;
	@Field(label = "主图")
	private String image1Thumbnail;

}