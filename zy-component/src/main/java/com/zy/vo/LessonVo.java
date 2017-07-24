package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class LessonVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "标题")
	private String title;
	@Field(label = "级别")
	private Integer lessonLevel;
	@Field(label = "上级ID")
	private String parentAllId;
	@Field(label = "创建人")
	private Long createById;
	@Field(label = "创建时间")
	private Date createDate;
	@Field(label = "状态标记位")
	private Integer viewFlage;

	/* 扩展 */

}