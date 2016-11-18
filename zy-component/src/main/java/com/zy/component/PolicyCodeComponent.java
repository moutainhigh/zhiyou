package com.zy.component;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.act.PolicyCode;
import com.zy.util.GcUtils;
import com.zy.vo.PolicyCodeAdminVo;
import com.zy.vo.PolicyCodeExportVo;

@Component
public class PolicyCodeComponent {

	private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	public PolicyCodeAdminVo buildAdminVo(PolicyCode policyCode) {
		PolicyCodeAdminVo policyCodeAdminVo = new PolicyCodeAdminVo();
		BeanUtils.copyProperties(policyCode, policyCodeAdminVo);
		
		Date usedTime = policyCode.getUsedTime();
		if(usedTime != null) {
			policyCodeAdminVo.setUsedTimeLabel(GcUtils.formatDate(usedTime, TIME_PATTERN));
		}
		Date createdTime = policyCode.getCreatedTime();
		if(createdTime != null) {
			policyCodeAdminVo.setCreatedTimeLabel(GcUtils.formatDate(createdTime, TIME_PATTERN));
		}
		return policyCodeAdminVo;
	}
	
	public PolicyCodeExportVo buildExportVo(PolicyCode policyCode) {
		PolicyCodeExportVo policyCodeExportVo = new PolicyCodeExportVo();
		BeanUtils.copyProperties(policyCode, policyCodeExportVo);
		
		policyCodeExportVo.setIsUsedLabel(policyCode.getIsUsed()? "已使用" : "");
		return policyCodeExportVo;
	}
}
