package com.zy.mobile.controller;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zy.component.ProductComponent;
import com.zy.entity.mal.Product;
import com.zy.entity.usr.Address;
import com.zy.model.Principal;
import com.zy.service.AddressService;
import com.zy.service.ProductService;
import com.zy.vo.ProductListVo;

@RequestMapping("/order")
@Controller
public class OrderController {

	@Autowired
	private AddressService addressService;
	
	@Autowired
	private ProductService productService;

	@Autowired
	private ProductComponent productComponent;
	
	@RequestMapping("/create")
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
	
}
