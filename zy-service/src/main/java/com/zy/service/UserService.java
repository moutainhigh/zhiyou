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


	
	User registerAgent(AgentRegisterDto agentRegisterDto);

	long count(UserQueryModel userQueryModel);
	
	String hashPassword(String plainPassword);

	void modifyPassword(Long id, String password);
	
	void modifyNickname(Long id, String nickname);
	
	void modifyAvatar(Long id, String avatar);


	/* 管理员操作 */
	void modifyParentIdAdmin(Long id, Long parentId, Long operatorId, String remark);

	void modifyPasswordAdmin(Long id, String password, Long operatorId);

	void modifyPhoneAdmin(Long id, String phone, Long operatorId);

	void freezeAdmin(Long id, Long operatorId);

	void unfreezeAdmin(Long id, Long operatorId);

	void modifyUserRankAdmin(Long id, UserRank userRank, Long operatorId, String remark);

	void unbindAdmin(Long id, Long operatorId, String remark);

	void modifyIsRootAdmin(Long id, boolean isRoot, Long operatorId, String remark);
	
}
