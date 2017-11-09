package com.zy.component;

import com.zy.common.model.tree.TreeHelper;
import com.zy.common.model.tree.TreeNode;
import com.zy.common.util.BeanUtils;
import com.zy.entity.cms.Matter;
import com.zy.entity.mergeusr.MergeUser;
import com.zy.entity.usr.User;
import com.zy.model.query.MatterCollectQueryModel;
import com.zy.model.query.MergeUserQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.MergeUserService;
import com.zy.service.UserService;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.MergeUserAdminVo;
import com.zy.vo.UserInfoVo;
import com.zy.vo.UserListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MergeUserComponent {
	
	@Autowired
	private CacheComponent cacheComponent;

	@Autowired
	private MergeUserService mergeUserService;

	@Autowired
	private UserService userService;
	
	public MergeUserAdminVo buildAdminVo(MergeUser mergeUser) {
		MergeUserAdminVo mergeUserAdminVo = new MergeUserAdminVo();
		BeanUtils.copyProperties(mergeUser, mergeUserAdminVo);
		if (mergeUser.getUserId() != null) {
			mergeUserAdminVo.setUser(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(mergeUser.getUserId())));
		}
		if (mergeUser.getInviterId() != null) {
			mergeUserAdminVo.setInviter(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(mergeUser.getInviterId())));
		}
		if(mergeUser.getParentId() != null) {
			mergeUserAdminVo.setParent(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(mergeUser.getParentId())));
		}
		if(mergeUser.getV4Id() != null){
			mergeUserAdminVo.setV4Parent(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(mergeUser.getV4Id())));
		}
		mergeUserAdminVo.setUserRankLabel(GcUtils.getMergeUserRankLabel(mergeUser.getUserRank()));
		return mergeUserAdminVo;
	}

	/**
	 * 查询 团队的总人数
	 * @param userId
	 * @return
	 */
	public long[] conyNewProducTeamTotal(Long userId,Integer productType) {
		long [] data = new long[]{0,0,0,0};
		List<MergeUser> users = mergeUserService.findAll(MergeUserQueryModel.builder().productTypeEQ(productType).build());
		List<MergeUser> children = TreeHelper.sortBreadth2(users, userId.toString(), v -> {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(v.getUserId().toString());
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


	/**
	 * 统计 直属特级
	 * @param userID
	 * @return
	 */
	public  List<MergeUser> conyteamTotalV4(Long userID,Integer productType) {
		List<MergeUser> mergeUsers = mergeUserService.findAll(MergeUserQueryModel.builder().productTypeEQ(productType).userRankEQ(User.UserRank.V4).v4IdEQ(userID).build());
		return mergeUsers;
	}

	/**
	 * 返回直属特接的ID
	 * @return
	 */
	public long[] tId(List<MergeUser> filterV4User ){
		if (filterV4User==null||filterV4User.isEmpty()){
			return null;
		}
		long [] ids = new long[filterV4User.size()];
		for (int i=0;i<filterV4User.size();i++){
			ids[i]= filterV4User.get(i).getUserId();
		}
		return ids;
	}

	public List<UserInfoVo> conyteamTotalV4Vo(long userId,Integer productType){
		List<MergeUser> mergeUsers = this.conyteamTotalV4(userId, productType);
		List<UserInfoVo> dataList = new ArrayList<UserInfoVo>();
		for(MergeUser m : mergeUsers){
			User user = userService.findOne(m.getUserId());
			User newUser = userService.findOne(m.getParentId());
			UserInfoVo userVo = new UserInfoVo();
			userVo.setImage1(user.getAvatar());
			userVo.setId(user.getId());
			userVo.setRealname(user.getNickname());
			userVo.setPhone(user.getPhone());
			userVo.setPname(newUser.getNickname());
			userVo.setPphone(newUser.getPhone());
			//判断是不是 新晋成员
			if (mergeUserService.findNewOne(user.getId())) {
				userVo.setNewflag("T");
			}
			dataList.add(userVo);
		}
		return dataList;
	}

	public Object buildVo(MergeUser mergeUser) {
		User user = userService.findOne(mergeUser.getUserId());
		UserListVo userListVo = new UserListVo();
		userListVo.setId(user.getId());
		userListVo.setAvatarThumbnail(GcUtils.getThumbnail(user.getAvatar()));
		userListVo.setNickname(user.getNickname());
		userListVo.setPhone(user.getPhone());
		userListVo.setUserRank(user.getUserRank());
		userListVo.setIsFrozen(user.getIsFrozen());
		return userListVo;
	}
}
