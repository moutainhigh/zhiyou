package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.usr.UserTargetSales;
import com.zy.vo.UserTargetSalesVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserTargetSalesComponent {
	
	@Autowired
	private CacheComponent cacheComponent;

	@Autowired
	private UserComponent userComponent;

	public UserTargetSalesVo buildVo(UserTargetSales userTargetSales) {
		UserTargetSalesVo userTargetSalesVo = new UserTargetSalesVo();
		BeanUtils.copyProperties(userTargetSales, userTargetSalesVo);
		if (userTargetSales.getUserId() != null) {
			userTargetSalesVo.setUser(userComponent.buildAdminVo(cacheComponent.getUser(userTargetSales.getUserId())));
		}
		return userTargetSalesVo;
	}

}
