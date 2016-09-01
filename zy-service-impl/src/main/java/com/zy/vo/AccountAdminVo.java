package com.gc.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class AccountAdminVo implements Serializable {
	/* 原生 */
	private Long userId;

	/* 扩展 */
	private BigDecimal money;
	private BigDecimal coin;
	private BigDecimal point;
	private UserAdminSimpleVo user;

}