package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.usr.UserInfo.Gender;
import com.zy.entity.act.Policy.PolicyStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PolicyExportVo implements Serializable {
	/* 原生 */
	@Field(label = "序号", order = 10)
	private Long id;
	@Field(label = "保险进度状态")
	private PolicyStatus policyStatus;
	@Field(label = "姓名", order = 20)
	private String realname;
	@Field(label = "性别", order = 40)
	private Gender gender;
	@Field(label = "手机号码", order = 60)
	private String phone;
	@Field(label = "证件号码", order = 30)
	private String idCardNumber;
	@Field(label = "图片1")
	private String image1;

	/* 扩展 */
	@Field(label = "出生年月", order = 50)
	private String birthdayLabel;

}