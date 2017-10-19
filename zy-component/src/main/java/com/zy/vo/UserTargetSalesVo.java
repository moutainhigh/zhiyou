package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class UserTargetSalesVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "用户id")
	private Long userId;
	@Field(label = "目标销量")
	private Integer targetCount;
	@Field(label = "年份")
	private Integer year;
	@Field(label = "月份")
	private Integer month;
	@Field(label = "创建时间")
	private Date createTime;

	/* 扩展 */
	@Field(label = "用户id")
	private UserAdminVo user;

}