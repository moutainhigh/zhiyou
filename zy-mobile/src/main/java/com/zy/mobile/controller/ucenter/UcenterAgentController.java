package com.zy.mobile.controller.ucenter;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zy.Config;
import com.zy.component.ProductComponent;
import com.zy.entity.mal.Product;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserRank;
import com.zy.model.Principal;
import com.zy.service.ProductService;
import com.zy.service.UserService;

@RequestMapping("/u/agent")
@Controller
public class UcenterAgentController {
	
	@Autowired
	private ProductService productService;

	@Autowired
	private UserService userService;

	@Autowired
	private ProductComponent productComponent;

	@Autowired
	private Config config;
	
	@RequestMapping
	public String agent(Principal principal, Model model) {

		Long productIdOld = config.getOld();
		Long productIdNew = config.getNew();

		Product productOld = productService.findOne(config.getOld());
		Product productNew = productService.findOne(config.getNew());

		model.addAttribute("productOld", productComponent.buildListVo(productOld));
		model.addAttribute("productNew", productComponent.buildListVo(productNew));

		User user = userService.findOne(principal.getUserId());
		model.addAttribute("userRank", user.getUserRank());

		/* old */
		int quantity1Old = 300;
		int quantity2Old = 100;
		int quantity3Old = 15;
		BigDecimal price1Old = productService.getPrice(productIdOld, UserRank.V0, quantity1Old);
		BigDecimal price2Old = productService.getPrice(productIdOld, UserRank.V0, quantity2Old);
		BigDecimal price3Old = productService.getPrice(productIdOld, UserRank.V0, quantity3Old);
		model.addAttribute("quantity1Old", quantity1Old);
		model.addAttribute("quantity2Old", quantity2Old);
		model.addAttribute("quantity3Old", quantity3Old);
		model.addAttribute("amount1Old", price1Old.multiply(new BigDecimal(quantity1Old)));
		model.addAttribute("amount2Old", price2Old.multiply(new BigDecimal(quantity2Old)));
		model.addAttribute("amount3Old", price3Old.multiply(new BigDecimal(quantity3Old)));


		/* new */
		int quantity1New = 240;
		int quantity2New = 80;
		int quantity3New = 5;
		BigDecimal price1New = productService.getPrice(productIdNew, UserRank.V0, quantity1New);
		BigDecimal price2New = productService.getPrice(productIdNew, UserRank.V0, quantity2New);
		BigDecimal price3New = productService.getPrice(productIdNew, UserRank.V0, quantity3New);
		model.addAttribute("quantity1New", quantity1New);
		model.addAttribute("quantity2New", quantity2New);
		model.addAttribute("quantity3New", quantity3New);
		model.addAttribute("amount1New", price1New.multiply(new BigDecimal(quantity1New)));
		model.addAttribute("amount2New", price2New.multiply(new BigDecimal(quantity2New)));
		model.addAttribute("amount3New", price3New.multiply(new BigDecimal(quantity3New)));

		return "agent";
	}
	
}
