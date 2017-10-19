package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class LargeareaDaySalesAdminVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "大区名称")
	private String largeareaName;
	@Field(label = "大区值")
	private Integer largeareaValue;
	@Field(label = "销量")
	private Integer sales;
	@Field(label = "年份")
	private Integer year;
	@Field(label = "月份")
	private Integer month;
	@Field(label = "日")
	private Integer day;
	@Field(label = "创建时间")
	private Date createTime;

	/* 扩展 */

}