package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class TeamReportNewExportVo implements Serializable {
	/* 原生 */
	@Field(label = "时间")
	private String date;
	@Field(label = "姓名")
	private String userName;
	@Field(label = "区名")
	private String districtName;
	@Field(label = "手机号")
	private String phone;
	@Field(label = "特级人数")
	private Integer extraNumber;
	@Field(label = "新晋特级人数")
	private Integer newextraNumber;
	@Field(label = "新晋特级占比")
	private Double newextraRate;
	@Field(label = "省级人数")
	private Integer provinceNumber;
	@Field(label = "新晋省级人数")
	private Integer newprovinceNumber;
	@Field(label = "新晋省级占比")
	private Double newprovinceRate;
	@Field(label = "排名")
	private Integer ranking;





}