package com.zy.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class ActivityApplyAdminVo implements Serializable {
	/* 原生 */
	private Long id;
	private Long userId;
	private Long activityId;
	private Date appliedTime;
	private Boolean isCancelled;
	private Long inviterId;

	/* 扩展 */
	private UserAdminSimpleVo user;
	private String appliedTimeLabel;
	private UserAdminSimpleVo inviter;

}