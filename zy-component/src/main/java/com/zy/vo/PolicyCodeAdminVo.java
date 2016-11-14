package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class PolicyCodeAdminVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "编号")
	private String code;
	@Field(label = "创建时间")
	private Date createdTime;
	@Field(label = "使用时间")
	private Date usedTime;
	@Field(label = "是否使用")
	private Boolean isUsed;
	@Field(label = "批次号")
	private String batchCode;

	/* 扩展 */
	@Field(label = "创建时间")
	private String createdTimeLabel;
	@Field(label = "使用时间")
	private String usedTimeLabel;

}