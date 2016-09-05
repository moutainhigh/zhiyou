package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
public class ActivityDetailVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "标题")
	private String title;
	@Field(label = "详细地址")
	private String address;
	@Field(label = "纬度")
	private Double latitude;
	@Field(label = " 经度")
	private Double longitude;
	@Field(label = "活动详情")
	private String detail;
	@Field(label = "关注数")
	private Long collectedCount;
	@Field(label = "报名数")
	private Long appliedCount;
	@Field(label = "浏览数")
	private Long viewedCount;

	/* 扩展 */
	@Field(label = "活动状态")
	private String status;
	@Field(label = "地区")
	private String province;
	@Field(label = "地区")
	private String city;
	@Field(label = "地区")
	private String district;
	@Field(label = "活动主图")
	private String imageBig;
	@Field(label = "活动主图")
	private String imageThumbnail;
	@Field(label = "报名截止时间")
	private String applyDeadlineLabel;
	@Field(label = "活动开始时间")
	private String startTimeLabel;
	@Field(label = "活动结束时间")
	private String endTimeLabel;
	@Field(label = "appliedUsers")
	private List<UserSimpleVo> appliedUsers = new ArrayList<>();

}