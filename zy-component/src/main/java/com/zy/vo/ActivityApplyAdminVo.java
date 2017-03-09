package com.zy.vo;

import com.zy.entity.act.ActivityApply.ActivityApplyStatus;
import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class ActivityApplyAdminVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "活动报名状态")
	private ActivityApplyStatus activityApplyStatus;
	@Field(label = "用户id")
	private Long userId;
	@Field(label = "代付人id")
	private Long payerUserId;
	@Field(label = "活动id")
	private Long activityId;
	@Field(label = "报名时间")
	private Date appliedTime;
	@Field(label = "是否取消")
	private Boolean isCancelled;
	@Field(label = "邀请人id")
	private Long inviterId;
	@Field(label = "短信通知是否已发送")
	private Boolean isSmsSent;

	/* 扩展 */
	@Field(label = "用户id")
	private UserAdminSimpleVo user;
	@Field(label = "代付人id")
	private UserAdminSimpleVo payerUser;
	@Field(label = "活动报名费")
	private String amountLabel;
	@Field(label = "报名时间")
	private String appliedTimeLabel;
	@Field(label = "邀请人id")
	private UserAdminSimpleVo inviter;

}