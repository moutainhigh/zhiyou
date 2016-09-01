package com.gc.service;

import java.util.List;

import com.zy.common.model.query.Page;
import com.gc.entity.usr.User;
import com.gc.entity.usr.WeixinUser;
import com.gc.entity.usr.User.UserRank;
import com.gc.model.dto.RegisterDto;
import com.gc.model.query.UserQueryModel;

public interface UserService {

	User findOne(Long id);

	User findByPhone(String phone);

	Page<User> findPage(UserQueryModel userQueryModel);

	List<User> findAll(UserQueryModel userQueryModel);

	void freeze(Long id, Long operatorId);
	
	void unfreeze(Long id, Long operatorId);

	void modifyUserRank(Long id, UserRank userRank, String remark, Long operatorId);
	
	User registerAgent(RegisterDto registerDto, UserRank userRank);
	
	User registerMerchant(RegisterDto registerDto);
	
	User registerBuyer(WeixinUser weixinUser, String registerIp, Long inviterId);

	long count(UserQueryModel userQueryModel);
	
	String hashPassword(String plainPassword);
	
	User findByInviteCode(String inviteCode);
	
	User findByOpenId(String openId);
	
	void modifyPasswordAndPhone(Long id, String password, String phone);
	
	void modifyPassword(Long id, String password);
	
	void modifyPhone(Long id, String phone);
	
	void modifyNickname(Long id, String nickname);
	
	void modifyAvatar(Long id, String avatar);
	
	void modifyInfo(User user);
	
	void bindPhone(Long id, String phone);
	
	void loginSuccess(Long id);
	
}
