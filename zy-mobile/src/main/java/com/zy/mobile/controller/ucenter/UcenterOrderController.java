package com.zy.mobile.controller.ucenter;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.exception.UnauthorizedException;
import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.ResultBuilder;
import com.zy.component.OrderComponent;
import com.zy.entity.fnc.PayType;
import com.zy.entity.mal.Order;
import com.zy.entity.mal.Order.OrderStatus;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.model.dto.OrderDeliverDto;
import com.zy.model.query.OrderQueryModel;
import com.zy.service.OrderService;
import com.zy.service.PaymentService;

import io.gd.generator.api.query.Direction;

@RequestMapping("/u/order")
@Controller
public class UcenterOrderController {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private OrderComponent orderComponent;
	
	
	@RequestMapping("/in")
	public String in(Principal principal, Model model, OrderStatus orderStatus) {
		OrderQueryModel orderQueryModel = OrderQueryModel.builder().userIdEQ(principal.getUserId()).orderBy("createdTime").direction(Direction.DESC).build();
		orderQueryModel.setOrderStatusEQ(orderStatus);
		Page<Order> page = orderService.findPage(orderQueryModel);
		model.addAttribute("page", PageBuilder.copyAndConvert(page, orderComponent::buildListVo));
		model.addAttribute("inOut", "in");
		model.addAttribute("orderStatus", orderStatus);
		orderQueryModel.setOrderStatusEQ(OrderStatus.待支付);
		model.addAttribute("waitForPayConut", orderService.count(orderQueryModel));
		orderQueryModel.setOrderStatusEQ(OrderStatus.已支付);
		model.addAttribute("waitForDeliverConut", orderService.count(orderQueryModel));
		orderQueryModel.setOrderStatusEQ(OrderStatus.已发货);
		model.addAttribute("waitForReceiveConut", orderService.count(orderQueryModel));
		return "ucenter/order/orderList";
	}
	
	@RequestMapping("/out")
	public String out(Principal principal, Model model, OrderStatus orderStatus) {
		OrderQueryModel orderQueryModel = OrderQueryModel.builder().sellerIdEQ(principal.getUserId()).orderBy("createdTime").direction(Direction.DESC).build();
		orderQueryModel.setOrderStatusEQ(orderStatus);
		Page<Order> page = orderService.findPage(orderQueryModel);
		model.addAttribute("page", PageBuilder.copyAndConvert(page, orderComponent::buildListVo));
		model.addAttribute("inOut", "out");
		model.addAttribute("orderStatus", orderStatus);
		orderQueryModel.setOrderStatusEQ(OrderStatus.待支付);
		model.addAttribute("waitForPayConut", orderService.count(orderQueryModel));
		orderQueryModel.setOrderStatusEQ(OrderStatus.已支付);
		model.addAttribute("waitForDeliverConut", orderService.count(orderQueryModel));
		orderQueryModel.setOrderStatusEQ(OrderStatus.已发货);
		model.addAttribute("waitForReceiveConut", orderService.count(orderQueryModel));
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
	
	@RequestMapping(path = "pay/{sn}", method = RequestMethod.GET)
	public String pay(@PathVariable String sn, @RequestParam(required = true) PayType payType, Model model) {
		  validate(payType, NOT_NULL, "order payType" + payType + " is null");
		  Order order =  orderService.findBySn(sn);
		  validate(order, NOT_NULL, "order sn" + sn + " not found");
		  model.addAttribute("order", orderComponent.buildDetailVo(order));
		  if(payType == PayType.银行汇款){
			  return "ucenter/order/payOffline";
		  } else {
			  return "ucenter/order/payBalance";
		  }
	}
	
	@RequestMapping(path = "pay/{sn}", method = RequestMethod.POST)
	public String pay(@PathVariable String sn, @RequestParam(required = true) PayType payType, Principal principal, RedirectAttributes redirectAttributes) {
		validate(payType, NOT_NULL, "order payType" + payType + " is null");
		Order order =  orderService.findBySn(sn);
		validate(order, NOT_NULL, "order sn" + sn + " not found");
		  
		return "redirect:/order/" + order.getSn();
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
