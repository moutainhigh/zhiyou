package com.zy.mobile.controller.ucenter;

import com.zy.Config;
import com.zy.common.model.result.ResultBuilder;
import com.zy.component.ProductComponent;
import com.zy.component.UserComponent;
import com.zy.entity.mal.Order;
import com.zy.entity.mal.OrderFillUser;
import com.zy.entity.mal.Product;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.usr.Address;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserInfo;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.model.dto.OrderCreateDto;
import com.zy.model.query.OrderFillUserQueryModel;
import com.zy.service.*;
import com.zy.vo.ProductListVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@RequestMapping("/u/order")
@Controller
public class UcenterOrderCreateController {

	Logger logger = LoggerFactory.getLogger(UcenterOrderCreateController.class);

	@Autowired
	private AddressService addressService;
	
	@Autowired
	private OrderService orderService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private ProductService productService;

	@Autowired
	private OrderFillUserService orderFillUserService;

	@Autowired
	private Config config;

	@Autowired
	private ProductComponent productComponent;
	
	@Autowired
	private UserComponent userComponent;

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(@RequestParam Long productId, @RequestParam Long quantity, Boolean isPayToPlatform, Model model, Principal principal) {

		Long userId = principal.getUserId();
		User user = userService.findOne(userId);
		User.UserRank userRank = user.getUserRank();

		UserInfo userInfo = userInfoService.findByUserId(principal.getUserId());
		if(userInfo != null && userInfo.getConfirmStatus() == ConfirmStatus.已通过) {
			model.addAttribute("hasUserInfo", true);
		}

		Address address = addressService.findDefaultByUserId(userId);
		Product product = productService.findOne(productId);
		product.setPrice(productService.getPrice(productId, user.getUserRank(), quantity));
		validate(product, NOT_NULL, "product id:" + productId + " is not found !");
		ProductListVo productVo = productComponent.buildListVo(product);
		model.addAttribute("product", productVo);
		model.addAttribute("quantity", quantity);
		model.addAttribute("address", address);
		model.addAttribute("userRank", userRank);

		if (userRank == User.UserRank.V0) {
			Long parentId = user.getParentId();
			Long inviterId = user.getInviterId();
			if (parentId != null) {
				User parent = userService.findOne(parentId);
				if (parent != null) {
					model.addAttribute("parent", userComponent.buildListVo(parent));
				}
			}
			if (inviterId != null) {
				User inviter = userService.findOne(inviterId);
				if (inviter != null) {
					model.addAttribute("inviter", userComponent.buildListVo(inviter));
				}
			}
		}

		boolean orderFill = config.isOpenOrderFill();
		if(orderFill) {
			List<OrderFillUser> all = orderFillUserService.findAll(OrderFillUserQueryModel.builder().userIdEQ(userId).build());
			if(all.isEmpty()) {
				orderFill = false;
			} else {
				orderFill = true;
			}
		}
		model.addAttribute("orderFill", orderFill);
		return "ucenter/order/orderCreate";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(OrderCreateDto orderCreateDto, boolean isOrderFill, Long parentId, Principal principal, RedirectAttributes redirectAttributes) {
		
		orderCreateDto.setUserId(principal.getUserId());
		orderCreateDto.setParentId(parentId);
		if(isOrderFill) {
			orderCreateDto.setOrderType(Order.OrderType.补单);
		} else {
			orderCreateDto.setOrderType(Order.OrderType.普通订单);
		}
		Order order = null;
		try {
			order = orderService.create(orderCreateDto);
		} catch (Exception e) {
			logger.error("下单错误", e);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("下单失败, 原因: " + e.getMessage()));
			return "redirect:/u/order/create?productId=" + orderCreateDto.getProductId() + "&quantity=" + orderCreateDto.getQuantity() + (orderCreateDto.getParentId() == null ? "" : ("&parentId=" + orderCreateDto.getParentId()))
					+ (orderCreateDto.getOrderType() == Order.OrderType.补单 ? ("&orderFill=1") : "");
		}
		redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("下单成功，请继续支付"));
		return "redirect:/u/order/" + order.getId();

	}
	
}
