package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class TeamProvinceReportAdminVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "省份")
	private String province;
	@Field(label = "特级人数")
	private Integer v4Number;
	@Field(label = "省级人数")
	private Integer v3Number;
	@Field(label = "特级活跃人数")
	private Integer v4ActiveNumber;
	@Field(label = "特级活跃度")
	private Double v4ActiveRate;
	@Field(label = "特级活跃度排名")
	private Integer v4ActiveRank;
	@Field(label = "年份")
	private Integer year;
	@Field(label = "月份")
	private Integer month;
	@Field(label = "创建时间")
	private Date createTime;

	/* 扩展 */

}