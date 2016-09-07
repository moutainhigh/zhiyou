package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.usr.User.UserType;
import com.zy.entity.usr.User.UserRank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class UserAdminVo implements Serializable {
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
	@Field(label = "qq")
	private String qq;
	@Field(label = "是否冻结")
	private Boolean isFrozen;
	@Field(label = "注册时间")
	private Date registerTime;
	@Field(label = "注册ip")
	private String registerIp;
	@Field(label = "信息是否已完善")
	private Boolean isInfoCompleted;
	@Field(label = "remark")
	private String remark;

	/* 扩展 */
	@Field(label = "头像",order = 999)
	private String avatarThumbnail;
	@Field(label = "邀请人id",order = 999)
	private UserAdminSimpleVo inviter;
	@Field(label = "上级id",order = 999)
	private UserAdminSimpleVo parent;

}