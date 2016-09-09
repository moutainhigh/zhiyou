package com.zy.mobile.controller.ucenter;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.component.UserComponent;
import com.zy.entity.mal.Product;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserRank;
import com.zy.entity.usr.User.UserType;
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
	private UserComponent userComponent;

	@RequestMapping
	public String agent(Long productId, Principal principal, Model model) {
		Product product = productService.findOne(productId);
		validate(product, NOT_NULL, "product id:" + productId + " is not found !");
		model.addAttribute("product", product);
		User user = userService.findOne(principal.getUserId());
		model.addAttribute("userRank", user.getUserRank());
		if(user.getInviterId() != null){
			User inviter = userService.findOne(user.getInviterId());
			if(inviter != null) {
				model.addAttribute("inviter", userComponent.buildListVo(inviter));
			}
		}
		int quantity1 = 300;
		int quantity2 = 100;
		int quantity3 = 15;
		BigDecimal price1 = productService.getPrice(productId, UserRank.V0, quantity1);
		BigDecimal price2 = productService.getPrice(productId, UserRank.V0, quantity2);
		BigDecimal price3 = productService.getPrice(productId, UserRank.V0, quantity3);
		model.addAttribute("quantity1", quantity1);
		model.addAttribute("quantity2", quantity2);
		model.addAttribute("quantity3", quantity3);
		model.addAttribute("amount1", price1.multiply(new BigDecimal(quantity1)));
		model.addAttribute("amount2", price2.multiply(new BigDecimal(quantity2)));
		model.addAttribute("amount3", price3.multiply(new BigDecimal(quantity3)));
		return "agent";
	}
	
	@ResponseBody
	@RequestMapping("checkPhone")
	public Result<String> checkPhone(Principal principal, String phone){
		User user = userService.findByPhone(phone);
		if(user != null) {
			if(user.getId().equals(principal.getUserId())){
				return ResultBuilder.error("不能设置本人为上级代理.");
			} else if(user.getUserType() != UserType.代理){
				return ResultBuilder.error("此手机绑定的用户不是代理.");
			} else {
				return ResultBuilder.ok(user.getId().toString());
			}
		} else {
			return ResultBuilder.error("没有找到此手机号的代理.");
		}
	}
	
}
