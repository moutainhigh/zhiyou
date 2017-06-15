package com.zy.entity.mal;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.annotation.view.View;
import io.gd.generator.annotation.view.ViewObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "mal_cart_item", uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "productId"}))
@Getter
@Setter
@QueryModel
@Type(label = "购物车")
@ViewObject(groups = {CartItem.VO})
public class CartItem implements Serializable {

	public static final String VO = "CartItemVo";
	
	@Id
	@Field(label = "id")
	@View
	private Long id;

	@NotNull
	@Field(label = "用户id")
	@View
	private Long userId;
	
	@NotNull
	@Field(label = "商品id", description = "暂时保留")
	@View
	@View(name = "productTitle", type = String.class, groups = VO)
	@View(name = "productImageThumbnail", type = String.class, groups = VO)
	@View(name = "marketPrice", type = BigDecimal.class, groups = VO)
	private Long productId;

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

}
