package com.zy.entity.act;

import com.zy.entity.sys.ConfirmStatus;
import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.annotation.view.View;
import io.gd.generator.annotation.view.ViewObject;
import io.gd.generator.api.query.Predicate;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "act_report_visited_log")
@Getter
@Setter
@Type(label = "回访")
@QueryModel
@ViewObject(groups = {ReportVisitedLog.VO_LIST, ReportVisitedLog.VO_ADMIN})
public class ReportVisitedLog implements Serializable {

	public static final String VO_ADMIN = "ReportVisitedLogAdminVo";
	public static final String VO_LIST = "ReportVisitedLogListVo";
	
	@Id
	@Field(label = "id")
	@View(groups = {VO_LIST, VO_ADMIN})
	@Query({Predicate.EQ, Predicate.IN})
	private Long id;

	@NotNull
	@Field(label = "检测报告id")
	@Query({Predicate.EQ, Predicate.IN})
	@View(groups = {VO_LIST, VO_ADMIN})
	private Long reportId;

	/** 外拨记录 **/
	@Field(label = "被访者与检测人关系")
	@Query({Predicate.LK})
	@View(groups = {VO_LIST, VO_ADMIN})
	private String relationship;
	@View(groups = {VO_LIST, VO_ADMIN})
	private String relationshipText;

	@Query({Predicate.LK})
	@Field(label = "一访客服")
	@View(groups = {VO_LIST, VO_ADMIN})
	private String customerServiceName1;

	@Field(label = "一访日期")
	@View(groups = {VO_LIST, VO_ADMIN})
	@Query({Predicate.GTE, Predicate.LT})
	@View(name = "visitedTime1Label", type = String.class, groups = {VO_LIST, VO_ADMIN})
	private Date visitedTime1;

	@Field(label = "一访状态")
	@Query({Predicate.EQ, Predicate.LK})
	@View(groups = {VO_LIST, VO_ADMIN})
	private String  visitedStatus1;

	@Field(label = "二访客服")
	@Query({Predicate.LK})
	@View(groups = {VO_LIST, VO_ADMIN})
	private String customerServiceName2;

	@Field(label = "二访日期")
	@View(groups = {VO_LIST, VO_ADMIN})
	@Query({Predicate.GTE, Predicate.LT})
	@View(name = "visitedTime2Label", type = String.class, groups = {VO_LIST, VO_ADMIN})
	private Date visitedTime2;

	@Field(label = "二访状态")
	@Query({Predicate.EQ, Predicate.LK})
	@View(groups = {VO_LIST, VO_ADMIN})
	private String  visitedStatus2;

	@Field(label = "三访客服")
	@Query({Predicate.LK})
	@View(groups = {VO_LIST, VO_ADMIN})
	private String customerServiceName3;

	@Field(label = "三访日期")
	@View(groups = {VO_LIST, VO_ADMIN})
	@Query({Predicate.GTE, Predicate.LT})
	@View(name = "visitedTime3Label", type = String.class, groups = {VO_LIST, VO_ADMIN})
	private Date visitedTime3;

	@Field(label = "三访状态")
	@Query({Predicate.EQ, Predicate.LK})
	@View(groups = {VO_LIST, VO_ADMIN})
	private String  visitedStatus3;

	@Field(label = "最终回访结果")
	@Query({Predicate.EQ, Predicate.LK})
	@View(groups = {VO_LIST, VO_ADMIN})
	private String visitedStatus;

	@Query(Predicate.EQ)
	@Field(label = "终审")
	@View(groups = {VO_LIST, VO_ADMIN})
	private ConfirmStatus confirmStatus;

	@Field(label = "审批备注")
	@View(groups = {VO_ADMIN})
	private String remark;

	/** 生活习惯 **/
	@Query({Predicate.GTE, Predicate.LT})
	@Field(label = "作息时间", description = "暂时只有晚上休息时间")
	@View(groups = {VO_LIST, VO_ADMIN})
	private String restTimeLabel;
	@Field(label = "作息时间(其他)")
	@View(groups = {VO_LIST, VO_ADMIN})
	private String restTimeText;

	@Field(label = "睡眠质量")
	@View(groups = {VO_LIST, VO_ADMIN})
	private String sleepQuality;
	@Field(label = "睡眠质量(其他)")
	@View(groups = {VO_LIST, VO_ADMIN})
	private String sleepQualityText;

	@Field(label = "饮酒")
	@View(groups = {VO_LIST, VO_ADMIN})
	private String drink;

	@Query({Predicate.EQ})
	@Field(label = "抽烟")
	@View(groups = {VO_LIST, VO_ADMIN})
	private String smoke;

	@Query({Predicate.EQ})
	@Field(label = "锻炼身体")
	@View(groups = {VO_LIST, VO_ADMIN})
	private String exercise;
	@Field(label = "锻炼身体(是/其他)")
	@View(groups = {VO_LIST, VO_ADMIN})
	private String exerciseText;

	@Field(label = "兴趣爱好")
	@View(groups = {VO_LIST, VO_ADMIN})
	private String hobby;
	@Field(label = "兴趣爱好(有)")
	@View(groups = {VO_LIST, VO_ADMIN})
	private String hobbyText;

	@Field(label = "生活习惯备注")
	@View(groups = {VO_ADMIN})
	private String remark1;

	/* 健康状况 */
	@Field(label = "检测原因")
	@View(groups = {VO_LIST, VO_ADMIN})
	private String cause;
	@Field(label = "检测原因(其他)")
	@View(groups = {VO_LIST, VO_ADMIN})
	private String causeText;

	@Field(label = "健康状况")
	@View(groups = {VO_LIST, VO_ADMIN})
	private String health;

	@Field(label = "当前病状")
	@View(groups = {VO_LIST, VO_ADMIN})
	private String sickness;

	@Field(label = "家族遗传史")
	@View(groups = {VO_LIST, VO_ADMIN})
	private String familyHistory;

	@Field(label = "健康状况备注")
	@View(groups = {VO_ADMIN})
	private String remark2;

	/* 保健品 */
	@Field(label = "是否服用保健品")
	@View(groups = {VO_LIST, VO_ADMIN})
	private String healthProduct;
	@Field(label = "是否服用保健品(是)")
	@View(groups = {VO_LIST, VO_ADMIN})
	private String healthProductText;

	@Field(label = "购买保健品月均消费")
	@View(groups = {VO_LIST, VO_ADMIN})
	private String monthlyCost;
	@Field(label = "购买保健品月均消费(其他)")
	@View(groups = {VO_LIST, VO_ADMIN})
	private String monthlyCostText;

	@Field(label = "产品名称")
	@View(groups = {VO_LIST, VO_ADMIN})
	private String productName;

	/* 检测干扰因素 */
	@Field(label = "干扰因素")
	@View(groups = {VO_LIST, VO_ADMIN})
	private String interferingFactors;

	@Field(label = "备注")
	@View(groups = {VO_ADMIN})
	private String remark3;

	@Field(label = "回访记录")
	@View(groups = {VO_LIST, VO_ADMIN})
	private String visitedInfo;

	@Field(label = "产品分享")
	@View(groups = {VO_LIST, VO_ADMIN})
	private String productSharing;
	@Field(label = "产品分享(其他)")
	@View(groups = {VO_LIST, VO_ADMIN})
	private String productSharingText;

	@Field(label = "客转代意向客户")
	@View(groups = {VO_LIST, VO_ADMIN})
	private String toAgent;

	@Field(label = "沟通方式")
	@View(groups = {VO_LIST, VO_ADMIN})
	private String contactWay;
	@Field(label = "沟通方式(微信号/其他)")
	@View(groups = {VO_LIST, VO_ADMIN})
	private String contactWayText;

}
