package com.gc.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zy.common.util.BeanUtils;
import com.gc.entity.fnc.Profit;
import com.zy.util.VoHelper;
import com.gc.vo.ProfitAdminVo;

@Component
public class ProfitComponent {
	
	@Autowired
	private CacheComponent cacheComponent;
	
	public ProfitAdminVo buildAdminVo(Profit profit) {
		ProfitAdminVo profitAdminVo = new ProfitAdminVo();
		BeanUtils.copyProperties(profit, profitAdminVo);
		profitAdminVo.setUser(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(profit.getUserId())));
		
		return profitAdminVo;
	}
	
}
