package com.gc.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ProductListVo implements Serializable {
	/* 原生 */
	private Long id;
	private String title;

	/* 扩展 */
	private String price;
	private String marketPrice;
	private String image1Big;
	private String image1Thumbnail;

}