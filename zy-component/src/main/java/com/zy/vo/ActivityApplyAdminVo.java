package com.zy.vo;

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
	@Field(label = "用户id")
	private Long userId;
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
	@Field(label = "用户id",order = 999)
	private UserAdminSimpleVo user;
	@Field(label = "报名时间",order = 999)
	private String appliedTimeLabel;
	@Field(label = "邀请人id",order = 999)
	private UserAdminSimpleVo inviter;

}