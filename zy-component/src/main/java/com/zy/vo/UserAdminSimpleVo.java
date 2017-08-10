package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.usr.User.UserType;
import com.zy.entity.usr.User.UserRank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserAdminSimpleVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "手机号")
	private String phone;
	@Field(label = "昵称")
	private String nickname;
	@Field(label = "用户类型")
	private UserType userType;
	@Field(label = "用户等级")
	private UserRank userRank;
	@Field(label = "总经理团队名称")
	private String bossName;
	@Field(label = "是否董事")
	private Boolean isDirector;
	@Field(label = "是否荣誉董事")
	private Boolean isHonorDirector;
	@Field(label = "是否股东")
	private Boolean isShareholder;

	/* 扩展 */
	@Field(label = "用户等级")
	private String userRankLabel;
	@Field(label = "头像")
	private String avatarThumbnail;

}