package com.zy.admin.controller.mal;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.component.OrderComponent;
import com.zy.entity.mal.Order;
import com.zy.entity.mal.Order.LogisticsFeePayType;
import com.zy.entity.mal.Order.OrderStatus;
import com.zy.entity.usr.User;
import com.zy.model.Constants;
import com.zy.model.dto.OrderDeliverDto;
import com.zy.model.query.OrderQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.OrderService;
import com.zy.service.UserService;
import com.zy.vo.OrderAdminVo;


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
            Long[] userIds = users.stream().map(v -> v.getId()).toArray(Long[]::new);
            orderQueryModel.setUserIdIN(userIds);
        }
		
		Page<Order> page = orderService.findPage(orderQueryModel);
		Page<OrderAdminVo> voPage = PageBuilder.copyAndConvert(page, orderComponent::buildAdminVo);
		return new Grid<OrderAdminVo>(voPage);
	}

	@RequiresPermissions("order:view")
	@RequestMapping(value = "/platformDeliverList", method = RequestMethod.GET)
	public String platformDeliverList(Model model) {
		long count = orderService.count(OrderQueryModel.builder().isPlatformDeliverEQ(true).orderStatusEQ(OrderStatus.已支付).build());
		model.addAttribute("count", count);
		return "mal/orderPlatformDeliverList";
	}
	
	@RequiresPermissions("order:view")
	@RequestMapping(value = "/platformDeliverList", method = RequestMethod.POST)
	@ResponseBody
	public Grid<OrderAdminVo> platformDeliverList(OrderQueryModel orderQueryModel, String userPhoneEQ, String userNicknameLK) {
		if (StringUtils.isNotBlank(userPhoneEQ) || StringUtils.isNotBlank(userNicknameLK)) {
        	UserQueryModel userQueryModel = new UserQueryModel();
        	userQueryModel.setPhoneEQ(userPhoneEQ);
        	userQueryModel.setNicknameLK(userNicknameLK);
            List<User> users = userService.findAll(userQueryModel);
            Long[] userIds = users.stream().map(v -> v.getId()).toArray(Long[]::new);
            orderQueryModel.setUserIdIN(userIds);
        }
		orderQueryModel.setIsPlatformDeliverEQ(true);
		
		Page<Order> page = orderService.findPage(orderQueryModel);
		Page<OrderAdminVo> voPage = PageBuilder.copyAndConvert(page, orderComponent::buildAdminVo);
		return new Grid<OrderAdminVo>(voPage);
	}
	
	@RequiresPermissions("order:view")
	@RequestMapping(value = "/detail")
	public String detail(Long id, Model model) {
		Order order = orderService.findOne(id);
		validate(order, NOT_NULL, "order id" + id + " not found");
		model.addAttribute("order", orderComponent.buildAdminFullVo(order));
		return "mal/orderDetail";
	}
	
	@RequiresPermissions("order:deliver")
	@RequestMapping(value = "/deliver", method = RequestMethod.GET)
	public String deliver(@RequestParam Long id, Model model) {
		Order order = orderService.findOne(id);
		validate(order, NOT_NULL, "order id " + id + "not found");
		
		model.addAttribute("order", orderComponent.buildAdminVo(order));
		model.addAttribute("logisticsFeePayTypes", LogisticsFeePayType.values());
		return "mal/orderDeliver";
	}
	
	@RequiresPermissions("order:deliver")
	@RequestMapping(value = "/deliver", method = RequestMethod.POST)
	public String deliver(OrderDeliverDto orderDeliverDto, RedirectAttributes redirectAttributes) {
		try {
			orderService.platformDeliver(orderDeliverDto);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("发货成功"));
			return "redirect:/order/platformDeliverList";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
			return "redirect:/order/deliver?id=" + orderDeliverDto.getId();
		}
	}
}
