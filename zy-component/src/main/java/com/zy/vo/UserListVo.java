package com.zy.vo;

import com.zy.entity.usr.User.UserType;
import com.zy.entity.usr.User.UserRank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserListVo implements Serializable {
	/* 原生 */
	private Long id;
	private String phone;
	private String nickname;
	private UserType userType;
	private UserRank userRank;

	/* 扩展 */
	private String avatarThumbnail;

}