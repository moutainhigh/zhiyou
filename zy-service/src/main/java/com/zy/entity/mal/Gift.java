package com.zy.entity.mal;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.api.query.Predicate;

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
@Table(name = "mal_gift")
@Getter
@Setter
@QueryModel
@Type(label = "礼品")
public class Gift implements Serializable {

	@Id
	@Query(Predicate.IN)
	@Field(label = "id")
	private Long id;

	@NotBlank
	@Query(Predicate.LK)
	@Field(label = "商品名")
	private String title;

	@Lob
	@NotBlank
	@Field(label = "商品详情")
	private String detail;

	@NotNull
	@Field(label = "成本价")
	private BigDecimal price;

	@NotNull
	@Field(label = "市场价")
	private BigDecimal marketPrice;

	@Field(label = "商品编码")
	private String skuCode;

	@Field(label = "库存数量")
	private Long stockQuantity;

	@Field(label = "锁定数量")
	private Long lockedCount;

	@Query(Predicate.EQ)
	@Field(label = "是否上架")
	private Boolean isOn = false;

	@NotNull
	@Field(label = "排序")
	private Integer orderNumber;

	@NotBlank
	@Field(label = "主图")
	private String image1;

	@Field(label = "主图2")
	private String image2;

	@Field(label = "主图3")
	private String image3;

	@Field(label = "主图4")
	private String image4;

	@Field(label = "主图5")
	private String image5;

	@Field(label = "主图6")
	private String image6;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Query({Predicate.GTE, Predicate.LT})
	@Field(label = "创建时间")
	private Date createdTime;

}
