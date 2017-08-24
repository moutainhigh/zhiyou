package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SalesVolumeListVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "用户等级")
	private Integer userRank;
	@Field(label = "用户名")
	private String userName;
	@Field(label = "用户手机号")
	private String userPhone;
	@Field(label = "达成量")
	private Integer amountReached;
	@Field(label = "目标量")
	private Integer amountTarget;
	@Field(label = "达成率")
	private Double achievement;
	@Field(label = "排名")
	private Integer ranking;
	@Field(label = "上升或下降名次")
	private Integer number;
	@Field(label = "1 上升 2 持平 3 下降")
	private Integer type;
	@Field(label = "大区类型：1 东 2 南 3 西 4 北 5 中")
	private Integer areaType;

	/* 扩展 */
	@Field(label = "插入时间")
	private String createTimeLabel;

}