package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class TeamReportNewAdminVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "userId")
	private Long userId;
	@Field(label = "userName")
	private String userName;
	@Field(label = "districtName")
	private String districtName;
	@Field(label = "districtId")
	private Long districtId;
	@Field(label = "extraNumber")
	private Integer extraNumber;
	@Field(label = "newextraNumber")
	private Integer newextraNumber;
	@Field(label = "newextraRate")
	private Double newextraRate;
	@Field(label = "provinceNumber")
	private Integer provinceNumber;
	@Field(label = "newprovinceNumber")
	private Integer newprovinceNumber;
	@Field(label = "newprovinceRate")
	private Double newprovinceRate;
	@Field(label = "ranking")
	private Integer ranking;
	@Field(label = "year")
	private Integer year;
	@Field(label = "month")
	private Integer month;
	@Field(label = "phone")
	private String phone;
	@Field(label = "createDate")
	private Date createDate;
	@Field(label = "sleepextraNumber")
	private Integer sleepextraNumber;
	@Field(label = "changRanking")
	private Integer changRanking;
	/* 扩展 */

}