package com.zy.vo;

import com.zy.entity.sys.ConfirmStatus;
import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class ReportVisitedLogListVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "检测报告id")
	private Long reportId;
	@Field(label = "被访者与检测人关系")
	private String relationship;
	@Field(label = "relationshipText")
	private String relationshipText;
	@Field(label = "一访客服")
	private String customerServiceName1;
	@Field(label = "一访日期")
	private Date visitedTime1;
	@Field(label = "一访状态")
	private String visitedStatus1;
	@Field(label = "二访客服")
	private String customerServiceName2;
	@Field(label = "二访日期")
	private Date visitedTime2;
	@Field(label = "二访状态")
	private String visitedStatus2;
	@Field(label = "三访客服")
	private String customerServiceName3;
	@Field(label = "三访日期")
	private Date visitedTime3;
	@Field(label = "三访状态")
	private String visitedStatus3;
	@Field(label = "最终回访结果")
	private String visitedStatus;
	@Field(label = "终审")
	private ConfirmStatus confirmStatus;
	@Field(label = "作息时间")
	private String restTimeLabel;
	@Field(label = "作息时间(其他)")
	private String restTimeText;
	@Field(label = "睡眠质量")
	private String sleepQuality;
	@Field(label = "睡眠质量(其他)")
	private String sleepQualityText;
	@Field(label = "饮酒")
	private String drink;
	@Field(label = "抽烟")
	private String smoke;
	@Field(label = "锻炼身体")
	private String exercise;
	@Field(label = "锻炼身体(是/其他)")
	private String exerciseText;
	@Field(label = "兴趣爱好")
	private String hobby;
	@Field(label = "兴趣爱好(有)")
	private String hobbyText;
	@Field(label = "检测原因")
	private String cause;
	@Field(label = "检测原因(其他)")
	private String causeText;
	@Field(label = "健康状况")
	private String health;
	@Field(label = "当前病状")
	private String sickness;
	@Field(label = "家族遗传史")
	private String familyHistory;
	@Field(label = "是否服用保健品")
	private String healthProduct;
	@Field(label = "是否服用保健品(是)")
	private String healthProductText;
	@Field(label = "购买保健品月均消费")
	private String monthlyCost;
	@Field(label = "购买保健品月均消费(其他)")
	private String monthlyCostText;
	@Field(label = "产品名称")
	private String productName;
	@Field(label = "干扰因素")
	private String interferingFactors;
	@Field(label = "回访记录")
	private String visitedInfo;
	@Field(label = "产品分享")
	private String productSharing;
	@Field(label = "产品分享(其他)")
	private String productSharingText;
	@Field(label = "客转代意向客户")
	private String toAgent;
	@Field(label = "沟通方式")
	private String contactWay;
	@Field(label = "沟通方式(微信号/其他)")
	private String contactWayText;

	/* 扩展 */
	@Field(label = "检测报告用户昵称")
	private String reportRealname;
	@Field(label = "检测报告用户手机")
	private String reportPhone;
	@Field(label = "一访日期")
	private String visitedTime1Label;
	@Field(label = "二访日期")
	private String visitedTime2Label;
	@Field(label = "三访日期")
	private String visitedTime3Label;

}