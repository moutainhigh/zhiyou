package com.zy.mobile.controller.ucenter;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.model.result.ResultBuilder;
import com.zy.component.ProductComponent;
import com.zy.component.UserComponent;
import com.zy.entity.mal.Order;
import com.zy.entity.mal.Product;
import com.zy.entity.usr.Address;
import com.zy.entity.usr.User;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.model.dto.OrderCreateDto;
import com.zy.service.AddressService;
import com.zy.service.OrderService;
import com.zy.service.ProductService;
import com.zy.service.UserService;
import com.zy.vo.ProductListVo;

@RequestMapping("/u/order")
@Controller
public class UcenterOrderCreateController {

	@Autowired
	private AddressService addressService;
	
	@Autowired
	private OrderService orderService;

	@Autowired
	private UserService userService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductComponent productComponent;
	
	@Autowired
	private UserComponent userComponent;

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(@RequestParam Long productId, @RequestParam Long quantity, Long parentId, Model model, Principal principal) {

		Long userId = principal.getUserId();
		User user = userService.findOne(userId);
		if (user.getUserRank() == User.UserRank.V0) {
			validate(parentId, NOT_NULL, "parent user id is null");
			User parent = userService.findOne(parentId);
			validate(parent, NOT_NULL, "parent user id " + parentId + " does not exist");
			model.addAttribute("parent", userComponent.buildListVo(parent));
		}

		Address address = addressService.findDefaultByUserId(userId);
		Product product = productService.findOne(productId);
		product.setPrice(productService.getPrice(productId, user.getUserRank(), quantity));
		validate(product, NOT_NULL, "product id:" + productId + " is not found !");
		ProductListVo productVo = productComponent.buildListVo(product);
		model.addAttribute("product", productVo);
		model.addAttribute("quantity", quantity);
		model.addAttribute("address", address);
		return "ucenter/order/orderCreate";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(OrderCreateDto orderCreateDto, Long parentId, Principal principal, RedirectAttributes redirectAttributes) {
		
		orderCreateDto.setUserId(principal.getUserId());
		orderCreateDto.setParentId(parentId);
		Order order = null;
		try {
			order = orderService.create(orderCreateDto);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
			return "redirect:/u/order/create?productId=" + orderCreateDto.getProductId() + "&quantity=" + orderCreateDto.getQuantity() + (orderCreateDto.getParentId() == null ? "" : ("&parentId=" + orderCreateDto.getParentId()));
		}
		redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("下单成功，请继续支付"));
		return "redirect:/u/order/" + order.getSn();

	}
	
}
