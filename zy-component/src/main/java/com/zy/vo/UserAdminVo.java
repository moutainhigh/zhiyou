package com.gc.vo;

import com.gc.entity.usr.User.UserType;
import com.gc.entity.usr.User.UserRank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class UserAdminVo implements Serializable {
	/* 原生 */
	private Long id;
	private String phone;
	private String nickname;
	private UserType userType;
	private UserRank userRank;
	private String qq;
	private Boolean isFrozen;
	private Date registerTime;
	private String registerIp;
	private Boolean isInfoCompleted;
	private String remark;

	/* 扩展 */
	private String avatarThumbnail;
	private UserAdminSimpleVo inviter;

}