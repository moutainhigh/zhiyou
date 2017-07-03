package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BlackOrWhiteAdminVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "用户Id")
	private Long userId;
	@Field(label = "数量")
	private Integer number;
	@Field(label = "类型")
	private Integer type;

	/* 扩展 */
	@Field(label = "用户")
	private UserAdminSimpleVo User;

}