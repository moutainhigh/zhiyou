package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class UserSpreadAdminVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long Id;
	@Field(label = "userId")
	private Long userId;
	@Field(label = "provinceId")
	private Long provinceId;
	@Field(label = "provinceName")
	private String provinceName;
	@Field(label = "V4")
	private Integer v4;
	@Field(label = "V3")
	private Integer v3;
	@Field(label = "V2")
	private Integer v2;
	@Field(label = "V1")
	private Integer v1;
	@Field(label = "V0")
	private Integer v0;
	@Field(label = "year")
	private Integer year;
	@Field(label = "month")
	private Integer month;
	@Field(label = "createDate")
	private Date createDate;

	/* 扩展 */

}