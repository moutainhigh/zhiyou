package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class ActivityCollectAdminVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "用户id")
	private Long userId;
	@Field(label = "活动id")
	private Long activityId;
	@Field(label = "关注时间")
	private Date collectedTime;

	/* 扩展 */
	@Field(label = "用户id",order = 999)
	private UserAdminSimpleVo user;
	@Field(label = "关注时间",order = 999)
	private String collectedTimeLabel;

}