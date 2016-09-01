package com.zy.admin.controller.mal;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.ui.Grid;
import com.gc.component.OrderComponent;
import com.gc.entity.mal.Order;
import com.gc.entity.usr.User;
import com.gc.model.query.OrderQueryModel;
import com.gc.model.query.UserQueryModel;
import com.gc.service.OrderService;
import com.gc.service.UserService;
import com.gc.vo.OrderAdminVo;


@RequestMapping("/order")
@Controller
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderComponent orderComponent;
	
	@Autowired
	private UserService userService;
	
	@RequiresPermissions("order:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "mal/orderList";
	}
	
	@RequiresPermissions("order:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<OrderAdminVo> list(OrderQueryModel orderQueryModel, String userPhoneEQ, String userNicknameLK) {

		if (StringUtils.isNotBlank(userPhoneEQ) || StringUtils.isNotBlank(userNicknameLK)) {
        	UserQueryModel userQueryModel = new UserQueryModel();
        	userQueryModel.setPhoneEQ(userPhoneEQ);
        	userQueryModel.setNicknameLK(userNicknameLK);
        	
            List<User> users = userService.findAll(userQueryModel);
            if (users == null || users.size() == 0) {
                return new Grid<OrderAdminVo>(PageBuilder.empty(orderQueryModel.getPageSize(), orderQueryModel.getPageNumber()));
            }
            Long[] userIds = users.stream().map(v -> v.getId()).toArray(Long[]::new);
            orderQueryModel.setUserIdIN(userIds);
        }
		
		Page<Order> page = orderService.findPage(orderQueryModel);
		Page<OrderAdminVo> voPage = PageBuilder.copyAndConvert(page, orderComponent::buildAdminVo);
		return new Grid<OrderAdminVo>(voPage);
	}


}
