package com.zy.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.usr.User;
import com.zy.util.GcUtils;
import com.zy.vo.UserAdminSimpleVo;
import com.zy.vo.UserAdminVo;
import com.zy.vo.UserListVo;
import com.zy.vo.UserSimpleVo;

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
		Long inviterId = user.getInviterId();
		if (inviterId != null) {
			User inviter = cacheComponent.getUser(inviterId);
			if (inviter != null) {
				UserAdminSimpleVo userAdminSimpleVo = new UserAdminSimpleVo();
				BeanUtils.copyProperties(inviter, userAdminSimpleVo);
				userAdminSimpleVo.setAvatarThumbnail(GcUtils.getThumbnail(inviter.getAvatar()));
				userAdminVo.setInviter(userAdminSimpleVo);
			}
		}
		return userAdminVo;
	}

}
