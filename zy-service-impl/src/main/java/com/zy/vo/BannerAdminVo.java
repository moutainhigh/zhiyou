package com.zy.vo;

import com.zy.entity.cms.Banner.BannerPosition;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BannerAdminVo implements Serializable {
	/* 原生 */
	private Long id;
	private Boolean isReleased;
	private String image;
	private String url;
	private String title;
	private Integer orderNumber;
	private BannerPosition bannerPosition;
	private Boolean isOpenBlank;

	/* 扩展 */
	private int width;
	private int height;
	private String imageThumbnail;

}