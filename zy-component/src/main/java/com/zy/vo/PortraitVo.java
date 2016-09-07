package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.usr.Portrait.Gender;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;

@Getter
@Setter
public class PortraitVo implements Serializable {
	/* 原生 */
	@Field(label = "用户id")
	private Long userId;
	@Field(label = "性别")
	private Gender gender;
	@Field(label = "生日")
	private Date birthday;
	@Field(label = "职业")
	private Long jobId;
	@Field(label = "所在地")
	private Long areaId;
	@Field(label = "标签")
	private String tagIds;

	/* 扩展 */
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
	@Field(label = "生日")
	private String birthdayLabel;

}