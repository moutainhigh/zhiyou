package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class OrderStoreVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "用户id")
	private Long userId;
	@Field(label = "订单ID")
	private Long orderId;
	@Field(label = "类型")
	private Integer type;
	@Field(label = "数量")
	private Integer number;
	@Field(label = "操作前数量")
	private Integer beforeNumber;
	@Field(label = "操作后数量")
	private Integer afterNumber;
	@Field(label = "创建人")
	private Long createBy;
	@Field(label = "是否最后")
	private Integer isEnd;

	/* 扩展 */
	@Field(label = "创建时间")
	private String createDate;
	@Field(label = "订单编号")
	private String sn;
}