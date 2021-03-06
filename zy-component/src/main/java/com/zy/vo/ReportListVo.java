package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.usr.UserInfo.Gender;
import com.zy.entity.act.Report.ReportResult;
import com.zy.entity.sys.ConfirmStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ReportListVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "用户id")
	private Long userId;
	@Field(label = "姓名")
	private String realname;
	@Field(label = "年龄")
	private Integer age;
	@Field(label = "性别")
	private Gender gender;
	@Field(label = "手机号")
	private String phone;
	@Field(label = "产品")
	private Long productId;
	@Field(label = "检测结果")
	private ReportResult reportResult;
	@Field(label = "检测次数")
	private Integer times;
	@Field(label = "审核状态")
	private ConfirmStatus confirmStatus;

	/* 扩展 */
	@Field(label = "职业")
	private String jobName;
	@Field(label = "所在地")
	private String province;
	@Field(label = "所在地")
	private String city;
	@Field(label = "所在地")
	private String district;
	@Field(label = "产品")
	private String productTitle;
	@Field(label = "检测日期")
	private String reportedDateLabel;
	@Field(label = "申请时间")
	private String appliedTimeLabel;
	@Field(label = "创建时间")
	private String createdTimeLabel;
	@Field(label = "旅游申请")
	private String tourFlage;
	@Field(label = "保险申请")
	private String insureFlage;

}