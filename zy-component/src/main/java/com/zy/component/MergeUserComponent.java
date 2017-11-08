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
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.MergeUserAdminVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MergeUserComponent {
	
	@Autowired
	private CacheComponent cacheComponent;

	@Autowired
	private MergeUserService mergeUserService;
	
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
	public long[] conyNewProducTeamTotal(Long userId) {
		long [] data = new long[]{0,0,0,0};
		List<MergeUser> users = mergeUserService.findAll(MergeUserQueryModel.builder().productTypeEQ(2).build());
		List<MergeUser> children = TreeHelper.sortBreadth2(users, userId.toString(), v -> {
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

	/**
	 * 查看权限
	 * @param user
	 * @return
	 */
	public String  findRole(User user) {
		String falg = "F";
		if (user!=null&&user.getUserRank()==User.UserRank.V4){
			if (1!=user.getViewflag()){
				falg ="T";
			}
		}
		return  falg;
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

}
