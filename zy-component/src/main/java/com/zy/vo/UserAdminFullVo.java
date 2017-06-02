package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.usr.User.UserType;
import com.zy.entity.usr.User.UserRank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;

@Getter
@Setter
public class UserAdminFullVo implements Serializable {
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
	@Field(label = "remark")
	private String remark;
	@Field(label = "是否子系统")
	private Boolean isRoot;
	@Field(label = "子系统名称")
	private String rootName;
	@Field(label = "是否总经理")
	private Boolean isBoss;
	@Field(label = "总经理团队名称")
	private String bossName;
	@Field(label = "上级总经理id")
	private Long bossId;
	@Field(label = "是否董事")
	private Boolean isDirector;
	@Field(label = "是否股东")
	private Boolean isShareholder;

	/* 扩展 */
	@Field(label = "userInfo")
	private UserInfoAdminVo userInfo;
	@Field(label = "用户等级")
	private String userRankLabel;
	@Field(label = "头像")
	private String avatarThumbnail;
	@Field(label = "邀请人id")
	private UserAdminSimpleVo inviter;
	@Field(label = "推荐人id")
	private UserAdminSimpleVo parent;
	@Field(label = "上级总经理id")
	private UserAdminSimpleVo boss;
	@Field(label = "userUpgrades")
	private List<UserUpgradeAdminVo> userUpgrades = new ArrayList<>();
	@Field(label = "teammates")
	private List<UserAdminSimpleVo> teammates = new ArrayList<>();

}