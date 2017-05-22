package com.zy.vo;

import com.zy.entity.act.ActivityTeamApply;
import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class ActivityTeamApplyListVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "活动团队报名单支付状态")
	private ActivityTeamApply.PaidStatus paidStatus;
	@Field(label = "活动id")
	private Long activityId;
	@Field(label = "数量")
	private Long count;
	@Field(label = "总金额")
	private BigDecimal amount;
	@Field(label = "创建时间")
	private Date createTime;
	@Field(label = "支付时间")
	private Date paidTime;

	/* 扩展 */
	@Field(label = "总金额")
	private String amountLabel;
	@Field(label = "活动id")
	private ActivityListVo activity;
	@Field(label = "创建时间")
	private String createTimeLabel;
	@Field(label = "支付时间")
	private String paidTimeLabel;

}