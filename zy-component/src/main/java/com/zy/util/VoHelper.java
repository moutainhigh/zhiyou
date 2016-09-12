package com.zy.util;

import com.zy.entity.usr.User;
import com.zy.vo.UserAdminSimpleVo;
import org.springframework.beans.BeanUtils;

public class VoHelper {

	public static UserAdminSimpleVo buildUserAdminSimpleVo(User user) {
		if (user == null) {
			return null;
		}
		UserAdminSimpleVo userAdminSimpleVo = new UserAdminSimpleVo();
		BeanUtils.copyProperties(user, userAdminSimpleVo);
		userAdminSimpleVo.setAvatarThumbnail(GcUtils.getThumbnail(user.getAvatar()));
		userAdminSimpleVo.setUserRankLabel(GcUtils.getUserRankLabel(user.getUserRank()));
		return userAdminSimpleVo;
	}

}
