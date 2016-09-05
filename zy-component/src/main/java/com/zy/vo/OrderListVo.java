package com.zy.vo;

import com.zy.entity.mal.Order.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
public class OrderListVo implements Serializable {
	/* 原生 */
	private Long id;
	private String sn;
	private Long userId;
	private Long sellerId;
	private String title;
	private OrderStatus orderStatus;

	/* 扩展 */
	private String createdTimeLabel;
	private String expiredTimeLabel;
	private String amount;
	private String deliveredTimeLabel;
	private List<OrderItemVo> orderItems = new ArrayList<>();

}