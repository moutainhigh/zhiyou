package com.zy.vo;

import com.zy.entity.usr.User.UserRank;
import com.zy.entity.usr.User.UserType;
import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class MergeUserViewSimpleVo implements Serializable {
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
	@Field(label = "是否董事")
	private Boolean isDirector;
	@Field(label = "是否荣誉董事")
	private Boolean isHonorDirector;
	@Field(label = "是否股东")
	private Boolean isShareholder;
	@Field(label = "所属大区")
	private Integer largearea;
	@Field(label = "是否大区总裁")
	private Boolean isPresident;
	@Field(label = "1：大区董事长  2：大区副董事长")
	private Integer largeareaDirector;

	/* 扩展 */
	@Field(label = "用户等级")
	private String userRankLabel;
	@Field(label = "头像")
	private String avatarThumbnail;
	@Field(label = "用户大区")
	private String largeareaLabel;
	@Field(label = "大区董事长")
	private String largeareaDirectorLabel;

}