package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class SystemCodeAdminVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "系统类型")
	private String systemType;
	@Field(label = "名称")
	private String systemName;
	@Field(label = "值")
	private String systemValue;
	@Field(label = "描述")
	private String systemDesc;
	@Field(label = "是否删除")
	private Integer systemFlag;
	@Field(label = "创建时间")
	private Date createDate;
	@Field(label = "创建人id")
	private Long createBy;
	@Field(label = "修改时间")
	private Date updateDate;
	@Field(label = "修改人id")
	private Long updateBy;

	/* 扩展 */
	@Field(label = "创建人id")
	private UserAdminSimpleVo createUser;
	@Field(label = "修改人id")
	private UserAdminSimpleVo updateUser;
	@Field(label = "创建时间")
	private String createTimeLabel;
	@Field(label = "修改时间")
	private String updateTimeLabel;

}