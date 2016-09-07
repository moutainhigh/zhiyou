package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class AccountAdminVo implements Serializable {
	/* 原生 */
	@Field(label = "用户id")
	private Long userId;

	/* 扩展 */
	@Field(label = "money",order = 999)
	private BigDecimal money;
	@Field(label = "coin",order = 999)
	private BigDecimal coin;
	@Field(label = "point",order = 999)
	private BigDecimal point;
	@Field(label = "用户id",order = 999)
	private UserAdminSimpleVo user;

}