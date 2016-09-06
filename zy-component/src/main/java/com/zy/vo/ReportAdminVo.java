package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.act.Report.ReportResult;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.usr.Portrait.Gender;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class ReportAdminVo implements Serializable {
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
	@Field(label = "检测结果")
	private ReportResult reportResult;
	@Field(label = "文字")
	private String text;
	@Field(label = "创建时间")
	private Date createdTime;
	@Field(label = "审核状态")
	private ConfirmStatus confirmStatus;
	@Field(label = "审核备注")
	private String confirmRemark;
	@Field(label = "申请时间")
	private Date appliedTime;
	@Field(label = "审核通过时间")
	private Date confirmedTime;
	@Field(label = "是否已结算")
	private Boolean isSettledUp;

	/* 扩展 */
	@Field(label = "检测时间")
	private String dateLabel;
	@Field(label = "图片1")
	private String image1Big;
	@Field(label = "图片1")
	private String image1Thumbnail;
	@Field(label = "图片2")
	private String image2Big;
	@Field(label = "图片2")
	private String image2Thumbnail;
	@Field(label = "图片3")
	private String image3Big;
	@Field(label = "图片3")
	private String image3Thumbnail;
	@Field(label = "图片4")
	private String image4Big;
	@Field(label = "图片4")
	private String image4Thumbnail;
	@Field(label = "图片5")
	private String image5Big;
	@Field(label = "图片5")
	private String image5Thumbnail;
	@Field(label = "图片6")
	private String image6Big;
	@Field(label = "图片6")
	private String image6Thumbnail;

}