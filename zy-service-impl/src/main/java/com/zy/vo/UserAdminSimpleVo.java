package com.gc.vo;

import com.gc.entity.usr.User.UserType;
import com.gc.entity.usr.User.UserRank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserAdminSimpleVo implements Serializable {
	/* 原生 */
	private Long id;
	private String phone;
	private String nickname;
	private UserType userType;
	private UserRank userRank;

	/* 扩展 */
	private String avatarThumbnail;

}