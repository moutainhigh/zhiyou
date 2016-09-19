package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserUpgrade;
import com.zy.model.query.UserUpgradeQueryModel;

import java.util.List;

public interface UserUpgradeService {

	void upgrade(Long userId, User.UserRank fromUserRank, User.UserRank toUserRank);
	
	Page<UserUpgrade> findPage(UserUpgradeQueryModel userUpgradeQueryModel);

	List<UserUpgrade> findAll(UserUpgradeQueryModel userUpgradeQueryModel);
	
	long count(UserUpgradeQueryModel userUpgradeQueryModel);
}
