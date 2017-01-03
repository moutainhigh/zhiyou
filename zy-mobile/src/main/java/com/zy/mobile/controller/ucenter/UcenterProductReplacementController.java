package com.zy.mobile.controller.ucenter;

import com.zy.entity.mal.ProductReplacement;
import com.zy.model.Principal;
import com.zy.model.query.ProductReplacementQueryModel;
import com.zy.service.ProductReplacementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@RequestMapping("/u/productReplacement")
@Controller
public class UcenterProductReplacementController {

	@Autowired
	private ProductReplacementService productReplacementService;

	@RequestMapping()
	public String list(Principal principal, Model model) {
		List<ProductReplacement> all = productReplacementService.findAll(ProductReplacementQueryModel.builder().userIdEQ(principal.getUserId()).build());
		model.addAttribute("productReplacement", all);
		return "ucenter/order/productReplacementList";
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create() {

		return "ucenter/order/productReplacementCreate";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(Principal principal, ProductReplacement productReplacement) {
		productReplacement.setUserId(principal.getUserId());
		try{
			productReplacementService.create(productReplacement);
		} catch (Exception e) {

		}
		return "redirect:/u/productReplacement";
	}

	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public String detail() {
		return "";
	}
}
