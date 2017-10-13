package com.zy.component;

import com.zy.common.exception.BizException;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserLog;
import com.zy.entity.usr.UserUpgrade;
import com.zy.extend.Producer;
import com.zy.mapper.UserLogMapper;
import com.zy.mapper.UserMapper;
import com.zy.mapper.UserUpgradeMapper;
import com.zy.model.BizCode;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;
import static com.zy.model.Constants.TOPIC_USER_RANK_CHANGED;

@Component
@Validated
public class UsrComponent {

	@Autowired
	private Producer producer;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserLogMapper userLogMapper;

	@Autowired
	private UserUpgradeMapper userUpgradeMapper;

	public void upgrade(@NotNull Long userId, @NotNull User.UserRank from, @NotNull User.UserRank to,Boolean isToV4) {
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

		//判断是否直接升为特级
		if (userRank != null && userRank == User.UserRank.V4){
			User parent = user;
			if(isToV4 != null && isToV4) {
				user.setIsToV4(true);
			}
			do{
				parent = userMapper.findOne(parent.getParentId());
			}while (parent.getLargearea() == null);
			user.setLargearea(parent.getLargearea());
		}

		userMapper.update(user);
		producer.send(TOPIC_USER_RANK_CHANGED, user.getId());
	}

	public void recordUserLog(@NotNull Long userId, @NotNull Long operatorId, @NotBlank String operation, String remark) {

		User user = userMapper.findOne(userId);
		validate(user, NOT_NULL, "user id " + userId + " is not found");

		User operator = userMapper.findOne(operatorId);
		validate(operator, NOT_NULL, "operator id " + operatorId + " is not found");

		UserLog userLog = new UserLog();
		userLog.setUserId(userId);
		userLog.setOperatorId(operatorId);
		userLog.setOperatedTime(new Date());
		userLog.setOperation(operation);
		userLog.setRemark(remark);
		userLogMapper.insert(userLog);
	}

}
