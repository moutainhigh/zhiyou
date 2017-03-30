package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.act.ActivityApply.ActivityApplyStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ActivityApplyListVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "活动报名状态")
	private ActivityApplyStatus activityApplyStatus;
	@Field(label = "活动id")
	private Long activityId;

	/* 扩展 */
	@Field(label = "用户id")
	private UserAdminSimpleVo user;
	@Field(label = "活动报名费")
	private String amountLabel;
	@Field(label = "活动id")
	private String activityTitle;
	@Field(label = "活动id")
	private String activityImageThumbnail;
	@Field(label = "报名时间")
	private String appliedTimeLabel;

}