package com.zy.vo;

import io.gd.generator.annotation.Field;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import com.zy.entity.act.Report.ReportResult;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.usr.UserInfo.Gender;

@Getter
@Setter
public class ReportExportVo implements Serializable {
	/* 原生 */
	@Field(label = "报告编号", order = 10)
	private Long id;
	@Field(label = "客户姓名", order = 20)
	private String realname;
	@Field(label = "年龄", order = 30)
	private Integer age;
	@Field(label = "性别", order = 40)
	private Gender gender;
	@Field(label = "手机号", order = 50)
	private String phone;
	@Field(label = "检测结果", order = 60)
	private ReportResult reportResult;
	@Field(label = "检测心得", order = 70)
	private String text;
	@Field(label = "检测次数")
	private Integer times;
	@Field(label = "初审状态")
	private ConfirmStatus preConfirmStatus;
	@Field(label = "初审通过时间")
	private Date preConfirmedTime;
	@Field(label = "审核状态")
	private ConfirmStatus confirmStatus;
	@Field(label = "审核备注")
	private String confirmRemark;
	@Field(label = "审核通过时间")
	private Date confirmedTime;

	/* 扩展 */
	@Field(label = "昵称", order = 15)
	private Long nickname;
	@Field(label = "所在地")
	private String province;
	@Field(label = "所在地")
	private String city;
	@Field(label = "所在地")
	private String district;
	@Field(label = "职业")
	private String jobName;
	@Field(label = "申请时间")
	private String appliedTimeLabel;
	@Field(label = "创建时间")
	private String createdTimeLabel;

}