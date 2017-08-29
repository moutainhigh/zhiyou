package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TeamProvinceReportExportVo implements Serializable {

	/* 原生 */
	@Field(label = "序号")
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
	@Field(label = "时间")
	private String date;
}