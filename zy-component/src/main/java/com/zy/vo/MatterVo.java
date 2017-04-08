package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class MatterVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "标题")
	private String title;
	@Field(label = "简述")
	private String description;
	@Field(label = "文件类型")
	private Integer type;
	@Field(label = "文件路径")
	private String url;
	@Field(label = "权限")
	private Integer authority;
	@Field(label = "上传时间")
	private String uploadTime;
	@Field(label = "上传用户Id")
	private Integer uploadUserId;
	@Field(label = "状态")
	private Integer status;
	@Field(label = "点击量")
	private Integer clickedCount;
	@Field(label = "关注量")
	private Integer collectedCount;
	@Field(label = "下载量")
	private Integer downloadCount;

	/* 扩展 */
	@Field(label = "作者")
	private String author;
	@Field(label = "是否关注")
	private Boolean isCollected;

}