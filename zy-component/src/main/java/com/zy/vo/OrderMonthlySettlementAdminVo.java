package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class OrderMonthlySettlementAdminVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "年月")
	private String yearAndMonth;

	/* 扩展 */
	@Field(label = "结算时间")
	private String settledUpTimeLabel;

}