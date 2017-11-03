package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.usr.User.UserRank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

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
	@Field(label = "注册时间")
	private Date registerTime;
	@Field(label = "是否冻结")
	private Boolean isFrozen;
	@Field(label = "是否董事")
	private Boolean isDirector;
	@Field(label = "是否荣誉董事")
	private Boolean isHonorDirector;
	@Field(label = "是否删除")
	private Boolean isDeleted;
	@Field(label = "是否直升特级")
	private Boolean isToV4;
	@Field(label = "直属v4Id")
	private Long v4Id;

	/* 扩展 */
	@Field(label = "用户id")
	private UserAdminSimpleVo user;
	@Field(label = "原始上级id")
	private UserAdminSimpleVo inviter;
	@Field(label = "上级id")
	private UserAdminSimpleVo parent;
	@Field(label = "用户等级")
	private String userRankLabel;
	@Field(label = "直属v4Id")
	private UserAdminSimpleVo v4Parent;

}