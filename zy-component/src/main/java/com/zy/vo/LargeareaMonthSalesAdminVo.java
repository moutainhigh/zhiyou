package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class LargeareaMonthSalesAdminVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "大区名称")
	private String largeareaName;
	@Field(label = "大区值")
	private Integer largeareaValue;
	@Field(label = "销量")
	private Integer sales;
	@Field(label = "目标销量")
	private Integer targetCount;
	@Field(label = "年份")
	private Integer year;
	@Field(label = "月份")
	private Integer month;
	@Field(label = "销量同比")
	private Double sameRate;
	@Field(label = "销量环比")
	private Double relativeRate;
	@Field(label = "创建时间")
	private Date createTime;

	/* 扩展 */

}