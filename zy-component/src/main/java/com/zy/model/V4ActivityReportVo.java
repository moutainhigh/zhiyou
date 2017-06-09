package com.zy.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class V4ActivityReportVo {

	private Integer id;  //序号

	private Long bossId;  //总经理id

	private String bossName;  //总经理团队名称

	private Long v4Count;  //团队特级总人数

	private Long activityCount;  //活跃人数  连续3个自然月有订货

	private Long inactiveCount;  //不活跃数

	private Long recentCount;  //新增特级人数

	private String activityRateLabel;  //当前活跃度 3个连续自然月活跃人数/团队特级总人数
	private double activityRate;  //当前活跃度 3个连续自然月活跃人数/团队特级总人数

	private String lastActivityRateLabel;  //上期活跃度 3个连续自然月活跃人数/团队特级总人数
	private double lastActivityRate;  //上期活跃度 3个连续自然月活跃人数/团队特级总人数

	private Integer activityRateOrderNumber;  //当前活跃度排名 排名重复进入同一排名

	private Integer lastActivityRateOrderNumber;  //上期活跃度排名

	private String growth;  //增长情况 ↑/↓/—
}
