package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.sys.SystemCode;
import com.zy.entity.usr.User;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.SystemCodeAdminVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SystemCodeComponent {

	@Autowired
	private CacheComponent cacheComponent;

	public SystemCodeAdminVo buildAdminVo(SystemCode systemCode) {
		SystemCodeAdminVo systemCodeAdminVo = new SystemCodeAdminVo();
		BeanUtils.copyProperties(systemCode,systemCodeAdminVo);
		Long createId = systemCode.getCreateBy();
		Long updateId = systemCode.getUpdateBy();
		if (createId != null) {
			User user1 = cacheComponent.getUser(createId);
			systemCodeAdminVo.setCreateUser(VoHelper.buildUserAdminSimpleVo(user1));
		}
		if (updateId != null) {
			User user2 = cacheComponent.getUser(updateId);
			systemCodeAdminVo.setUpdateUser(VoHelper.buildUserAdminSimpleVo(user2));
		}
		systemCodeAdminVo.setCreateTimeLabel(GcUtils.formatDate(systemCode.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
		systemCodeAdminVo.setUpdateTimeLabel(GcUtils.formatDate(systemCode.getUpdateDate(), "yyyy-MM-dd HH:mm:ss"));
		return systemCodeAdminVo;
	}
}
