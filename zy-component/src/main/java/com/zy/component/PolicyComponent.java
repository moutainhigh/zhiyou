package com.zy.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.act.Policy;
import com.zy.entity.usr.User;
import com.zy.util.VoHelper;
import com.zy.vo.PolicyAdminVo;
import com.zy.vo.PolicyDetailVo;
import com.zy.vo.PolicyListVo;

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
}
