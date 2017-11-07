package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class MergeV3ToV4AdminVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "用户明")
	private String name;
	@Field(label = "用户ID")
	private Long UserId;
	@Field(label = "使用标记位")
	private Integer flage;
	@Field(label = "创建人")
	private Long create_by;
	@Field(label = "升级时间")
	private Date create_date;
	@Field(label = "创建人")
	private Long update_by;
	@Field(label = "更新时间")
	private Date update_date;
	@Field(label = "图片")
	private String image1;
	@Field(label = "图片")
	private String image2;
	@Field(label = "删除标记为")
	private Integer delFlage;

	/* 扩展 */
	@Field(label = "升级时间")
	private String createDateLabel;
	@Field(label = "更新时间")
	private String updateDateLabel;

}