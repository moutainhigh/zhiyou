package com.zy.component;

import com.zy.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.usr.User;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;

@Component
public class UserComponent {
	
	@Autowired
	private CacheComponent cacheComponent;
	
	public UserSimpleVo buildSimpleVo(User user) {
		UserSimpleVo userSimpleVo = new UserSimpleVo();
		userSimpleVo.setId(user.getId());
		userSimpleVo.setAvatarThumbnail(GcUtils.getThumbnail(user.getAvatar()));
		userSimpleVo.setNickname(user.getNickname());
		return userSimpleVo;
	}
	
	public UserListVo buildListVo(User user) {
		UserListVo userListVo = new UserListVo();
		userListVo.setId(user.getId());
		userListVo.setAvatarThumbnail(GcUtils.getThumbnail(user.getAvatar()));
		userListVo.setNickname(user.getNickname());
		userListVo.setPhone(user.getPhone());
		userListVo.setUserRank(user.getUserRank());
		return userListVo;
	}
	
	public UserAdminVo buildAdminVo(User user) {
		UserAdminVo userAdminVo = new UserAdminVo();
		BeanUtils.copyProperties(user, userAdminVo);
		userAdminVo.setAvatarThumbnail(GcUtils.getThumbnail(user.getAvatar()));
		if (user.getInviterId() != null) {
			userAdminVo.setInviter(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(user.getInviterId())));
		}
		if(user.getParentId() != null) {
			userAdminVo.setParent(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(user.getParentId())));
		}
		userAdminVo.setUserRankLabel(GcUtils.getUserRankLabel(user.getUserRank()));
		return userAdminVo;
	}

	public UserAdminFullVo buildAdminFullVo(User user) {
		return null;
	}

}
