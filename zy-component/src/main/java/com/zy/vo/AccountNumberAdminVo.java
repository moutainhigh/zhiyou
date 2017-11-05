package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class AccountNumberAdminVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "原来的账号手机号")
	private String oldPhone;
	@Field(label = "原来的账号人姓名")
	private String oldName;
	@Field(label = "新绑定的账号手机号")
	private String newPhone;
	@Field(label = "是否可以在此使用")
	private String flage;
	@Field(label = "创建人")
	private Long createBy;
	@Field(label = "创建时间")
	private Date createDate;
	@Field(label = "更新人")
	private Long updateBy;
	@Field(label = "更新时间")
	private Date updateDate;

	/* 扩展 */

	private String createName;

	private String createDateLable;

	private String updateDateLable;

}