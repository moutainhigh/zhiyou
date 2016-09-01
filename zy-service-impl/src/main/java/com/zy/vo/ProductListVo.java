package com.gc.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ProductListVo implements Serializable {
	/* 原生 */

	/* 扩展 */
	private String price;
	private String marketPrice;
	private String imageThumbnail;

}