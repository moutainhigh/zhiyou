package com.zy.mobile.controller;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.model.result.ResultBuilder;
import com.zy.component.OrderComponent;
import com.zy.component.ProductComponent;
import com.zy.entity.mal.Order;
import com.zy.entity.mal.Product;
import com.zy.entity.usr.Address;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.model.dto.OrderCreateDto;
import com.zy.service.AddressService;
import com.zy.service.OrderService;
import com.zy.service.ProductService;
import com.zy.vo.ProductListVo;

@RequestMapping("/order")
@Controller
public class OrderController {

	@Autowired
	private AddressService addressService;
	
	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderComponent orderComponent;
	
	@Autowired
	private ProductService productService;

	@Autowired
	private ProductComponent productComponent;
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(Long productId, Long quantity, Model model, Principal principal) {
		validate(productId, NOT_NULL, "product id is null!");
		Long userId = principal.getUserId();
		validate(userId, NOT_NULL, "user id is null!");
		Address address = addressService.findDefaultByUserId(userId);
		Product product = productService.findOne(productId);
		validate(product, NOT_NULL, "product id:" + productId + " is not found !");
		ProductListVo productVo = productComponent.buildListVo(product);
		model.addAttribute("product", productVo);
		model.addAttribute("quantity", quantity);
		model.addAttribute("address", address);
		return "order/orderCreate";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(HttpServletRequest request, OrderCreateDto orderCreateDto, Principal principal, RedirectAttributes redirectAttributes, Model model) {
		
		orderCreateDto.setUserId(principal.getUserId());
		try {
			Order order = orderService.create(orderCreateDto);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("下单成功"));
			return "redirect:/ucenter/order/" + order.getSn();
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
			return "redirect:/ucenter/order/create?productId" + orderCreateDto.getProductId();
		}
	}
	
}
