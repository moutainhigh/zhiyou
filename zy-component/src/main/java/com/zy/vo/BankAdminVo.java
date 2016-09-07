package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BankAdminVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "银行名称")
	private String name;
	@Field(label = "代码")
	private String code;
	@Field(label = "排序")
	private Integer orderNumber;

	/* 扩展 */

}