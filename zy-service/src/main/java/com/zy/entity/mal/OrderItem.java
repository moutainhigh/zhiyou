package com.zy.entity.mal;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.view.View;
import io.gd.generator.annotation.view.ViewObject;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "mal_order_item")
@Getter
@Setter
@ViewObject(groups = { "OrderItemVo", "OrderItemAdminVo" })
@Type(label = "订单子项")
public class OrderItem implements Serializable {

	@Id
	@Field(label = "id")
	@View
	private Long id;

	@NotNull
	@Field(label = "订单id")
	@View
	private Long orderId;
	
	@NotNull
	@Field(label = "商品id")
	@View
	private Long productId;

	@NotBlank
	@Field(label = "标题")
	@View
	private String title;

	@NotBlank
	@View(name = "imageThumbnail")
	@Field(label = "商品图")
	@View(groups = "OrderItemAdminVo")
	private String image;

	@NotNull
	@DecimalMin("0.00")
	@Field(label = "原价/市场价")
	@View
	private BigDecimal marketPrice;

	@NotNull
	@DecimalMin("0.00")
	@Field(label = "单价")
	@View
	private BigDecimal price;

	@NotNull
	@Min(1L)
	@Field(label = "数量")
	@View
	private Long quantity;

	@NotNull
	@DecimalMin("0.00")
	@Field(label = "子项金额", description = "单价 * 数量")
	@View
	private BigDecimal amount;

}
