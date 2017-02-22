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

import java.util.Date;

import static com.zy.util.GcUtils.getThumbnail;

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
		String image1 = policy.getImage1();
		if (image1 != null) {
			policyAdminVo.setImage1Thumbnail(getThumbnail(image1, 300, 180));
		}
		Date validTimeBegin = policy.getValidTimeBegin();
		if (validTimeBegin != null) {
			policyAdminVo.setValidTimeBeginLabel(GcUtils.formatDate(validTimeBegin, "yyyy-MM-dd"));
		}
		Date validTimeEnd = policy.getValidTimeEnd();
		if (validTimeEnd != null) {
			policyAdminVo.setValidTimeEndLabel(GcUtils.formatDate(validTimeEnd, "yyyy-MM-dd"));
		}
		Policy.PolicyStatus policyStatus = policy.getPolicyStatus();
		if (policyStatus != null) {
			policyAdminVo.setPolicyStatusStyle(GcUtils.getPolicyStatusStyle(policyStatus));
		}
		return policyAdminVo;
	}
	
	public PolicyListVo buildListVo(Policy policy) {
		PolicyListVo policyListVo = new PolicyListVo();
		BeanUtils.copyProperties(policy, policyListVo);

		String image1 = policy.getImage1();
		if (image1 != null) {
			policyListVo.setImage1Thumbnail(getThumbnail(image1, 300, 180));
		}
		return policyListVo;
	}
	
	public PolicyDetailVo buildDetailVo(Policy policy) {
		PolicyDetailVo policyDetailVo = new PolicyDetailVo();
		BeanUtils.copyProperties(policy, policyDetailVo);

		String image1 = policy.getImage1();
		if (image1 != null) {
			policyDetailVo.setImage1Thumbnail(getThumbnail(image1, 300, 180));
		}
		return policyDetailVo;
	}
	
	public PolicyExportVo buildExportVo(Policy policy) {
		PolicyExportVo policyExportVo = new PolicyExportVo();
		BeanUtils.copyProperties(policy, policyExportVo);
		
		policyExportVo.setBirthdayLabel(GcUtils.formatDate(policy.getBirthday(), "yyyy-MM-dd"));
		return policyExportVo;
	}
}
