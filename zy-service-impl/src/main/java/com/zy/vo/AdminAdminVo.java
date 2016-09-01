package com.zy.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AdminAdminVo implements Serializable {
	/* 原生 */
	private Long id;
	private Long userId;
	private String roleNames;
	private Long siteId;

	/* 扩展 */
	private UserAdminSimpleVo user;

}