package com.zy.entity.mal;

import static io.gd.generator.api.query.Predicate.EQ;
import static io.gd.generator.api.query.Predicate.GTE;
import static io.gd.generator.api.query.Predicate.IN;
import static io.gd.generator.api.query.Predicate.LK;
import static io.gd.generator.api.query.Predicate.LT;
import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.annotation.view.View;
import io.gd.generator.annotation.view.ViewObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.NotBlank;

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
	private BigDecimal price;

	@View(type = String.class)
	@Field(label = "市场价")
	private BigDecimal marketPrice;

	@View(groups = { "ProductDetailVo", "ProductAdminVo" })
	@Field(label = "商品编码")
	private String skuCode;

	@View(type = String.class, groups = { "ProductDetailVo", "ProductAdminVo" })
	@Field(label = "库存数量")
	private Long stockQuantity;

	@Field(label = "锁定数量")
	private Long lockedCount;

	@Query(EQ)
	@Field(label = "是否上架")
	private Boolean isOn = false;

	@NotBlank
	@View(name = "image1Big")
	@View(name = "image1Thumbnail" )
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
	private Date createdTime;

}
