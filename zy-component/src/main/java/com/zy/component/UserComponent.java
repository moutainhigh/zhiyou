package com.zy.component;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserInfo;
import com.zy.entity.usr.UserUpgrade;
import com.zy.model.query.UserUpgradeQueryModel;
import com.zy.service.UserInfoService;
import com.zy.service.UserService;
import com.zy.service.UserUpgradeService;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.UserAdminFullVo;
import com.zy.vo.UserAdminVo;
import com.zy.vo.UserListVo;
import com.zy.vo.UserSimpleVo;

@Component
public class UserComponent {
	
	@Autowired
	private CacheComponent cacheComponent;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserInfoService userInfoService;
	
	@Autowired
	private UserUpgradeService userUpgradeService;
	
	@Autowired
	private UserUpgradeComponent userUpgradeComponent;
	
	@Autowired
	private UserInfoComponent userInfoComponent;
	
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
		UserAdminFullVo userAdminFullVo = new UserAdminFullVo();
		BeanUtils.copyProperties(user, userAdminFullVo);
		
		userAdminFullVo.setUserRankLabel(GcUtils.getUserRankLabel(user.getUserRank()));
		userAdminFullVo.setAvatarThumbnail(GcUtils.getThumbnail(user.getAvatar()));
		Long inviterId = user.getInviterId();
		Long parentId = user.getParentId();
		if(inviterId != null) {
			userAdminFullVo.setInviter(VoHelper.buildUserAdminSimpleVo(userService.findOne(inviterId)));
		}
		if(parentId != null) {
			userAdminFullVo.setParent(VoHelper.buildUserAdminSimpleVo(userService.findOne(parentId)));
		}
		Long userId = user.getId();
		if(userId != null) {
			List<UserUpgrade> userUpgrades = userUpgradeService.findAll(UserUpgradeQueryModel.builder().userIdEQ(userId).build());
			if(!userUpgrades.isEmpty()) {
				userAdminFullVo.setUserUpgrades(userUpgrades.stream().map(userUpgradeComponent::buildAdminVo).collect(Collectors.toList()));
			}
			
			UserInfo userInfo = userInfoService.findByUserId(userId);
			if(userInfo != null) {
				userAdminFullVo.setUserInfo(userInfoComponent.buildAdminVo(userInfo));
			}
		}
		
		return userAdminFullVo;
	}

}
