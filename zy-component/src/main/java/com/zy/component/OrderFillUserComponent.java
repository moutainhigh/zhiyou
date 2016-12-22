package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.mal.OrderFillUser;
import com.zy.util.VoHelper;
import com.zy.vo.OrderFillUserAdminVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderFillUserComponent {

	@Autowired
	private CacheComponent cacheComponent;

	public OrderFillUserAdminVo buildAdminVo(OrderFillUser orderFillUser) {
		OrderFillUserAdminVo orderFillUserAdminVo = new OrderFillUserAdminVo();
		BeanUtils.copyProperties(orderFillUser, orderFillUserAdminVo);

		orderFillUserAdminVo.setUser(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(orderFillUser.getUserId())));
		return orderFillUserAdminVo;
	}

}
