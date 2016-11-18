package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PolicyCodeExportVo implements Serializable {
	/* 原生 */
	@Field(label = "编号", order = 10)
	private String code;
	@Field(label = "批次号", order = 5)
	private String batchCode;

	/* 扩展 */
	@Field(label = "是否使用", order = 15)
	private String isUsedLabel;

}