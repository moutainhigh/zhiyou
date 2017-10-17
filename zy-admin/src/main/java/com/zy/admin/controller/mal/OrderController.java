package com.zy.admin.controller.mal;

import com.zy.Config;
import com.zy.admin.model.AdminPrincipal;
import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.component.OrderComponent;
import com.zy.entity.fnc.Payment;
import com.zy.entity.fnc.Payment.PaymentStatus;
import com.zy.entity.fnc.Payment.PaymentType;
import com.zy.entity.mal.Order;
import com.zy.entity.mal.Order.OrderStatus;
import com.zy.entity.mal.Product;
import com.zy.entity.usr.User;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.model.dto.OrderDeliverDto;
import com.zy.model.query.OrderQueryModel;
import com.zy.model.query.PaymentQueryModel;
import com.zy.model.query.ProductQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.OrderService;
import com.zy.service.PaymentService;
import com.zy.service.ProductService;
import com.zy.service.UserService;
import com.zy.vo.OrderAdminVo;
import io.gd.generator.api.query.Direction;
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

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;


@RequestMapping("/order")
@Controller
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private OrderComponent orderComponent;
	
	@Autowired
	private UserService userService;

	@Autowired
	private ProductService productService;

	@Autowired
	private Config config;
	
	@RequiresPermissions("order:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		List<Product> products = productService.findAll(ProductQueryModel.builder().build());
		Map<Long, String> productMap = products.stream().collect(Collectors.toMap(Product::getId, v -> v.getTitle()));
		model.addAttribute("productMap", productMap);
		return "mal/orderList";
	}
	
	@RequiresPermissions("order:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<OrderAdminVo> list(OrderQueryModel orderQueryModel, String paidTimeOrderBy, String userPhoneEQ, String userNicknameLK) {

		if (StringUtils.isNotBlank(userPhoneEQ) || StringUtils.isNotBlank(userNicknameLK)) {
        	UserQueryModel userQueryModel = new UserQueryModel();
        	userQueryModel.setPhoneEQ(userPhoneEQ);
        	userQueryModel.setNicknameLK(userNicknameLK);
            List<User> users = userService.findAll(userQueryModel);
            Long[] userIds = users.stream().map(v -> v.getId()).toArray(Long[]::new);
            orderQueryModel.setUserIdIN(userIds);
        }
        if(StringUtils.isNotBlank(paidTimeOrderBy)) {
	        orderQueryModel.setOrderBy("paidTime");
	        orderQueryModel.setDirection(Direction.DESC);
        }
		
		Page<Order> page = orderService.findPage(orderQueryModel);
		Page<OrderAdminVo> voPage = PageBuilder.copyAndConvert(page, orderComponent::buildAdminVo);
		return new Grid<OrderAdminVo>(voPage);
	}

	@RequiresPermissions("order:view")
	@RequestMapping(value = "/platformDeliverList", method = RequestMethod.GET)
	public String platformDeliverList(Model model) {
		Long sysUserId = config.getSysUserId();
		long count = orderService.count(OrderQueryModel.builder().sellerIdEQ(sysUserId).orderStatusEQ(OrderStatus.已支付).build());
		model.addAttribute("count", count);
		model.addAttribute("orderStatuses", OrderStatus.values());
		List<Product> products = productService.findAll(ProductQueryModel.builder().build());
		Map<Long, String> productMap = products.stream().collect(Collectors.toMap(Product::getId, v -> v.getTitle()));
		model.addAttribute("productMap", productMap);
		return "mal/orderPlatformDeliverList";
	}
	
	@RequiresPermissions("order:view")
	@RequestMapping(value = "/platformDeliverList", method = RequestMethod.POST)
	@ResponseBody
	public Grid<OrderAdminVo> platformDeliverList(OrderQueryModel orderQueryModel, String paidTimeOrderBy, String userPhoneEQ, String userNicknameLK) {
		Long sysUserId = config.getSysUserId();
		if (StringUtils.isNotBlank(userPhoneEQ) || StringUtils.isNotBlank(userNicknameLK)) {
        	UserQueryModel userQueryModel = new UserQueryModel();
        	userQueryModel.setPhoneEQ(userPhoneEQ);
        	userQueryModel.setNicknameLK(userNicknameLK);
            List<User> users = userService.findAll(userQueryModel);
            Long[] userIds = users.stream().map(v -> v.getId()).toArray(Long[]::new);
            orderQueryModel.setUserIdIN(userIds);
        }
		orderQueryModel.setSellerIdEQ(sysUserId);

		if(StringUtils.isNotBlank(paidTimeOrderBy)) {
			orderQueryModel.setOrderBy("paidTime");
			orderQueryModel.setDirection(Direction.DESC);
		}

		Page<Order> page = orderService.findPage(orderQueryModel);
		Page<OrderAdminVo> voPage = PageBuilder.copyAndConvert(page, orderComponent::buildAdminVo);
		return new Grid<OrderAdminVo>(voPage);
	}
	
	@RequiresPermissions("order:view")
	@RequestMapping(value = "/detail")
	public String detail(Long id, Model model, Boolean isPure) {
		Order order = orderService.findOne(id);
		validate(order, NOT_NULL, "order id" + id + " not found");
		model.addAttribute("isPure", isPure == null ? false : isPure);
		model.addAttribute("order", orderComponent.buildAdminFullVo(order));
		long count = paymentService.count(PaymentQueryModel.builder().refIdEQ(id).paymentTypeEQ(PaymentType.订单支付).paymentStatusEQ(PaymentStatus.已支付).build());
		model.addAttribute("count", count);
		return "mal/orderDetail";
	}
	
	@RequiresPermissions("order:deliver")
	@RequestMapping(value = "/deliver", method = RequestMethod.GET)
	public String deliver(@RequestParam Long id, Model model) {
		Order order = orderService.findOne(id);
		validate(order, NOT_NULL, "order id " + id + "not found");
		
		model.addAttribute("order", orderComponent.buildAdminVo(order));
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
	
	@RequestMapping(value = "/sum", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> sum(OrderQueryModel orderQueryModel, String userPhoneEQ, String userNicknameLK, Boolean isPlatformDeliver) {
		
		if (StringUtils.isNotBlank(userPhoneEQ) || StringUtils.isNotBlank(userNicknameLK)) {
        	UserQueryModel userQueryModel = new UserQueryModel();
        	userQueryModel.setPhoneEQ(userPhoneEQ);
        	userQueryModel.setNicknameLK(userNicknameLK);
            List<User> users = userService.findAll(userQueryModel);
            Long[] userIds = users.stream().map(v -> v.getId()).toArray(Long[]::new);
            orderQueryModel.setUserIdIN(userIds);
        }
		if(isPlatformDeliver != null && isPlatformDeliver) {
			orderQueryModel.setSellerIdEQ(config.getSysUserId());
		}
		return ResultBuilder.result(orderService.sum(orderQueryModel));
	}
	
	@RequiresPermissions("order:refund")
	@RequestMapping(value = "/refund")
	public String refund(@NotNull Long paymentId, RedirectAttributes redirectAttributes) {
		Payment payment = paymentService.findOne(paymentId);
		validate(payment, NOT_NULL, "payment id" + paymentId + " not found");
		validate(payment, v -> (v.getPaymentStatus() == PaymentStatus.已支付 && v.getPaymentType() == PaymentType.订单支付), "paymentStatus and paymentType is error, payment id is " + paymentId + "");
		Order order = orderService.findOne(payment.getRefId());
		validate(order, v -> v.getOrderStatus() == OrderStatus.已支付, "orderStatus is not paid");
		
		long count = paymentService.count(PaymentQueryModel.builder().refIdEQ(payment.getRefId()).paymentTypeEQ(PaymentType.订单支付).paymentStatusEQ(PaymentStatus.已支付).build());
		if(count > 1){
			paymentService.refund(paymentId);
			redirectAttributes.addFlashAttribute(ResultBuilder.ok("退款成功！"));
		} else {
			redirectAttributes.addFlashAttribute(ResultBuilder.error("退款失败，订单退款仅支持重复支付退款！"));
		}
		return "redirect:/order";
	}

	/* 只能查看已支付的订单并发货 */
	@RequiresPermissions("order:deliver")
	@RequestMapping( value="/paid", method = RequestMethod.GET)
	public String paidList(Model model) {
		List<Product> products = productService.findAll(ProductQueryModel.builder().build());
		Map<Long, String> productMap = products.stream().collect(Collectors.toMap(Product::getId, v -> v.getTitle()));
		model.addAttribute("productMap", productMap);
		return "mal/orderPaidList";
	}

	@RequiresPermissions("order:deliver")
	@RequestMapping(value="/paid", method = RequestMethod.POST)
	@ResponseBody
	public Grid<OrderAdminVo> paidList(OrderQueryModel orderQueryModel, String paidTimeOrderBy, String userPhoneEQ, String userNicknameLK) {

		if (StringUtils.isNotBlank(userPhoneEQ) || StringUtils.isNotBlank(userNicknameLK)) {
			UserQueryModel userQueryModel = new UserQueryModel();
			userQueryModel.setPhoneEQ(userPhoneEQ);
			userQueryModel.setNicknameLK(userNicknameLK);
			List<User> users = userService.findAll(userQueryModel);
			Long[] userIds = users.stream().map(v -> v.getId()).toArray(Long[]::new);
			orderQueryModel.setUserIdIN(userIds);
		}
		if(StringUtils.isNotBlank(paidTimeOrderBy)) {
			orderQueryModel.setOrderBy("paidTime");
			orderQueryModel.setDirection(Direction.DESC);
		}
		orderQueryModel.setOrderStatusEQ(OrderStatus.已支付);
		Page<Order> page = orderService.findPage(orderQueryModel);
		Page<OrderAdminVo> voPage = PageBuilder.copyAndConvert(page, orderComponent::buildAdminVo);
		return new Grid<>(voPage);
	}

	@RequiresPermissions("order:deliver")
	@RequestMapping(value = "/paid/deliver", method = RequestMethod.GET)
	public String paidDeliver(@RequestParam Long id, Model model) {
		Order order = orderService.findOne(id);
		validate(order, NOT_NULL, "order id " + id + "not found");

		model.addAttribute("order", orderComponent.buildAdminVo(order));
		return "mal/orderPaidDeliver";
	}

	@RequiresPermissions("order:deliver")
	@RequestMapping(value = "/paid/deliver", method = RequestMethod.POST)
	public String paidDeliver(OrderDeliverDto orderDeliverDto, RedirectAttributes redirectAttributes, AdminPrincipal principal) {
		try {
			orderDeliverDto.setDeliveredId(principal.getUserId());
			orderService.platformDeliver(orderDeliverDto);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("发货成功"));
			return "redirect:/order/paid";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
			return "redirect:/order/paid/deliver?id=" + orderDeliverDto.getId();
		}
	}
}
