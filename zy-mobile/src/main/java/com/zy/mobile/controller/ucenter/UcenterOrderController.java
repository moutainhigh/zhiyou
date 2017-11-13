package com.zy.mobile.controller.ucenter;

import com.zy.Config;
import com.zy.common.exception.BizException;
import com.zy.common.exception.UnauthorizedException;
import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.ResultBuilder;
import com.zy.component.OrderComponent;
import com.zy.component.UserComponent;
import com.zy.entity.fnc.PayType;
import com.zy.entity.fnc.Profit;
import com.zy.entity.mal.Order;
import com.zy.entity.mal.Order.OrderStatus;
import com.zy.entity.mal.OrderItem;
import com.zy.entity.mal.Product;
import com.zy.entity.usr.User;
import com.zy.model.BizCode;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.model.dto.OrderDeliverDto;
import com.zy.model.query.OrderQueryModel;
import com.zy.model.query.ProfitQueryModel;
import com.zy.service.*;
import io.gd.generator.api.query.Direction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static com.zy.common.util.ValidateUtils.*;
import static com.zy.model.Constants.SETTING_NEW_MIN_QUANTITY;
import static com.zy.model.Constants.SETTING_OLD_MIN_QUANTITY;

@RequestMapping("/u/order")
@Controller
public class UcenterOrderController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderItemService orderItemService;

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProfitService profitService;

	@Autowired
	private Config config;

	@Autowired
	private OrderComponent orderComponent;

	@Autowired
	private UserComponent userComponent;
	
	@RequestMapping("/in")
	public String in(Principal principal, Model model, OrderStatus orderStatus) {
		OrderQueryModel orderQueryModel = OrderQueryModel.builder().userIdEQ(principal.getUserId())
				.orderBy("createdTime").direction(Direction.DESC).isDeletedEQ(false)
				.pageSize(50).build();
		orderQueryModel.setOrderStatusEQ(orderStatus);
		Page<Order> page = orderService.findPage(orderQueryModel);
		model.addAttribute("page", PageBuilder.copyAndConvert(page, orderComponent::buildListVo));
		model.addAttribute("inOut", "in");
		model.addAttribute("orderStatus", orderStatus);
		orderQueryModel.setOrderStatusEQ(null);
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
		OrderQueryModel orderQueryModel = OrderQueryModel.builder().sellerIdEQ(principal.getUserId())
				.orderBy("createdTime").direction(Direction.DESC).isDeletedEQ(false)
				.pageSize(50).build();
		orderQueryModel.setOrderStatusEQ(orderStatus);
		Page<Order> page = orderService.findPage(orderQueryModel);
		model.addAttribute("page", PageBuilder.copyAndConvert(page, orderComponent::buildListVo));
		model.addAttribute("inOut", "out");
		model.addAttribute("orderStatus", orderStatus);
		orderQueryModel.setOrderStatusEQ(null);
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
		if (!principal.getUserId().equals(order.getSellerId()) && !principal.getUserId().equals(order.getUserId())) {
			throw new UnauthorizedException("权限不足");
		}
		OrderItem persistentOrderItem = orderItemService.findByOrderId(id).get(0);
		int minQuantity = 0;
		Long productId = persistentOrderItem.getProductId();
		Product product = productService.findOne(productId);
		boolean canCopy = false;
		if (product.getProductType() == 1){
			minQuantity = config.isOld(productId) ? SETTING_OLD_MIN_QUANTITY : SETTING_NEW_MIN_QUANTITY;
		}

		model.addAttribute("inOut", order.getUserId().equals(principal.getUserId()) ? "in" : "out");
		User buyer = userService.findOne(order.getUserId());
		model.addAttribute("buyer", userComponent.buildListVo(buyer));
		User seller = userService.findOne(order.getSellerId());
		model.addAttribute("seller", userComponent.buildListVo(seller));

		long quantity = persistentOrderItem.getQuantity();
		
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

		if (product.getProductType() == 1) {
			if (order.getSellerUserRank() == User.UserRank.V4 && quantity >= minQuantity && quantity % minQuantity == 0) {
				canCopy = true;
			}
		}
		model.addAttribute("canCopy", canCopy);

		return "ucenter/order/orderDetail";
	}

	@RequestMapping(path = "/pay/{orderId}", method = RequestMethod.GET)
	public String orderPay(@PathVariable Long orderId, @RequestParam(required = true) PayType payType, Model model,
			Principal principal) {
		validate(payType, NOT_NULL, "order payType" + payType + " is null");
		Order order = orderService.findOne(orderId);
		validate(order, NOT_NULL, "order id" + orderId + " not found");
		if(!order.getUserId().equals(principal.getUserId())){
			throw new UnauthorizedException("权限不足");
		}
		if(order.getOrderStatus() != OrderStatus.待支付){
			return "redirect:/u/order/" + orderId;
		}

		model.addAttribute("title", order.getTitle());
		model.addAttribute("sn", order.getSn());
		model.addAttribute("amount", order.getAmount());
		model.addAttribute("payType", payType);
		if (payType == PayType.银行汇款) {
			model.addAttribute("id", order.getId());
			return "ucenter/pay/payOffline";
		} else {
			throw new BizException(BizCode.ERROR, "不支持的付款方式");
		}
	}
	
	@RequestMapping(path = "/pay", method = RequestMethod.POST)
	public String orderPay(Long id, String offlineImage, String offlineMemo, RedirectAttributes redirectAttributes, Principal principal) {
		validate(id, NOT_NULL, "order id " + id + " is null");
		Order order = orderService.findOne(id);
		validate(order, NOT_NULL, "order id" + id + " not found");
		if (!principal.getUserId().equals(order.getUserId())) {
			throw new UnauthorizedException("权限不足");
		}

		validate(offlineImage, NOT_BLANK, "order offlineImage is blank");
		validate(offlineMemo, NOT_BLANK, "order offlineMemo is blank");
		orderService.offlinePay(id, offlineImage, offlineMemo);
		redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("转账汇款凭证提交成功，请等待确认"));
		return "redirect:/u/order/" + id;
	}
	
	@RequestMapping("/confirmPay")
	public String confirmPay(Long id, RedirectAttributes redirectAttributes, Principal principal) {
		Order persistence = orderService.findOne(id);
		validate(persistence, NOT_NULL, "order id" + id + " not found");

		//校验库存
		if (persistence.getBuyerUserRank() != User.UserRank.V4 && persistence.getQuantity() < 2000){
			if (persistence.getProductType() == 2){
				Boolean  flag = orderService.checkOrderStore(persistence.getSellerId(), 2, persistence.getQuantity());
				if (flag == false){
					redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("您的库存已不足，请及时进货"));
					return "redirect:/u/order/" + persistence.getId();
//					throw new BizException(BizCode.ERROR, "您的库存已不足，请及时进货");
				}
			}
		}

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
	
	@RequestMapping("/rejectPay")
	public String rejectPay(Long id, RedirectAttributes redirectAttributes, Principal principal) {
		Order persistence = orderService.findOne(id);
		validate(persistence, NOT_NULL, "order id" + id + " not found");
		if (!principal.getUserId().equals(persistence.getSellerId())) {
			throw new UnauthorizedException("权限不足");
		}
		try {
			orderService.rejectPay(id, null);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("驳回支付成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
		}
		return "redirect:/u/order/" + persistence.getId();
	}
	
	@RequestMapping(path = "/deliver", method = RequestMethod.GET)
	public String deliver(Long id, Model model, Principal principal) {
		Order persistence = orderService.findOne(id);
		//校验库存
		if (persistence.getBuyerUserRank() != User.UserRank.V4 && persistence.getQuantity() < 2000){
			if (persistence.getProductType() == 2){
				Boolean  flag = orderService.checkOrderStore(persistence.getSellerId(), 2, persistence.getQuantity());
				if (flag == false){
					throw new BizException(BizCode.ERROR, "您的库存已不足，请及时进货");
				}
			}
		}

		validate(persistence, NOT_NULL, "order id" + id + " not found");
		if (!principal.getUserId().equals(persistence.getSellerId())) {
			throw new UnauthorizedException("权限不足");
		}
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
		Long newOrderId = null;
		try {
			newOrderId = orderService.copy(id);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
			return "redirect:/u/order/" + id;
		}
		if(order.getIsPayToPlatform()){
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("已成功转给公司发货，无需支付"));
		} else {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("已成功转给公司发货，请尽快支付"));
		}
		return "redirect:/u/order/" + newOrderId;
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
		return "redirect:/u/order/in";
	}
	
	@RequestMapping("/cancel")
	public String cancel(Long id, RedirectAttributes redirectAttributes, Principal principal) {
		Order persistence = orderService.findOne(id);
		validate(persistence, NOT_NULL, "order id" + id + " not found");
		if (!principal.getUserId().equals(persistence.getUserId())) {
			throw new UnauthorizedException("权限不足");
		}
		try {
			orderService.cancel(id);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("取消订单成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
		}
		return "redirect:/u/order/in";
	}
	
}
