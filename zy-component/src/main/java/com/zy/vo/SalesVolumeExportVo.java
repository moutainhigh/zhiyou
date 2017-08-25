package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SalesVolumeExportVo implements Serializable {
	/* 原生 */
	@Field(label = "姓名")
	private String userName;
	@Field(label = "手机号")
	private String userPhone;
	@Field(label = "所属大区")
	private String areaType;
	@Field(label = "达成量")
	private Integer amountReached;
	@Field(label = "目标量")
	private Integer amountTarget;
	@Field(label = "达成率 （%）")
	private Double achievement;
	@Field(label = "排名")
	private Integer ranking;
	@Field(label = "排名升降情况")
	private String rankDetail;

}