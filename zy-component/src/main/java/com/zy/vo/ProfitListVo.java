package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ProfitListVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "收益单号")
	private String sn;
	@Field(label = "收益标题")
	private String title;

	/* 扩展 */
	@Field(label = "收益单状态")
	private String profitStatusStyle;
	@Field(label = "金额")
	private String amountLabel;
	@Field(label = "创建时间")
	private String createdTimeLabel;
	@Field(label = "发放时间")
	private String grantedTimeLabel;

}