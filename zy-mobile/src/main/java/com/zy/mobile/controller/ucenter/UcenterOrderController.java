package com.zy.mobile.controller.ucenter;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

import java.util.List;

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
import com.zy.component.UserComponent;
import com.zy.entity.fnc.Profit;
import com.zy.entity.mal.Order;
import com.zy.entity.mal.Order.OrderStatus;
import com.zy.entity.usr.User;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.model.dto.OrderDeliverDto;
import com.zy.model.query.OrderQueryModel;
import com.zy.model.query.ProfitQueryModel;
import com.zy.service.OrderService;
import com.zy.service.ProfitService;
import com.zy.service.UserService;

import io.gd.generator.api.query.Direction;

@RequestMapping("/u/order")
@Controller
public class UcenterOrderController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ProfitService profitService;

	@Autowired
	private OrderComponent orderComponent;

	@Autowired
	private UserComponent userComponent;
	
	@RequestMapping("/in")
	public String in(Principal principal, Model model, OrderStatus orderStatus) {
		OrderQueryModel orderQueryModel = OrderQueryModel.builder().userIdEQ(principal.getUserId()).orderBy("createdTime").direction(Direction.DESC).build();
		orderQueryModel.setOrderStatusEQ(orderStatus);
		Page<Order> page = orderService.findPage(orderQueryModel);
		model.addAttribute("page", PageBuilder.copyAndConvert(page, orderComponent::buildListVo));
		model.addAttribute("inOut", "in");
		model.addAttribute("orderStatus", orderStatus);
		model.addAttribute("orderCount", orderService.count(orderQueryModel));
		orderQueryModel.setOrderStatusEQ(OrderStatus.待支付);
		model.addAttribute("orderCount0", orderService.count(orderQueryModel));
		orderQueryModel.setOrderStatusEQ(OrderStatus.待确认);
		model.addAttribute("orderCount1", orderService.count(orderQueryModel));
		orderQueryModel.setOrderStatusEQ(OrderStatus.已支付);
		model.addAttribute("orderCount2", orderService.count(orderQueryModel));
		orderQueryModel.setOrderStatusEQ(OrderStatus.已发货);
		model.addAttribute("orderCount3", orderService.count(orderQueryModel));
		orderQueryModel.setOrderStatusEQ(OrderStatus.已完成);
		model.addAttribute("orderCount4", orderService.count(orderQueryModel));
		orderQueryModel.setOrderStatusEQ(OrderStatus.已退款);
		model.addAttribute("orderCount5", orderService.count(orderQueryModel));
		orderQueryModel.setOrderStatusEQ(OrderStatus.已取消);
		model.addAttribute("orderCount6", orderService.count(orderQueryModel));
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
		model.addAttribute("orderCount", orderService.count(orderQueryModel));
		orderQueryModel.setOrderStatusEQ(OrderStatus.待支付);
		model.addAttribute("orderCount0", orderService.count(orderQueryModel));
		orderQueryModel.setOrderStatusEQ(OrderStatus.待确认);
		model.addAttribute("orderCount1", orderService.count(orderQueryModel));
		orderQueryModel.setOrderStatusEQ(OrderStatus.已支付);
		model.addAttribute("orderCount2", orderService.count(orderQueryModel));
		orderQueryModel.setOrderStatusEQ(OrderStatus.已发货);
		model.addAttribute("orderCount3", orderService.count(orderQueryModel));
		orderQueryModel.setOrderStatusEQ(OrderStatus.已完成);
		model.addAttribute("orderCount4", orderService.count(orderQueryModel));
		orderQueryModel.setOrderStatusEQ(OrderStatus.已退款);
		model.addAttribute("orderCount5", orderService.count(orderQueryModel));
		orderQueryModel.setOrderStatusEQ(OrderStatus.已取消);
		model.addAttribute("orderCount6", orderService.count(orderQueryModel));
		return "ucenter/order/orderList";
	}

	@RequestMapping("/{id}")
	public String detail(@PathVariable Long id, Principal principal, Model model) {
		Order order = orderService.findOne(id);
		validate(order, NOT_NULL, "order id" + id + " not found");
		
		model.addAttribute("inOut", order.getUserId().equals(principal.getUserId()) ? "in" : "out");
		User buyer = userService.findOne(order.getUserId());
		model.addAttribute("buyer", userComponent.buildListVo(buyer));
		User seller = userService.findOne(order.getSellerId());
		model.addAttribute("seller", userComponent.buildListVo(seller));
		
		if(order.getIsSettledUp()) {
			List<Profit> profits = profitService.findAll(
					ProfitQueryModel.builder()
					.profitTypeIN(new Profit.ProfitType[] {Profit.ProfitType.特级平级奖, Profit.ProfitType.订单收款, Profit.ProfitType.销量奖})
					.refIdEQ(order.getId())
					.userIdEQ(principal.getUserId())
					.build());
			model.addAttribute("profits", profits);
		}
		model.addAttribute("order", orderComponent.buildDetailVo(order));
		return "ucenter/order/orderDetail";
	}

	@RequestMapping("/confirmPay")
	public String confirmPay(Long id, RedirectAttributes redirectAttributes, Principal principal) {
		Order persistence = orderService.findOne(id);
		validate(persistence, NOT_NULL, "order id" + id + " not found");
		if (!principal.getUserId().equals(persistence.getSellerId())) {
			throw new UnauthorizedException("权限不足");
		}
		try {
			orderService.confirmPay(id);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("确认支付成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
		}
		return "redirect:/u/order/" + persistence.getId();
	}
	
	@RequestMapping(path = "/deliver", method = RequestMethod.GET)
	public String deliver(Long id, Model model) {
		model.addAttribute("orderId", id);
		return "ucenter/order/orderDeliver";
	}

	@RequestMapping(path = "/deliver", method = RequestMethod.POST)
	public String deliver(OrderDeliverDto orderDeliverDto, RedirectAttributes redirectAttributes, Principal principal) {

		Long id = orderDeliverDto.getId();
		Order persistence = orderService.findOne(id);
		validate(persistence, NOT_NULL, "order id" + id + " not found");
		if (!principal.getUserId().equals(persistence.getSellerId())) {
			throw new UnauthorizedException("权限不足");
		}
		try {
			orderService.deliver(orderDeliverDto);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("发货成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
		}

		return "redirect:/u/order/" + persistence.getId();
	}

	@RequestMapping("/confirmDelivery")
	public String confirmDelivery(Long id, RedirectAttributes redirectAttributes, Principal principal) {

		Order persistence = orderService.findOne(id);
		validate(persistence, NOT_NULL, "order id" + id + " not found");
		if (!principal.getUserId().equals(persistence.getUserId())) {
			throw new UnauthorizedException("权限不足");
		}

		try {
			orderService.receive(id);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("确认收货成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
		}

		return "redirect:/u/order/" + persistence.getId();
	}
	
	@RequestMapping("/platformDeliver")
	public String platformDeliver(Long id, Principal principal, RedirectAttributes redirectAttributes) {
		Order order = orderService.findOne(id);
		validate(order, NOT_NULL, "order id" + id + " not found");
		if(!principal.getUserId().equals(order.getSellerId())) {
			throw new UnauthorizedException("权限不足");
		}
		try {
			orderService.modifyIsPlatformDeliver(id, true);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("已成功转给公司发货"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
		}
		return "redirect:/u/order/" + order.getId();
	}
	
	@RequestMapping("/delete")
	public String delete(Long id, RedirectAttributes redirectAttributes, Principal principal) {
		Order persistence = orderService.findOne(id);
		validate(persistence, NOT_NULL, "order id" + id + " not found");
		if (!principal.getUserId().equals(persistence.getUserId())) {
			throw new UnauthorizedException("权限不足");
		}
		try {
			orderService.delete(id);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("删除订单成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
		}
		return "redirect:/u/order/" + persistence.getId();
	}
	
}
