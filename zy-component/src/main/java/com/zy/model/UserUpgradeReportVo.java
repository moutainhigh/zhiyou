package com.zy.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpgradeReportVo {

	private Long count;

	private Long growthCount;

	private String growthRateLabel;


	private Long v4Count;

	private Long v4GrowthCount;

	private String v4GrowthRateLabel;

	private String v4ConversionRateLabel;


	private Long v3Count;

	private Long v3GrowthCount;

	private String v3GrowthRateLabel;


	private Long v2Count;

	private Long v2GrowthCount;

	private String v2GrowthRateLabel;


	private Long v1Count;

	private Long v1GrowthCount;

	private String v1GrowthRateLabel;


	private Long v0Count;

	private Long v0ConversionCount;

	private String v0ConversionRateLabel;

	@Getter
	@Setter
	public static class UserUpgradeReportQueryModel {
		private Long provinceIdEQ;
		private Long cityIdEQ;
		private Long districtIdEQ;

		private String rootRootNameLK;
	}

}
