package com.zy.vo;

import com.zy.entity.usr.Portrait.Gender;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class PortraitVo implements Serializable {
	/* 原生 */
	private Long id;
	private Long userId;
	private Gender gender;
	private Date birthday;

	/* 扩展 */
	private String job;
	private String province;
	private String city;
	private String district;

}