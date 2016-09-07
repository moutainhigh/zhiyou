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
public class ReportVo implements Serializable {
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
	@Field(label = "一审状态")
	private ConfirmStatus preConfirmStatus;
	@Field(label = "审核状态")
	private ConfirmStatus confirmStatus;
	@Field(label = "审核备注")
	private String confirmRemark;
	@Field(label = "申请时间")
	private Date appliedTime;
	@Field(label = "审核通过时间")
	private Date confirmedTime;

	/* 扩展 */
	@Field(label = "检测时间",order = 999)
	private String dateLabel;
	@Field(label = "图片1",order = 999)
	private String image1Big;
	@Field(label = "图片1",order = 999)
	private String image1Thumbnail;
	@Field(label = "图片2",order = 999)
	private String image2Big;
	@Field(label = "图片2",order = 999)
	private String image2Thumbnail;
	@Field(label = "图片3",order = 999)
	private String image3Big;
	@Field(label = "图片3",order = 999)
	private String image3Thumbnail;
	@Field(label = "图片4",order = 999)
	private String image4Big;
	@Field(label = "图片4",order = 999)
	private String image4Thumbnail;
	@Field(label = "图片5",order = 999)
	private String image5Big;
	@Field(label = "图片5",order = 999)
	private String image5Thumbnail;
	@Field(label = "图片6",order = 999)
	private String image6Big;
	@Field(label = "图片6",order = 999)
	private String image6Thumbnail;

}