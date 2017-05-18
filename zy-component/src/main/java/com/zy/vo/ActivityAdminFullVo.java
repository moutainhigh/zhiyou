package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
public class ActivityAdminFullVo implements Serializable {
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
	@Field(label = "活动状态")
	private String status;
	@Field(label = "地区")
	private String province;
	@Field(label = "地区")
	private String city;
	@Field(label = "地区")
	private String district;
	@Field(label = "活动报名费")
	private String amountLabel;
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
	@Field(label = "活动报名")
	private List<ActivityApplyAdminVo> activityApplies = new ArrayList<>();
	@Field(label = "活动关注")
	private List<ActivityCollectAdminVo> activityCollects = new ArrayList<>();
	@Field(label = "活动签到")
	private List<ActivitySignInAdminVo> activitySignIns = new ArrayList<>();
	private Long maxCount;
	@Field(label = "活动票务类型")
	private Integer ticketType;

}