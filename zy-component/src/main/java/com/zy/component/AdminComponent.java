package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.adm.Admin;
import com.zy.entity.usr.User;
import com.zy.util.GcUtils;
import com.zy.vo.AdminAdminVo;
import com.zy.vo.UserAdminSimpleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.apache.shiro.web.filter.mgt.DefaultFilter.user;

@Component
public class AdminComponent {

	@Autowired
	private CacheComponent cacheComponent;

	public AdminAdminVo buildAdminVo(Admin admin) {
		AdminAdminVo adminAdminVo = new AdminAdminVo();
		BeanUtils.copyProperties(user, adminAdminVo);
		Long userId = admin.getUserId();
		if (userId != null) {
			User user = cacheComponent.getUser(userId);
			if (user != null) {
				UserAdminSimpleVo userAdminSimpleVo = new UserAdminSimpleVo();
				BeanUtils.copyProperties(user, userAdminSimpleVo);
				userAdminSimpleVo.setAvatarThumbnail(GcUtils.getThumbnail(user.getAvatar()));
				adminAdminVo.setUser(userAdminSimpleVo);
			}
		}
		return adminAdminVo;
	}
	
}
