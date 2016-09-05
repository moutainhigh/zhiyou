package com.zy.entity.mal;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.annotation.view.View;
import io.gd.generator.annotation.view.ViewObject;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import static io.gd.generator.api.query.Predicate.*;

@Entity
@Table(name = "mal_product")
@Getter
@Setter
@ViewObject(groups = { "ProductListVo", "ProductDetailVo", "ProductAdminVo" })
@QueryModel
@Type(label = "商品")
public class Product implements Serializable {

	@Id
	@Query(IN)
	@View
	private Long id;

	@NotBlank
	@Query(LK)
	@Field(label = "商品名")
	@View
	private String title;

	@Lob
	@NotBlank
	@View(groups = { "ProductDetailVo", "ProductAdminVo" })
	@Field(label = "商品详情")
	private String detail;

	@NotNull
	@View(type = String.class)
	@Field(label = "价格")
	@DecimalMin("0.00")
	@View(groups = { "ProductAdminVo" })
	private BigDecimal price;

	@NotNull
	@View(type = String.class)
	@Field(label = "价格")
	@DecimalMin("0.00")
	@View(groups = { "ProductAdminVo" })
	private BigDecimal price1;

	@NotNull
	@View(type = String.class)
	@Field(label = "价格")
	@DecimalMin("0.00")
	@View(groups = { "ProductAdminVo" })
	private BigDecimal price2;

	@NotNull
	@View(type = String.class)
	@Field(label = "价格")
	@DecimalMin("0.00")
	@View(groups = { "ProductAdminVo" })
	private BigDecimal price3;

	@NotNull
	@View(type = String.class)
	@Field(label = "价格")
	@DecimalMin("0.00")
	@View(groups = { "ProductAdminVo" })
	private BigDecimal price4;

	@View(type = String.class)
	@Field(label = "市场价")
	@DecimalMin("0.00")
	@View(groups = { "ProductAdminVo" })
	private BigDecimal marketPrice;

	@View(groups = { "ProductDetailVo", "ProductAdminVo" })
	@Field(label = "商品编码")
	private String skuCode;

	@Query(EQ)
	@Field(label = "是否上架")
	@NotNull
	@View(groups = { "ProductAdminVo" })
	private Boolean isOn;

	@NotBlank
	@View(name = "image1Big")
	@View(name = "image1Thumbnail" )
	@View(groups = { "ProductAdminVo" })
	@Field(label = "主图")
	private String image1;

	@Field(label = "图片2")
	private String image2;

	@Field(label = "图片2")
	private String image3;

	@Field(label = "图片3")
	private String image4;

	@Field(label = "图片5")
	private String image5;

	@Field(label = "图片6")
	private String image6;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Query({GTE,LT})
	@Field(label = "创建时间")
	@View(groups = { "ProductAdminVo" })
	private Date createdTime;

}
