package com.zy.mobile.controller.ucenter;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

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
import com.zy.model.dto.OrderDeliverDto;
import com.zy.model.query.OrderQueryModel;
import com.zy.service.OrderService;

import io.gd.generator.api.query.Direction;

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
		return "ucenter/order/orderList";
	}
	
	@RequestMapping("/out")
	public String out(Principal principal, Model model) {
		Page<Order> page = orderService.findPage(OrderQueryModel.builder().userIdEQ(principal.getUserId()).orderBy("createdTime").direction(Direction.DESC).build());
		model.addAttribute("page", PageBuilder.copyAndConvert(page, orderComponent::buildListVo));
		return "ucenter/order/orderList";
	}
	
	@RequestMapping("/{sn}")
	public String detail(@PathVariable String sn, Principal principal, Model model) {
		  Order order =  orderService.findBySn(sn);
		  validate(order, NOT_NULL, "order sn" + sn + " not found");
		  model.addAttribute("order", orderComponent.buildDetailVo(order));
		  model.addAttribute("inOut", order.getUserId().equals(principal.getUserId()) ? "in" : "out");
		  return "ucenter/order/orderDetail";  
	}
	
	@RequestMapping(path = "/deliver", method = RequestMethod.GET)
	public String deliver(String sn, Model model) {
		
		return "ucenter/order/orderDeliver";
	}
	
	@RequestMapping(path = "/deliver", method = RequestMethod.POST)
	public String deliver(OrderDeliverDto orderDeliverDto, RedirectAttributes redirectAttributes, Principal principal) {
		
		Long id = orderDeliverDto.getId();
		Order persistence = orderService.findOne(id);
		validate(persistence, NOT_NULL, "order id" + id + " not found");
		if(principal.getUserId().equals(persistence.getSellerId())) {
			throw new UnauthorizedException("权限不足");
		}
		try {
			orderService.deliver(orderDeliverDto);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("发货成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
		}
		
		return "redirect:/order/" + persistence.getSn();
	}
	
	@RequestMapping("/confirmDelivery")
	public String confirmDelivery(Long id, RedirectAttributes redirectAttributes, Principal principal) {
		
		Order persistence = orderService.findOne(id);
		validate(persistence, NOT_NULL, "order id" + id + " not found");
		if(principal.getUserId().equals(persistence.getUserId())) {
			throw new UnauthorizedException("权限不足");
		}
		
		try {
			orderService.receive(id);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("确认收货成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
		}
		
		return "redirect:/order/" + persistence.getSn();
	}
	
}
