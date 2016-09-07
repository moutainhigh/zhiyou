package com.zy.service.impl;

import com.zy.Config;
import com.zy.ServiceUtils;
import com.zy.common.exception.BizException;
import com.zy.common.model.query.Page;
import com.zy.component.ActComponent;
import com.zy.component.FncComponent;
import com.zy.entity.fnc.Account;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserRank;
import com.zy.entity.usr.User.UserType;
import com.zy.entity.usr.UserLog;
import com.zy.extend.Producer;
import com.zy.mapper.AccountMapper;
import com.zy.mapper.UserLogMapper;
import com.zy.mapper.UserMapper;
import com.zy.model.BizCode;
import com.zy.model.Constants;
import com.zy.model.dto.AgentRegisterDto;
import com.zy.model.query.UserQueryModel;
import com.zy.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;
import static com.zy.entity.fnc.CurrencyType.*;
import static com.zy.model.Constants.TOPIC_REGISTER_SUCCESS;
import static com.zy.model.Constants.TOPIC_USER_RANK_CHANGED;

@Service
@Validated
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private AccountMapper accountMapper;

	@Autowired
	private UserLogMapper userLogMapper;

	@Autowired
	private FncComponent fncComponent;
	
	@Autowired
	private ActComponent actComponent;

	@Autowired
	private Config config;

	@Autowired
	private Producer producer;

	@Override
	public User findOne(@NotNull(message = "user is cannot be null") Long id) {
		return userMapper.findOne(id);
	}

	@Override
	public User findByPhone(@NotNull(message = "user phone cannot be  null") String phone) {
		return userMapper.findByPhone(phone);
	}

	@Override
	public Page<User> findPage(@NotNull UserQueryModel userQueryModel) {
		if (userQueryModel.getPageNumber() == null)
			userQueryModel.setPageNumber(0);
		if (userQueryModel.getPageSize() == null)
			userQueryModel.setPageSize(20);
		long total = userMapper.count(userQueryModel);
		List<User> data = userMapper.findAll(userQueryModel);
		Page<User> page = new Page<>();
		page.setPageNumber(userQueryModel.getPageNumber());
		page.setPageSize(userQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public List<User> findAll(@NotNull UserQueryModel userQueryModel) {
		return userMapper.findAll(userQueryModel);
	}

	@Override
	public User registerAgent(@NotNull AgentRegisterDto agentRegisterDto) {
		validate(agentRegisterDto);

		Long inviterId = agentRegisterDto.getInviterId();
		User inviter = userMapper.findOne(inviterId);
		validate(inviter, NOT_NULL, "inviter id " + inviterId + " is not found");

		String openId = agentRegisterDto.getOpenId();
		String phone = agentRegisterDto.getPhone();

		String avatar = agentRegisterDto.getAvatar();
		String nickname = agentRegisterDto.getNickname();
		String registerIp = agentRegisterDto.getRegisterIp();

		User user = userMapper.findByOpenId(openId);

		if (user != null) {
			if (user.getPhone().equals(phone)) {
				return user; // 幂等操作
			} else {
				throw new BizException(BizCode.ERROR, "微信已绑定其他手机");
			}
		}

		user = userMapper.findByPhone(phone);
		if (user != null) {
			if (user.getUserType() != UserType.代理) {
				throw new BizException(BizCode.ERROR, "微信只能绑定代理");
			}
			if (user.getOpenId() != null) {
				if (user.getOpenId().equals(openId)) {
					return user; // 幂等操作 虽然不可能到达
				} else {
					throw new BizException(BizCode.ERROR, "手机已经绑定其他微信");
				}
			} else {
				/* 绑定 */
				user.setNickname(nickname);
				user.setAvatar(avatar);
				user.setRegisterTime(new Date());
				user.setRegisterIp(registerIp);
				user.setOpenId(openId);
				userMapper.update(user);
			}
		} else {

			/* 注册 */
			user = new User();
			user.setUserType(UserType.代理);
			user.setRegisterIp(registerIp);
			user.setInviterId(inviterId);
			user.setNickname(nickname);
			user.setAvatar(avatar);
			user.setRegisterTime(new Date());
			user.setIsFrozen(false);
			user.setUserRank(UserRank.V0);
			user.setOpenId(openId);

			validate(user);
			userMapper.insert(user);
			insertAccount(user); // 初始化
		}

		producer.send(TOPIC_REGISTER_SUCCESS, user);
		return user;
	}

	@Override
	public long count(@NotNull UserQueryModel userQueryModel) {
		return userMapper.count(userQueryModel);
	}

	@Override
	public String hashPassword(@NotNull(message = "plain password is blank") String plainPassword) {
		return ServiceUtils.hashPassword(plainPassword);
	}

	@Override
	public void freeze(@NotNull(message = "id is null") Long id, @NotNull(message = "operatorId is null") Long operatorId) {
		User frozenUser = userMapper.findOne(id);
		validate(frozenUser, NOT_NULL, "add vip user is null, id = " + id);
		User operatorUser = userMapper.findOne(operatorId);
		validate(operatorUser, NOT_NULL, "operator user is null, id = " + id);

		User userForMerge = new User();
		userForMerge.setId(id);
		userForMerge.setIsFrozen(true);
		if (userMapper.merge(userForMerge, "isFrozen") > 0) {
			UserLog userLog = new UserLog();
			userLog.setOperatorId(operatorId);
			userLog.setOperatedTime(new Date());
			userLog.setOperation("冻结");
			userLog.setRemark("管理员[" + operatorUser.getNickname() + "]冻结了用户[" + frozenUser.getNickname() + "]");
			userLogMapper.insert(userLog);
		}
		;
	}

	@Override
	public void unfreeze(@NotNull(message = "id is null") Long id, @NotNull(message = "operatorId is null") Long operatorId) {

		User frozenUser = userMapper.findOne(id);
		validate(frozenUser, NOT_NULL, "add vip user is null, id = " + id);
		User operatorUser = userMapper.findOne(operatorId);
		validate(operatorUser, NOT_NULL, "operator user is null, id = " + id);

		User userForMerge = new User();
		userForMerge.setId(id);
		userForMerge.setIsFrozen(false);
		if (userMapper.merge(userForMerge, "isFrozen") > 0) {
			UserLog userLog = new UserLog();
			userLog.setOperatorId(operatorId);
			userLog.setOperatedTime(new Date());
			userLog.setOperation("解冻");
			userLog.setRemark("管理员[" + operatorUser.getNickname() + "]解冻了用户[" + frozenUser.getNickname() + "]");
			userLogMapper.insert(userLog);
		}
		;

	}

	@Override
	public void modifyUserRank(@NotNull(message = "id is null") Long id, @NotNull(message = "remark is null") UserRank userRank, String remark,
			@NotNull(message = "operator Id is null") Long operatorId) {

		User user = userMapper.findOne(id);
		validate(user, NOT_NULL, "add vip user is null, id = " + id);
		User operator = userMapper.findOne(operatorId);
		validate(operator, NOT_NULL, "operator user is null, id = " + id);

		user.setUserRank(userRank);
		if (userMapper.update(user) > 0) {
			UserLog userLog = new UserLog();
			userLog.setOperatorId(operatorId);
			userLog.setOperatedTime(new Date());
			userLog.setOperation("加VIP");
			String msg = userRank == null ? "减去" : "增加";
			userLog.setRemark("管理员[" + operator.getNickname() + "]给用户[" + user.getNickname() + "]" + msg + "了VIP");
			userLogMapper.insert(userLog);
			producer.send(TOPIC_USER_RANK_CHANGED, user);
		}

	}

	@Override
	public User findByOpenId(@NotBlank String openId) {
		return userMapper.findByOpenId(openId);
	}

	/* 注册自动生成账号 */
	private void insertAccount(User user) {
		Long userId = user.getId();
		BigDecimal zero = new BigDecimal("0.00");
		/* account insert */
		{
			Account account = new Account();
			account.setAmount(zero);
			account.setCurrencyType(现金);
			account.setUserId(userId);
			account.setVersion(0);
			validate(account);
			accountMapper.insert(account);
		}
		{
			Account account = new Account();
			account.setAmount(zero);
			account.setCurrencyType(积分);
			account.setUserId(userId);
			account.setVersion(0);
			validate(account);
			accountMapper.insert(account);
		}
		{
			Account account = new Account();
			account.setAmount(zero);
			account.setCurrencyType(金币);
			account.setUserId(userId);
			account.setVersion(0);
			validate(account);
			accountMapper.insert(account);
		}

	}

	@Override
	public void modifyPhone(Long userId, @NotBlank(message = "phone is null") String phone) {
		findAndValidate(userId);
		User user = userMapper.findByPhone(phone);
		validate(user, NOT_NULL, "user existed, phone = " + phone);

		User userForMerge = new User();
		userForMerge.setId(userId);
		userForMerge.setPhone(phone);
		userMapper.merge(userForMerge, "phone");
	}

	@Override
	public void modifyNickname(Long userId, @NotBlank(message = "nickname is null") String nickname) {
		findAndValidate(userId);

		User userForMerge = new User();
		userForMerge.setId(userId);
		userForMerge.setNickname(nickname);
		userMapper.merge(userForMerge, "nickname");

	}

	@Override
	public void modifyAvatar(Long userId, @NotBlank(message = "avatar is null") String avatar) {
		findAndValidate(userId);

		User userForMerge = new User();
		userForMerge.setId(userId);
		userForMerge.setAvatar(avatar);
		userMapper.merge(userForMerge, "avatar");

	}

	@Override
	public void modifyPasswordAndPhone(Long id, String password, @NotBlank(message = "phone is null") String phone) {
		findAndValidate(id);

		User userForMerge = new User();
		userForMerge.setId(id);
		userForMerge.setPhone(phone);
		if (StringUtils.isNotBlank(password)) {
			userForMerge.setPassword(ServiceUtils.hashPassword(password));
			userMapper.merge(userForMerge, "phone", "password");
		} else {
			userMapper.merge(userForMerge, "phone");
		}
	}

	@Override
	public void modifyPassword(Long id, @NotBlank(message = "password is null") String password) {
		findAndValidate(id);

		User userForMerge = new User();
		userForMerge.setId(id);
		userForMerge.setPassword(hashPassword(password));
		userMapper.merge(userForMerge, "password");
	}

	@Override
	public void loginSuccess(@NotNull Long id) {
		User user = userMapper.findOne(id);
		validate(user, NOT_NULL, "user id" + id + "not found");
		producer.send(Constants.TOPIC_LOGIN_SUCCESS, user);
	}

	@Override
	public void modifyInfo(@NotNull User user) {
		findAndValidate(user.getId());

		userMapper.merge(user, "nickname", "qq", "isInfoCompleted");
	}

	private User findAndValidate(Long userId) {
		User user = userMapper.findOne(userId);
		validate(user, NOT_NULL, "user id " + userId + "not found");
		return user;
	}

}
