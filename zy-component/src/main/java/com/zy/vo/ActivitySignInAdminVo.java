package com.zy.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class ActivitySignInAdminVo implements Serializable {
	/* 原生 */
	private Long id;
	private Long userId;
	private Long activityId;
	private Date signedInTime;

	/* 扩展 */
	private UserAdminSimpleVo user;
	private String signedInTimeLabel;

}