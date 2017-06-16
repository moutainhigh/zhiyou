package com.zy.component;

import com.zy.common.exception.BizException;
import com.zy.common.model.tree.TreeHelper;
import com.zy.common.model.tree.TreeNode;
import com.zy.common.util.BeanUtils;
import com.zy.common.util.DateUtil;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserInfo;
import com.zy.entity.usr.UserUpgrade;
import com.zy.model.BizCode;
import com.zy.model.TeamModel;
import com.zy.model.dto.DepositSumDto;
import com.zy.model.dto.UserTeamCountDto;
import com.zy.model.dto.UserTeamDto;
import com.zy.model.query.UserQueryModel;
import com.zy.model.query.UserUpgradeQueryModel;
import com.zy.model.query.UserlongQueryModel;
import com.zy.service.UserInfoService;
import com.zy.service.UserService;
import com.zy.service.UserUpgradeService;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class UserComponent {
	
	@Autowired
	private CacheComponent cacheComponent;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserInfoService userInfoService;
	
	@Autowired
	private UserUpgradeService userUpgradeService;
	
	@Autowired
	private UserUpgradeComponent userUpgradeComponent;
	
	@Autowired
	private UserInfoComponent userInfoComponent;


	public UserSimpleVo buildSimpleVo(User user) {
		UserSimpleVo userSimpleVo = new UserSimpleVo();
		userSimpleVo.setId(user.getId());
		userSimpleVo.setAvatarThumbnail(GcUtils.getThumbnail(user.getAvatar()));
		userSimpleVo.setNickname(user.getNickname());
		return userSimpleVo;
	}
	
	public UserListVo buildListVo(User user) {
		UserListVo userListVo = new UserListVo();
		userListVo.setId(user.getId());
		userListVo.setAvatarThumbnail(GcUtils.getThumbnail(user.getAvatar()));
		userListVo.setNickname(user.getNickname());
		userListVo.setPhone(user.getPhone());
		userListVo.setUserRank(user.getUserRank());
		userListVo.setIsFrozen(user.getIsFrozen());
		return userListVo;
	}
	
	public UserAdminVo buildAdminVo(User user) {
		UserAdminVo userAdminVo = new UserAdminVo();
		BeanUtils.copyProperties(user, userAdminVo);
		userAdminVo.setAvatarThumbnail(GcUtils.getThumbnail(user.getAvatar()));
		if (user.getInviterId() != null) {
			userAdminVo.setInviter(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(user.getInviterId())));
		}
		if(user.getParentId() != null) {
			userAdminVo.setParent(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(user.getParentId())));
		}
		userAdminVo.setUserRankLabel(GcUtils.getUserRankLabel(user.getUserRank()));
		Long bossId = user.getBossId();
		if (bossId != null) {
			userAdminVo.setBoss(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(bossId)));
		}
		return userAdminVo;
	}

	public UserAdminFullVo buildAdminFullVo(User user) {
		UserAdminFullVo userAdminFullVo = new UserAdminFullVo();
		BeanUtils.copyProperties(user, userAdminFullVo);
		
		userAdminFullVo.setUserRankLabel(GcUtils.getUserRankLabel(user.getUserRank()));
		userAdminFullVo.setAvatarThumbnail(GcUtils.getThumbnail(user.getAvatar()));
		Long inviterId = user.getInviterId();
		Long parentId = user.getParentId();
		if(inviterId != null) {
			userAdminFullVo.setInviter(VoHelper.buildUserAdminSimpleVo(userService.findOne(inviterId)));
		}
		if(parentId != null) {
			userAdminFullVo.setParent(VoHelper.buildUserAdminSimpleVo(userService.findOne(parentId)));
		}
		Long userId = user.getId();
		if(userId != null) {
			List<UserUpgrade> userUpgrades = userUpgradeService.findAll(UserUpgradeQueryModel.builder().userIdEQ(userId).build());
			if(!userUpgrades.isEmpty()) {
				userAdminFullVo.setUserUpgrades(userUpgrades.stream().map(userUpgradeComponent::buildAdminVo).collect(Collectors.toList()));
			}
			
			UserInfo userInfo = userInfoService.findByUserId(userId);
			if(userInfo != null) {
				userAdminFullVo.setUserInfo(userInfoComponent.buildAdminVo(userInfo));
			}
		}
		
		return userAdminFullVo;
	}
	
	public UserAdminSimpleVo buildAdminSimpleVo(User user) {
		return VoHelper.buildUserAdminSimpleVo(user);
	}

	public UserTeamDto buildUserTeamDto(UserTeamDto userTeamDto){
		userTeamDto.setNickname(userService.findRealName(userTeamDto.getUserId()));
		userTeamDto.setRank(this.getRank(userTeamDto.getUserId()));
		return userTeamDto;
	}

	/**
	 * 获取  用排名
	 * @param userId
	 * @return
     */
	public int getRank(Long userId){
		int myrank=0;
		UserlongQueryModel userlongQueryModel = new UserlongQueryModel();
		userlongQueryModel.setRemark("从V0%");
		userlongQueryModel.setRegisterTimeLT(DateUtil.getBeforeMonthBegin(new Date(),0,0));
		userlongQueryModel.setRegisterTimeGTE(DateUtil.getBeforeMonthBegin(new Date(),-1,0));
		List<DepositSumDto> depositSumDtoList = userService.findRankGroup(userlongQueryModel);
		Map<String,Integer> dataMap = new HashMap<String,Integer>();
		if (depositSumDtoList!=null && !depositSumDtoList.isEmpty()){
			for (int i = 0; i < depositSumDtoList.size();i++) { //将list转成map
				DepositSumDto depositSumDto = depositSumDtoList.get(i);
				dataMap.put(String.valueOf(depositSumDto.getSumAmount()),i+1);
			}
			userlongQueryModel.setParentIdNL(userId);
			List<UserTeamDto> mydepositSumDtoList = userService.findByRank(userlongQueryModel);
			if (mydepositSumDtoList != null && !mydepositSumDtoList.isEmpty()) {
				UserTeamDto userTeamDto = mydepositSumDtoList.get(0);
				myrank=dataMap.get(String.valueOf(userTeamDto.getNum()));
			}
		}
		return myrank;
	}

	/**
	 * 计算 活动占比
	 * @param userId
	 * @return
     */
	public String activeProportion(Long userId) {
		UserQueryModel userQueryModel = new UserQueryModel();
		userQueryModel.setRegisterTimeLT(DateUtil.getBeforeMonthBegin(new Date(),-3,0));
		userQueryModel.setRegisterTimeGTE(DateUtil.getBeforeMonthEnd(new Date(),0,0));
		long total = userService.countByActive(userQueryModel);
		userQueryModel.setParentIdNL(userId);
		long mytotal = userService.countByActive(userQueryModel);
		DecimalFormat df = new DecimalFormat("###0.00");
		if (total==0){
			return "0.00";
		}else{
		return df.format(total/mytotal*100);
		}
	}

	/**
	 * 统计 新进直属特级人数
	 * @param userId
	 * @return
     */
	public  List<User> conyteamTotalV4(Long userId) {
		List<User> users = userService.findAll(new UserQueryModel());
		List<User> children = TreeHelper.sortBreadth2(users, userId.toString(), v -> {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(v.getId().toString());
			treeNode.setParentId(v.getParentId() == null ? null : v.getParentId().toString());
			return treeNode;
		});
		return children.stream().filter(v -> v.getUserRank() == User.UserRank.V4).collect(Collectors.toList());
	}


	/**
	 * 返回直属特接的ID
	 * @return
     */
	public long[] tId(List<User> filterV4User ){
		if (filterV4User==null||filterV4User.isEmpty()){
			return null;
		}
		long [] ids = new long[filterV4User.size()];
		for (int i=0;i<filterV4User.size();i++){
			ids[i]= filterV4User.get(i).getId();
		}
		return ids;
	}

	/**
	 * 查询 团队的总人数
	 * @param userId
	 * @return
     */
	public long[] conyteamTotal(Long userId) {
		long [] data = new long[]{0,0,0,0};
		List<User> users = userService.findAll(new UserQueryModel());
		List<User> children = TreeHelper.sortBreadth2(users, userId.toString(), v -> {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(v.getId().toString());
			treeNode.setParentId(v.getParentId() == null ? null : v.getParentId().toString());
			return treeNode;
		});
		children = children.stream().filter(v -> v.getUserRank() != User.UserRank.V0).collect(Collectors.toList());
		data[0]= children.stream().filter(v -> v.getUserRank() == User.UserRank.V4).collect(Collectors.toList()).size();
		data[1]= children.stream().filter(v -> v.getUserRank() == User.UserRank.V3).collect(Collectors.toList()).size();
		data[2]= children.stream().filter(v -> v.getUserRank() == User.UserRank.V2).collect(Collectors.toList()).size();
		data[3]= children.stream().filter(v -> v.getUserRank() == User.UserRank.V1).collect(Collectors.toList()).size();
		return data;
	}
}
