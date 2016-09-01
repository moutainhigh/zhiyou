package com.zy.vo;

import com.zy.entity.sys.ConfirmStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AppearanceVo implements Serializable {
	/* 原生 */
	private Long id;
	private Long userId;
	private String realname;
	private String idCardNumber;
	private String image1;
	private String image2;
	private ConfirmStatus confirmStatus;
	private String confirmRemark;

	/* 扩展 */
	private String image1Thumbnail;
	private String image2Thumbnail;

}