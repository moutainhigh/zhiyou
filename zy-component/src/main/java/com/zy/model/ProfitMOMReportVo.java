package com.zy.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProfitMOMReportVo {

	private Integer id;  //序号

	private Long bossId;  //总经理id

	private String bossNickname;  //总经理昵称

	private String bossName;  //总经理团队名称

	private List<ProfitMOMReportVo.ProfitMOMReportVoItem> profitMOMReportVoItems = new ArrayList<ProfitMOMReportVo.ProfitMOMReportVoItem>();

	@Getter
	@Setter
	public static class ProfitMOMReportVoItem {

		private String dateLabel;  //时间 yyyy-MM or yyyy-MM-dd

		private BigDecimal profit;  //总经理个人收益

		private BigDecimal teamProfit;  //团队收益

		private BigDecimal avgProfit;  //人均收入

		private double inTeamProfitRate;  //个人收入在团队的占比

	}

	private String profitMOMRateLabel;  //环比收益  （当前月-上个月）/ 上个月

	@Getter
	@Setter
	public static class ProfitMOMReportVoQueryModel {

		private String monthLabel;  //月份 2017-01, 2017-02, 2017-03
		private Integer pageNumber;
		private Integer pageSize;

	}

}
