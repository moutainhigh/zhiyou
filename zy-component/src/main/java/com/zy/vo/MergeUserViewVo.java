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
public class MergeUserViewVo implements Serializable {
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
	@Field(label = "是否荣誉董事")
	private Boolean isHonorDirector;
	@Field(label = "是否股东")
	private Boolean isShareholder;
	@Field(label = "是否删除")
	private Boolean isDeleted;
	@Field(label = "是否直升特级")
	private Boolean isToV4;
	@Field(label = "最后一次登录时间")
	private Date lastloginTime;
	@Field(label = "所属大区")
	private Integer largearea;
	@Field(label = "设置大区备注")
	private String setlargearearemark;
	@Field(label = "是否大区总裁")
	private Boolean isPresident;
	@Field(label = "所属大区总裁id")
	private Long presidentId;
	@Field(label = "1：大区董事长  2：大区副董事长")
	private Integer largeareaDirector;
	@Field(label = "直属v4Id")
	private Long v4Id;

	/* 扩展 */
	@Field(label = "用户等级")
	private String userRankLabel;
	@Field(label = "头像")
	private String avatarThumbnail;
	@Field(label = "原始优检体系的parent")
	private MergeUserViewSimpleVo inviter;
	@Field(label = "推荐人id")
	private MergeUserViewSimpleVo parent;
	@Field(label = "大区总裁")
	private MergeUserViewSimpleVo president;
	@Field(label = "上级总经理id")
	private MergeUserViewSimpleVo boss;
	@Field(label = "直属v4Id")
	private MergeUserViewSimpleVo v4Parent;
	@Field(label = "大区董事长")
	private String largeareaDirectorLabel;
	@Field(label = "大区董事长")
	private String largeareaLabel;

}