package com.zy.component;

import org.springframework.stereotype.Component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.usr.UserUpgrade;
import com.zy.util.GcUtils;
import com.zy.vo.UserUpgradeAdminVo;

@Component
public class UserUpgradeComponent {

	public UserUpgradeAdminVo buildAdminVo(UserUpgrade userUpgrade) {
		UserUpgradeAdminVo userUpgradeAdminVo = new UserUpgradeAdminVo();
		BeanUtils.copyProperties(userUpgrade, userUpgradeAdminVo);
		
		userUpgradeAdminVo.setFromUserRankLabel(GcUtils.getUserRankLabel(userUpgrade.getFromUserRank()));
		userUpgradeAdminVo.setToUserRankLabel(GcUtils.getUserRankLabel(userUpgrade.getToUserRank()));
		userUpgradeAdminVo.setUpgradedTimeLabel(GcUtils.formatDate(userUpgrade.getUpgradedTime(), "yyyy-MM-dd HH:mm"));
		return userUpgradeAdminVo;
	}
}
