package com.gc.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zy.common.util.BeanUtils;
import com.gc.entity.usr.User;
import com.zy.util.GcUtils;
import com.gc.vo.UserAdminSimpleVo;
import com.gc.vo.UserAdminVo;
import com.gc.vo.UserSimpleVo;

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
