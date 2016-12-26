package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.act.Policy;
import com.zy.entity.usr.User;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.PolicyAdminVo;
import com.zy.vo.PolicyDetailVo;
import com.zy.vo.PolicyExportVo;
import com.zy.vo.PolicyListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PolicyComponent {

	@Autowired
	private CacheComponent cacheComponent;
	
	public PolicyAdminVo buildAdminVo(Policy policy) {
		PolicyAdminVo policyAdminVo = new PolicyAdminVo();
		BeanUtils.copyProperties(policy, policyAdminVo);
		
		Long userId = policy.getUserId();
		if(userId != null) {
			User user = cacheComponent.getUser(userId);
			policyAdminVo.setUser(VoHelper.buildUserAdminSimpleVo(user));
		}
		policyAdminVo.setCreatedTimeLabel(GcUtils.formatDate(policy.getCreatedTime(), "yyyy-MM-dd HH:mm:ss"));
		return policyAdminVo;
	}
	
	public PolicyListVo buildListVo(Policy policy) {
		PolicyListVo policyListVo = new PolicyListVo();
		BeanUtils.copyProperties(policy, policyListVo);
		
		return policyListVo;
	}
	
	public PolicyDetailVo buildDetailVo(Policy policy) {
		PolicyDetailVo policyDetailVo = new PolicyDetailVo();
		BeanUtils.copyProperties(policy, policyDetailVo);
		
		return policyDetailVo;
	}
	
	public PolicyExportVo buildExportVo(Policy policy) {
		PolicyExportVo policyExportVo = new PolicyExportVo();
		BeanUtils.copyProperties(policy, policyExportVo);
		
		policyExportVo.setBirthdayLabel(GcUtils.formatDate(policy.getBirthday(), "yyyy-MM-dd"));
		return policyExportVo;
	}
}
