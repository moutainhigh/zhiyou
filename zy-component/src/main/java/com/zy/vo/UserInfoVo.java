package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.usr.UserInfo.Gender;
import com.zy.entity.sys.ConfirmStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
public class UserInfoVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "用户id")
	private Long userId;
	@Field(label = "真实姓名")
	private String realname;
	@Field(label = "身份证号")
	private String idCardNumber;
	@Field(label = "图片1")
	private String image1;
	@Field(label = "图片2")
	private String image2;
	@Field(label = "审核状态")
	private ConfirmStatus confirmStatus;
	@Field(label = "审核备注")
	private String confirmRemark;
	@Field(label = "性别")
	private Gender gender;
	@Field(label = "职业")
	private Long jobId;
	@Field(label = "所在地")
	private Long areaId;
	@Field(label = "标签")
	private String tagIds;

	/* 扩展 */
	@Field(label = "身份证号")
	private String idCardNumberLabel;
	@Field(label = "图片1")
	private String image1Thumbnail;
	@Field(label = "图片2")
	private String image2Thumbnail;
	@Field(label = "申请时间")
	private String appliedTimeLabel;
	@Field(label = "审核通过时间")
	private String confirmedTimeLabel;
	@Field(label = "生日")
	private String birthdayLabel;
	@Field(label = "职业")
	private String jobName;
	@Field(label = "所在地")
	private String province;
	@Field(label = "所在地")
	private String city;
	@Field(label = "所在地")
	private String district;
	@Field(label = "标签")
	private List<String> tagNames = new ArrayList<>();
	@Field(label = "推荐人name")
	private String pName;
	@Field(label = "手机号")
	private String phone;
	@Field(label = "推荐人手机号")
	private String pPhone;


}