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
	@Field(label = "money")
	private BigDecimal money;
	@Field(label = "coin")
	private BigDecimal coin;
	@Field(label = "point")
	private BigDecimal point;
	@Field(label = "用户id")
	private UserAdminSimpleVo user;

}