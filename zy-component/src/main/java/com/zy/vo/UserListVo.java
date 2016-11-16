package com.zy.vo;

import java.io.Serializable;

import com.zy.entity.usr.User.UserRank;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserListVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "手机号")
	private String phone;
	@Field(label = "昵称")
	private String nickname;
	@Field(label = "用户等级")
	private UserRank userRank;

	/* 扩展 */
	@Field(label = "用户等级")
	private String userRankLabel;
	@Field(label = "头像")
	private String avatarThumbnail;

}