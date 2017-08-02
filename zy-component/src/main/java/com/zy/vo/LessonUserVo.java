package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class LessonUserVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "课程id")
	private Long lessonId;
	@Field(label = "用户id")
	private Long userId;
	@Field(label = "状态")
	private Integer lessonStatus;
	@Field(label = "备注")
	private String remark;
	@Field(label = "备注")
	private Long createById;
	@Field(label = "创建时间")
	private Date createDate;

	/* 扩展 */

}