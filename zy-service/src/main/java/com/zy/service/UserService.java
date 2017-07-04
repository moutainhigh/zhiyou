package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserRank;
import com.zy.model.dto.*;
import com.zy.model.query.UserQueryModel;
import com.zy.model.query.UserlongQueryModel;


import java.util.List;
import java.util.Map;

public interface UserService {

	User findOne(Long id);

	User findByPhone(String phone);

	User findByCode(String code);

	User findByOpenId(String openId);

	Page<User> findPage(UserQueryModel userQueryModel);

	List<User> findAll(UserQueryModel userQueryModel);

	void setParentId(Long id, Long parentId);
	
	User registerAgent(AgentRegisterDto agentRegisterDto);

	long count(UserQueryModel userQueryModel);
	
	String hashPassword(String plainPassword);

	void modifyPassword(Long id, String password);
	
	void modifyNickname(Long id, String nickname);
	
	void modifyAvatar(Long id, String avatar);

	void generateCode(Long id);

	/* 管理员操作 */
	void modifyParentIdAdmin(Long id, Long parentId, Long operatorId, String remark);

	void modifyPasswordAdmin(Long id, String password, Long operatorId);

	void modifyPhoneAdmin(Long id, String phone, Long operatorId);

	void modifyNicknameAdmin(Long id, String nickname, Long operatorId);

	void freezeAdmin(Long id, Long operatorId);

	void unfreezeAdmin(Long id, Long operatorId);

	void modifyUserRankAdmin(Long id, UserRank userRank, Long operatorId, String remark);

	void unbindAdmin(Long id, Long operatorId, String remark);

	void modifyIsRootAdmin(Long id, boolean isRoot, String rootName, Long operatorId, String remark);

	void modifyIsBoss(Long id, boolean isBoss, String bossName, Long operatorId);

	void modifyBossId(Long id, Long bossId);

	void modifyIsDirector(Long id, boolean isDirector);

	void modifyIsShareholder(Long id, boolean isShareholder);

	long[] conyteamTotal(Long userId);

	long[] countdirTotal(Long userId);

	Map<String,Object> countNewMemTotal(Long userId, boolean b);

	String findRealName(Long userId);

	Page<UserTeamDto> disposeRank(UserlongQueryModel userlongQueryModel, boolean b);

	long countByActive(UserQueryModel userQueryModel);

	Page<User> findActive(UserQueryModel userQueryModel, boolean b);

	List<DepositSumDto> findRankGroup(UserlongQueryModel userlongQueryModel);

	List<UserTeamDto> findByRank(UserlongQueryModel userlongQueryModel);

	Map<String,Object> findNewSup(long[] ids);

	Page<User> findAddpeople(UserQueryModel userQueryModel);

	boolean findNewOne(Long id);

	Page<User> findPage1(UserQueryModel userQueryModel);

	List<UserDto> findUserAll(UserQueryModel userQueryModel);

	long countUserAll(UserQueryModel userQueryModel);
}
