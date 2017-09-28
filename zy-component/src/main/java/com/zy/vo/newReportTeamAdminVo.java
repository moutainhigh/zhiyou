package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class newReportTeamAdminVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "provinceName")
	private String provinceName;
	@Field(label = "provinceId")
	private Long provinceId;
	@Field(label = "year")
	private Integer year;
	@Field(label = "month")
	private Integer month;
	@Field(label = "number")
	private Integer number;
	@Field(label = "rank")
	private Integer rank;
	@Field(label = "rankChange")
	private Integer rankChange;
	@Field(label = "createDate")
	private Date createDate;
	@Field(label = "region")
	private Integer region;

	/* 扩展 */

}