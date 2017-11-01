package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.usr.User.UserRank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class MergeUserAdminVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "用户id")
	private Long userId;
	@Field(label = "原始上级id")
	private Long inviterId;
	@Field(label = "上级id")
	private Long parentId;
	@Field(label = "用户等级")
	private UserRank userRank;
	@Field(label = "产品类型")
	private Integer productType;

	/* 扩展 */
	@Field(label = "用户id")
	private UserAdminSimpleVo user;
	@Field(label = "原始上级id")
	private UserAdminSimpleVo inviter;
	@Field(label = "上级id")
	private UserAdminSimpleVo parent;
	@Field(label = "用户等级")
	private String userRankLabel;

}