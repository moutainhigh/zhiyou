package com.zy.vo;

import io.gd.generator.annotation.Field;

import com.zy.entity.usr.UserInfo.Gender;
import com.zy.entity.act.Report.ReportResult;
import com.zy.entity.sys.ConfirmStatus;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

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
	@Field(label = "图片链接", order = 71)
	private String image;
	@Field(label = "检测次数", order = 59)
	private Integer times;
	@Field(label = "初审状态", order = 77)
	private ConfirmStatus preConfirmStatus;
	@Field(label = "审核状态", order = 79)
	private ConfirmStatus confirmStatus;
	@Field(label = "审核备注", order = 80)
	private String confirmRemark;
	@Field(label = "审核通过时间")
	private Date confirmedTime;

	/* 扩展 */
	@Field(label = "昵称", order = 15)
	private String userNickname;
	@Field(label = "手机", order = 15)
	private String userPhone;
	@Field(label = "职业", order = 55)
	private String jobName;
	@Field(label = "所在省", order = 56)
	private String province;
	@Field(label = "所在市", order = 57)
	private String city;
	@Field(label = "所在区", order = 58)
	private String district;
	@Field(label = "检测日期", order = 74)
	private String reportedDateLabel;
	@Field(label = "申请时间", order = 75)
	private String appliedTimeLabel;
	@Field(label = "创建时间", order = 76)
	private String createdTimeLabel;
	@Field(label = "初审通过时间", order = 78)
	private String preConfirmedTimeLabel;
	@Field(label = "初审通过时间", order = 90)
	private String confirmedTimeLabel;

}