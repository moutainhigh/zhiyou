package com.gc.vo;

import com.gc.entity.sys.ConfirmStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class AppearanceAdminVo implements Serializable {
	/* 原生 */
	private Long id;
	private Long userId;
	private String realname;
	private String idCardNumber;
	private String image1;
	private String image2;
	private ConfirmStatus confirmStatus;
	private String confirmRemark;
	private Date appliedTime;
	private Date confirmedTime;

	/* 扩展 */
	private UserAdminSimpleVo user;
	private String image1Thumbnail;
	private String image2Thumbnail;

}