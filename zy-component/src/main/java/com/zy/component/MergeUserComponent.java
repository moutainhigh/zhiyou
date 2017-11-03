package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.mergeusr.MergeUser;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.MergeUserAdminVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MergeUserComponent {
	
	@Autowired
	private CacheComponent cacheComponent;
	
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

}
