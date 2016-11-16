package com.zy.vo;

import java.io.Serializable;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSimpleVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "昵称")
	private String nickname;

	/* 扩展 */
	@Field(label = "用户等级")
	private String userRankLabel;
	@Field(label = "头像")
	private String avatarThumbnail;

}