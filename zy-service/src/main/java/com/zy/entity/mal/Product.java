package com.zy.entity.mal;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.annotation.view.View;
import io.gd.generator.annotation.view.ViewObject;
import io.gd.generator.api.query.Predicate;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import static com.zy.entity.mal.Product.VO_ADMIN;
import static com.zy.entity.mal.Product.VO_DETAIL;
import static com.zy.entity.mal.Product.VO_LIST;
import static io.gd.generator.api.query.Predicate.*;

@Entity
@Table(name = "mal_product")
@Getter
@Setter
@ViewObject(groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
@QueryModel
@Type(label = "商品")
public class Product implements Serializable {

	public static final String VO_ADMIN = "ProductAdminVo";
	public static final String VO_LIST = "ProductListVo";
	public static final String VO_DETAIL = "ProductDetailVo";

	@Type(label = "商品价格类型")
	public enum ProductPriceType {
		一般价格, 矩阵价格, 脚本价格
	}

	@Id
	@Query(IN)
	@View
	private Long id;

	@NotBlank
	@Query(LK)
	@Field(label = "商品名")
	@View
	@Length(max = 100)
	private String title;

	@Lob
	@NotBlank
	@View(groups = { VO_DETAIL, VO_ADMIN })
	@Field(label = "商品详情")
	private String detail;

	@Field(label = "商品价格类型")
	@View(groups = { VO_ADMIN })
	private ProductPriceType productPriceType;

	@Field(label = "价格")
	@View
	private BigDecimal price;

	@Field(label = "价格脚本")
	@View(groups = { VO_ADMIN })
	@Column(length = 3000)
	private String priceScript;

	@Field(label = "市场价")
	@DecimalMin("0.00")
	@View
	private BigDecimal marketPrice;

	@View(groups = { VO_DETAIL, VO_ADMIN })
	@Field(label = "商品编码")
	private String skuCode;

	@Query(EQ)
	@Field(label = "是否上架")
	@NotNull
	@View(groups = { VO_ADMIN })
	private Boolean isOn;

	@NotBlank
	@View(name = "image1Big", groups = { VO_DETAIL, VO_ADMIN  })
	@View(name = "image1Thumbnail" )
	@View(groups = { VO_ADMIN })
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
	@View(groups = { VO_ADMIN })
	private Date createdTime;

	@View
	@Field(label = "是否结算")
	@Query(Predicate.EQ)
	private Boolean isSettlement;

}
