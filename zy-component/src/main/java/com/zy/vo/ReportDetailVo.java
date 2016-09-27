package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.usr.UserInfo.Gender;
import com.zy.entity.act.Report.ReportResult;
import com.zy.entity.sys.ConfirmStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;

@Getter
@Setter
public class ReportDetailVo implements Serializable {
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
	@Field(label = "职业")
	private Long jobId;
	@Field(label = "所在地")
	private Long areaId;
	@Field(label = "标签")
	private String tagIds;
	@Field(label = "检测结果")
	private ReportResult reportResult;
	@Field(label = "文字")
	private String text;
	@Field(label = "图片")
	private String image;
	@Field(label = "检测次数")
	private Integer times;
	@Field(label = "申请时间")
	private Date appliedTime;
	@Field(label = "初审状态")
	private ConfirmStatus preConfirmStatus;
	@Field(label = "审核状态")
	private ConfirmStatus confirmStatus;
	@Field(label = "审核备注")
	private String confirmRemark;
	@Field(label = "审核通过时间")
	private Date confirmedTime;
	@Field(label = "是否已结算")
	private Boolean isSettledUp;

	/* 扩展 */
	@Field(label = "职业")
	private String jobName;
	@Field(label = "所在地")
	private String province;
	@Field(label = "所在地")
	private String city;
	@Field(label = "所在地")
	private String district;
	@Field(label = "申请时间")
	private String appliedTimeLabel;
	@Field(label = "创建时间")
	private String createdTimeLabel;
	@Field(label = "审核通过时间")
	private String confirmedTimeLabel;
	@Field(label = "标签")
	private List<String> tagNames = new ArrayList<>();
	@Field(label = "图片")
	private List<String> images = new ArrayList<>();
	@Field(label = "图片")
	private List<String> imageThumbnails = new ArrayList<>();
	@Field(label = "图片")
	private List<String> imageBigs = new ArrayList<>();

}