package com.zy.component;

import com.zy.common.exception.BizException;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserUpgrade;
import com.zy.mapper.UserMapper;
import com.zy.mapper.UserUpgradeMapper;
import com.zy.model.BizCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@Component
@Validated
public class UsrComponent {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserUpgradeMapper userUpgradeMapper;

	public void upgrade(@NotNull Long userId, @NotNull User.UserRank from, @NotNull User.UserRank to) {
		User user = userMapper.findOne(userId);
		validate(user, NOT_NULL, "user id " + userId + " is not found");
		User.UserRank userRank = user.getUserRank();
		if (userRank == to) {
			return; // 幂等操作
		} else if (userRank != from){
			throw new BizException(BizCode.ERROR, "升级前等级不一致");
		}
		UserUpgrade userUpgrade = new UserUpgrade();
		userUpgrade.setFromUserRank(from);
		userUpgrade.setToUserRank(to);
		userUpgrade.setUpgradedTime(new Date());
		userUpgrade.setUserId(userId);
		userUpgradeMapper.insert(userUpgrade);

		user.setLastUpgradedTime(new Date());
		user.setUserRank(to);
		userMapper.update(user);
	}

}
