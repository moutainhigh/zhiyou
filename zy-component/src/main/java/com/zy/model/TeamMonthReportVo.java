package com.zy.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TeamMonthReportVo {

	private Long bossId;  //总经理id

	private String bossNickname;  //总经理昵称

	private String bossName;  //总经理团队名称

	private Long inQuantitySum;  //团队进货量

	private Long lastMonthInQuantitySum;  //上个月团队进货量

	private String growthRateLabel;  //订单量环比

	private Long avgQuantity;  //当月特级人均订单量

	@Getter
	@Setter
	public static class TeamMonthReportVoQueryModel {

		private String monthLabel;  //月份 2017-01, 2017-02, 2017-03
		private Integer pageNumber;
		private Integer pageSize;

	}
}
