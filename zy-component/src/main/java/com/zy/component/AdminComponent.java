package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.adm.Admin;
import com.zy.entity.usr.User;
import com.zy.util.VoHelper;
import com.zy.vo.AdminAdminVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminComponent {

	@Autowired
	private CacheComponent cacheComponent;

	public AdminAdminVo buildAdminVo(Admin admin) {
		AdminAdminVo adminAdminVo = new AdminAdminVo();
		BeanUtils.copyProperties(admin, adminAdminVo);
		Long userId = admin.getUserId();

		if (userId != null) {
			User user = cacheComponent.getUser(userId);
			if (user != null) {
				adminAdminVo.setUser(VoHelper.buildUserAdminSimpleVo(user));
			}
		}
		return adminAdminVo;
	}
	
}
