package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.cms.Banner.BannerPosition;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BannerAdminVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "是否发布")
	private Boolean isReleased;
	@Field(label = "banner图片")
	private String image;
	@Field(label = "url")
	private String url;
	@Field(label = "标题")
	private String title;
	@Field(label = "排序")
	private Integer orderNumber;
	@Field(label = "banner位置")
	private BannerPosition bannerPosition;
	@Field(label = "是否新窗口打开")
	private Boolean isOpenBlank;

	/* 扩展 */
	@Field(label = "width",order = 999)
	private int width;
	@Field(label = "height",order = 999)
	private int height;
	@Field(label = "banner图片",order = 999)
	private String imageThumbnail;

}