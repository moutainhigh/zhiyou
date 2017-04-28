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
public class UserReportVo implements Serializable {
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
	@Field(label = "是否冻结")
	private Boolean isFrozen;
	@Field(label = "注册时间")
	private Date registerTime;
	@Field(label = "注册ip")
	private String registerIp;
	@Field(label = "上级id")
	private Long parentId;
	@Field(label = "是否子系统")
	private Boolean isRoot;
	@Field(label = "子系统名称")
	private String rootName;
	@Field(label = "是否总经理")
	private Boolean isBoss;
	@Field(label = "上级总经理id")
	private Long bossId;
	@Field(label = "是否董事")
	private Boolean isDirector;
	@Field(label = "是否股东")
	private Boolean isShareholder;

	/* 扩展 */
	@Field(label = "provinceId")
	private Long provinceId;
	@Field(label = "cityId")
	private Long cityId;
	@Field(label = "districtId")
	private Long districtId;
	@Field(label = "v4UserId")
	private Long v4UserId;
	@Field(label = "v4UserNickname")
	private String v4UserNickname;
	@Field(label = "rootId")
	private Long rootId;
	@Field(label = "rootRootName")
	private String rootRootName;

}