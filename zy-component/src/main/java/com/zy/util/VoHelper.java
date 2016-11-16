package com.zy.util;

import org.springframework.beans.BeanUtils;

import com.zy.entity.usr.User;
import com.zy.vo.UserAdminSimpleVo;
import com.zy.vo.UserListVo;
import com.zy.vo.UserSimpleVo;

public class VoHelper {

	public static UserAdminSimpleVo buildUserAdminSimpleVo(User user) {
		if (user == null) {
			return null;
		}
		UserAdminSimpleVo userAdminSimpleVo = new UserAdminSimpleVo();
		BeanUtils.copyProperties(user, userAdminSimpleVo);
		userAdminSimpleVo.setAvatarThumbnail(GcUtils.getThumbnail(user.getAvatar()));
		userAdminSimpleVo.setUserRankLabel(GcUtils.getUserRankLabel(user.getUserRank()));
		if (user.getUserType() == User.UserType.平台) {
			userAdminSimpleVo.setPhone("-");
		}
		return userAdminSimpleVo;
	}
	
	public static UserSimpleVo buildUserSimpleVo(User user) {
		if (user == null) {
			return null;
		}
		UserSimpleVo userSimpleVo = new UserSimpleVo();
		BeanUtils.copyProperties(user, userSimpleVo);
		userSimpleVo.setAvatarThumbnail(GcUtils.getThumbnail(user.getAvatar()));
		userSimpleVo.setUserRankLabel(GcUtils.getUserRankLabel(user.getUserRank()));
		return userSimpleVo;
	}

	public static UserListVo buildUserListVo(User user) {
		if (user == null) {
			return null;
		}
		UserListVo userListVo = new UserListVo();
		BeanUtils.copyProperties(user, userListVo);
		userListVo.setAvatarThumbnail(GcUtils.getThumbnail(user.getAvatar()));
		userListVo.setUserRankLabel(GcUtils.getUserRankLabel(user.getUserRank()));
		if (user.getUserType() == User.UserType.平台) {
			userListVo.setPhone("-");
		}
		return userListVo;
	}

}
