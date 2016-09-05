package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.sys.ConfirmStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AppearanceVo implements Serializable {
	/* 原生 */
	@Field(label = "实名认证id")
	private Long id;
	@Field(label = "用户id")
	private Long userId;
	@Field(label = "真实姓名")
	private String realname;
	@Field(label = "身份证号")
	private String idCardNumber;
	@Field(label = "图片1")
	private String image1;
	@Field(label = "图片2")
	private String image2;
	@Field(label = "审核状态")
	private ConfirmStatus confirmStatus;
	@Field(label = "审核备注")
	private String confirmRemark;

	/* 扩展 */
	@Field(label = "图片1")
	private String image1Thumbnail;
	@Field(label = "图片2")
	private String image2Thumbnail;

}