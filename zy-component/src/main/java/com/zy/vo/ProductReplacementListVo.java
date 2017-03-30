package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.mal.ProductReplacement.ProductReplacementStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ProductReplacementListVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "服务商")
	private Long userId;
	@Field(label = "更换状态")
	private ProductReplacementStatus productReplacementStatus;
	@Field(label = "被更换产品")
	private String fromProduct;
	@Field(label = "更换产品")
	private String toProduct;
	@Field(label = "数量")
	private Long quantity;

	/* 扩展 */
	@Field(label = "更换时间")
	private String createdTimeLabel;

}