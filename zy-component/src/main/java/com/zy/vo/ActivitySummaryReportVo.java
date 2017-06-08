package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Author: Xuwq
 * Date: 2017/6/6.
 */
@Getter
@Setter
public class ActivitySummaryReportVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "标题")
	private String title;
	@Field(label = "地区")
	private Long areaId;
	@Field(label = "详细地址")
	private String address;
	@Field(label = "纬度")
	private Double latitude;
	@Field(label = " 经度")
	private Double longitude;
	@Field(label = "活动主图")
	private String image;
	@Field(label = "活动详情")
	private String detail;
	@Field(label = "签到数")
	private Long signedInCount;
	@Field(label = "关注数")
	private Long collectedCount;
	@Field(label = "报名数")
	private Long appliedCount;
	@Field(label = "浏览数")
	private Long viewedCount;
	@Field(label = "是否发布")
	private Boolean isReleased;

	/* 扩展 */
	@Field(label = "地区")
	private String province;
	@Field(label = "地区")
	private String city;
	@Field(label = "地区")
	private String district;
	@Field(label = "活动开始时间")
	private String startTimeLabel;
	@Field(label = "活动开始时间")
	private String startTimeFormatted;
	@Field(label = "活动结束时间")
	private String endTimeLabel;
	@Field(label = "活动结束时间")
	private String endTimeFormatted;
	@Field(label = "未支付人数")
	private Long nonPayment;
	@Field(label = "已支付人数")
	private Long payment;
}