package com.zy.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserSimpleVo implements Serializable {
	/* 原生 */
	private Long id;
	private String nickname;

	/* 扩展 */
	private String avatarThumbnail;

}