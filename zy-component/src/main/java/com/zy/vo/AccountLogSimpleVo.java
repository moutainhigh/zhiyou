package com.gc.vo;

import com.gc.entity.fnc.AccountLog.InOut;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class AccountLogSimpleVo implements Serializable {
	/* 原生 */
	private Long id;
	private InOut inOut;
	private String title;
	private Long userId;
	private BigDecimal transAmount;
	private BigDecimal afterAmount;

	/* 扩展 */
	private String transTimeLabel;

}