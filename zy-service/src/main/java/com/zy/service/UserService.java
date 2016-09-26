package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserRank;
import com.zy.model.dto.AgentRegisterDto;
import com.zy.model.query.UserQueryModel;

import java.util.List;

public interface UserService {

	User findOne(Long id);

	User findByPhone(String phone);

	User findByOpenId(String openId);

	Page<User> findPage(UserQueryModel userQueryModel);

	List<User> findAll(UserQueryModel userQueryModel);

	void freeze(Long id, Long operatorId);
	
	void unfreeze(Long id, Long operatorId);

	void modifyUserRank(Long id, UserRank userRank, Long operatorId, String remark);
	
	User registerAgent(AgentRegisterDto agentRegisterDto);

	long count(UserQueryModel userQueryModel);
	
	String hashPassword(String plainPassword);
	
	void modifyPassword(Long id, String password, Long operatorId);
	
	void modifyPhone(Long id, String phone, Long operatorId);
	
	void modifyNickname(Long id, String nickname);
	
	void modifyAvatar(Long id, String avatar);
	
	void modifyInfo(User user);
	
	void loginSuccess(Long id);

	void unbind(Long id, Long operatorId, String remark);

	void modifyParentId(Long id, Long parentId, Long operatorId, String remark);
	
}
