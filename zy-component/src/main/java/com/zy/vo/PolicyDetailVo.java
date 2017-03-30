package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.usr.UserInfo.Gender;
import com.zy.entity.act.Policy.PolicyStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class PolicyDetailVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "保险进度状态")
	private PolicyStatus policyStatus;
	@Field(label = "检测报告id")
	private Long reportId;
	@Field(label = "用户id")
	private Long userId;
	@Field(label = "姓名")
	private String realname;
	@Field(label = "出生年月日")
	private Date birthday;
	@Field(label = "性别")
	private Gender gender;
	@Field(label = "手机号")
	private String phone;
	@Field(label = "编号")
	private String code;
	@Field(label = "身份证号")
	private String idCardNumber;
	@Field(label = "图片1")
	private String image1;

	/* 扩展 */
	@Field(label = "图片1")
	private String image1Thumbnail;

}