package com.zy.util;

import com.zy.entity.mergeusr.MergeUserView;
import com.zy.entity.sys.SystemCode;
import com.zy.service.SystemCodeService;
import com.zy.vo.MergeUserViewSimpleVo;
import org.springframework.beans.BeanUtils;

import com.zy.entity.usr.User;
import com.zy.vo.UserAdminSimpleVo;
import com.zy.vo.UserListVo;
import com.zy.vo.UserSimpleVo;
import org.springframework.beans.factory.annotation.Autowired;

public class VoHelper {



	public static MergeUserViewSimpleVo buildMergeUserViewSimpleVo(MergeUserView mergeUserView) {
		if (mergeUserView == null) {
			return null;
		}
		MergeUserViewSimpleVo mergeUserViewSimpleVo = new MergeUserViewSimpleVo();
		BeanUtils.copyProperties(mergeUserView, mergeUserViewSimpleVo);
		mergeUserViewSimpleVo.setAvatarThumbnail(GcUtils.getThumbnail(mergeUserView.getAvatar()));
		mergeUserViewSimpleVo.setUserRankLabel(GcUtils.getUserRankLabel(mergeUserView.getUserRank()));
		mergeUserViewSimpleVo.setLargeareaDirectorLabel(GcUtils.getLargeareaDirectorLabel(mergeUserView.getLargeareaDirector()));
		if (mergeUserView.getUserType() == User.UserType.平台) {
			mergeUserViewSimpleVo.setPhone("-");
		}
		return mergeUserViewSimpleVo;
	}

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
