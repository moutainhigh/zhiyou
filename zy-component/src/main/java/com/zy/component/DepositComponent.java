package com.gc.component;

import com.zy.common.util.BeanUtils;
import com.gc.entity.fnc.Deposit;
import com.gc.entity.usr.User;
import com.zy.util.VoHelper;
import com.gc.vo.DepositAdminVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DepositComponent {
	
	@Autowired
	private CacheComponent cacheComponent;
	
	public DepositAdminVo buildAdminVo(Deposit deposit) {
		DepositAdminVo depositAdminVo = new DepositAdminVo();
		BeanUtils.copyProperties(deposit, depositAdminVo);
		User user = cacheComponent.getUser(deposit.getUserId());
		depositAdminVo.setUser(VoHelper.buildUserAdminSimpleVo(user));
		return depositAdminVo;
	}
	
}
