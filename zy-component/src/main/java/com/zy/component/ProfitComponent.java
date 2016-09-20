package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.fnc.Profit;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.ProfitAdminVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.zy.util.GcUtils.formatDate;

@Component
public class ProfitComponent {
	
	@Autowired
	private CacheComponent cacheComponent;
	
	private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	public ProfitAdminVo buildAdminVo(Profit profit) {
		ProfitAdminVo profitAdminVo = new ProfitAdminVo();
		BeanUtils.copyProperties(profit, profitAdminVo);

		profitAdminVo.setUser(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(profit.getUserId())));
		profitAdminVo.setCreatedTimeLabel(formatDate(profit.getCreatedTime(), TIME_PATTERN));
		profitAdminVo.setGrantedTimeLabel(formatDate(profit.getGrantedTime(), TIME_PATTERN));
		profitAdminVo.setProfitStatusStyle(GcUtils.getProfitStatusStyle(profit.getProfitStatus()));

		profitAdminVo.setAmountLabel(GcUtils.formatCurreny(profit.getAmount()));

		return profitAdminVo;
	}
	
}
