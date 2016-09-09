package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.usr.User.UserRank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class UserUpgradeAdminVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "用户")
	private Long userId;
	@Field(label = "升级前等级")
	private UserRank fromUserRank;
	@Field(label = "升级后等级")
	private UserRank toUserRank;
	@Field(label = "升级时间")
	private Date upgradedTime;

	/* 扩展 */

}