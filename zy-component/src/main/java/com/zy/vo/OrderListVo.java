package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.mal.Order.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

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
	@Field(label = "卖家id")
	private Long parentId;
	@Field(label = "标题")
	private String title;
	@Field(label = "订单状态")
	private OrderStatus orderStatus;
	@Field(label = "应付总金额")
	private BigDecimal amount;

	/* 扩展 */
	@Field(label = "下单时间")
	private String createdTimeLabel;
	@Field(label = "过期时间")
	private String expiredTimeLabel;
	@Field(label = "发货时间")
	private String deliveredTimeLabel;
	@Field(label = "orderItems")
	private List<OrderItemVo> orderItems = new ArrayList<>();

}