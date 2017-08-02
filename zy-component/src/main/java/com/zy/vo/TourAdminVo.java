package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TourAdminVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "标题")
	private String title;
	@Field(label = "摘要")
	private String brief;
	@Field(label = "创建人")
	private String createName;
	@Field(label = "是否发布")
	private Boolean isReleased;
	@Field(label = "主图")
	private String image;
	@Field(label = "内容")
	private String content;
	@Field(label = "发布时间")
	private String createdTime;
	@Field(label = "更新时间")
	private String updateTime;
	@Field(label = "天数")
	private Integer days;

}