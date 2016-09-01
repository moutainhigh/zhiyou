package com.gc.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ProductAdminVo implements Serializable {
	/* 原生 */
	private String detail;
	private String skuCode;

	/* 扩展 */
	private String price;
	private String marketPrice;
	private String stockQuantity;
	private String imageThumbnail;

}