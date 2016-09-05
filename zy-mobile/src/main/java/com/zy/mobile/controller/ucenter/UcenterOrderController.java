package com.zy.mobile.controller.ucenter;

import static com.zy.common.util.ValidateUtils.NOT_BLANK;
import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;
import io.gd.generator.api.query.Direction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.exception.UnauthorizedException;
import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.ResultBuilder;
import com.zy.component.OrderComponent;
import com.zy.entity.mal.Order;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.model.query.OrderQueryModel;
import com.zy.service.OrderService;

@RequestMapping("/u/order")
@Controller
public class UcenterOrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderComponent orderComponent;
	
	
	@RequestMapping("/in")
	public String in(Principal principal, Model model) {
		Page<Order> page = orderService.findPage(OrderQueryModel.builder().sellerIdEQ(principal.getUserId()).orderBy("createdTime").direction(Direction.DESC).build());
		model.addAttribute("page", PageBuilder.copyAndConvert(page, orderComponent::buildListVo));
		return "order/orderList";
	}
	
	@RequestMapping("/out")
	public String out(Principal principal, Model model) {
		Page<Order> page = orderService.findPage(OrderQueryModel.builder().userIdEQ(principal.getUserId()).orderBy("createdTime").direction(Direction.DESC).build());
		model.addAttribute("page", PageBuilder.copyAndConvert(page, orderComponent::buildListVo));
		return "order/orderList";
	}
	
	@RequestMapping("/{sn}")
	public String detail(@PathVariable String sn, Model model) {
		  Order order =  orderService.findBySn(sn);
		  validate(order, NOT_NULL, "order sn" + sn + " not found");
		  model.addAttribute("order", order);
		  return "order/orderDetail";  
	}
	
	@RequestMapping(path = "/deliver", method = RequestMethod.GET)
	public String deliver(String sn, Model model) {
		
		return "order/orderDeliver";
	}
	
	@RequestMapping(path = "/deliver", method = RequestMethod.POST)
	public String deliver(Order order, RedirectAttributes redirectAttributes, Principal principal) {
		
		String sn = order.getSn();
		validate(sn, NOT_BLANK, "sn is null");
		Order persistence = orderService.findBySn(sn);
		validate(persistence, NOT_NULL, "order sn" + sn + " not found");
		if(principal.getUserId().equals(persistence.getSellerId())) {
			throw new UnauthorizedException("权限不足");
		}
		try {
			orderService.deliver(order);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("发货成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
		}
		
		return "redirect:/order/" + order.getSn();
	}
	
	@RequestMapping("/confirmDelivery")
	public String confirmDelivery(String sn, RedirectAttributes redirectAttributes, Principal principal) {
		
		validate(sn, NOT_BLANK, "sn is null");
		Order persistence = orderService.findBySn(sn);
		validate(persistence, NOT_NULL, "order sn" + sn + " not found");
		if(principal.getUserId().equals(persistence.getUserId())) {
			throw new UnauthorizedException("权限不足");
		}
		
		try {
			orderService.confirmDelivery(sn);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("确认收货成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
		}
		
		return "redirect:/order/" + sn;
	}
	
}
