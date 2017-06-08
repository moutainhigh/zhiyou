package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ActionReportExportVo implements Serializable {
	@Field(label = "序号" ,order =1)
	private Long id;
	@Field(label = "手机号" ,order =7)
	private String phone;
	@Field(label = "用户等级" ,order =8)
	private String userRankLable;
	@Field(label = "昵称" ,order =5)
	private String nickname;
	@Field(label = "真实姓名" ,order =6)
	private String realname;
	@Field(label = "邀请人姓名",order =9)
	private String parentName;
	@Field(label = "标题",order =2 )
	private String title;
	@Field(label = "开始时间",order =3)
	private String starDate;
	@Field(label = "详细地址" ,order =4)
	private String address;
}