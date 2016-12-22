package com.zy.vo;

import com.zy.entity.mal.Order.OrderStatus;
import com.zy.entity.mal.Order.OrderType;
import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderListVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "订单编号")
	private String sn;
	@Field(label = "用户id")
	private Long userId;
	@Field(label = "卖家id")
	private Long sellerId;
	@Field(label = "是否支付给平台")
	private Boolean isPayToPlatform;
	@Field(label = "标题")
	private String title;
	@Field(label = "订单状态")
	private OrderStatus orderStatus;
	@Field(label = "订单类型")
	private OrderType orderType;
	@Field(label = "应付总金额")
	private BigDecimal amount;
	@Field(label = "商品id")
	private Long productId;

	/* 扩展 */
	@Field(label = "isPlatformDeliver")
	private Boolean isPlatformDeliver;
	@Field(label = "下单时间")
	private String createdTimeLabel;
	@Field(label = "过期时间")
	private String expiredTimeLabel;
	@Field(label = "应付总金额")
	private String amountLabel;
	@Field(label = "商品图")
	private String imageThumbnail;
	@Field(label = "orderItems")
	private List<OrderItemVo> orderItems = new ArrayList<>();

}