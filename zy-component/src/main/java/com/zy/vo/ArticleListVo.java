package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ArticleListVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "标题")
	private String title;

	/* 扩展 */
	@Field(label = "发布时间")
	private String releasedTimeLabel;
	@Field(label = "主图")
	private String imageThumbnail;

}