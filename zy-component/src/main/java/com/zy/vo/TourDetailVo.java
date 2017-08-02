package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TourDetailVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "标题")
	private String title;
	@Field(label = "摘要")
	private String brief;
	@Field(label = "作者")
	private String author;
	@Field(label = "是否发布")
	private Boolean isReleased;
	@Field(label = "主图")
	private String image;
	@Field(label = "内容")
	private String content;
	@Field(label = "访问数")
	private Long visitCount;
	@Field(label = "排序")
	private Integer orderNumber;
	@Field(label = "是否热门")
	private Boolean isHot;

	/* 扩展 */
	@Field(label = "发布时间")
	private String releasedTimeLabel;
	@Field(label = "主图")
	private String imageBig;
	@Field(label = "主图")
	private String imageThumbnail;
	@Field(label = "创建时间")
	private String createdTimeLabel;

}