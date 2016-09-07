package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AdminAdminVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "用户id")
	private Long userId;
	@Field(label = "角色名")
	private String roleNames;
	@Field(label = "站点id")
	private Long siteId;

	/* 扩展 */
	@Field(label = "用户id")
	private UserAdminSimpleVo user;

}