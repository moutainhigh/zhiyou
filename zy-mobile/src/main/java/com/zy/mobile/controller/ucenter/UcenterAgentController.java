package com.zy.mobile.controller.ucenter;

import com.zy.Config;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.component.ProductComponent;
import com.zy.entity.mal.Product;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserRank;
import com.zy.entity.usr.User.UserType;
import com.zy.model.Principal;
import com.zy.service.ProductService;
import com.zy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

@RequestMapping("/u")
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
	
	@RequestMapping("/agent")
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
		int quantity4New = 3600;
		BigDecimal price1New = productService.getPrice(productIdNew, UserRank.V0, quantity1New);
		BigDecimal price2New = productService.getPrice(productIdNew, UserRank.V0, quantity2New);
		BigDecimal price3New = productService.getPrice(productIdNew, UserRank.V0, quantity3New);
		BigDecimal price4New = productService.getPrice(productIdNew, UserRank.V0, quantity4New);
		model.addAttribute("quantity1New", quantity1New);
		model.addAttribute("quantity2New", quantity2New);
		model.addAttribute("quantity3New", quantity3New);
		model.addAttribute("quantity4New", quantity4New);
		model.addAttribute("amount1New", price1New.multiply(new BigDecimal(quantity1New)));
		model.addAttribute("amount2New", price2New.multiply(new BigDecimal(quantity2New)));
		model.addAttribute("amount3New", price3New.multiply(new BigDecimal(quantity3New)));
		model.addAttribute("amount4New", price4New.multiply(new BigDecimal(quantity4New)));

		return "agent";
	}
	
	@ResponseBody
	@RequestMapping("checkPhone")
	public Result<String> checkPhone(Principal principal, String phone){
		User user = userService.findByPhone(phone);
		if(user != null) {
			if(user.getId().equals(principal.getUserId())){
				return ResultBuilder.error("不能设置本人为推荐人服务商.");
			} else if(user.getUserType() != UserType.代理){
				return ResultBuilder.error("此手机绑定的用户不是服务商.");
			} else if(user.getUserRank() == UserRank.V0){
				return ResultBuilder.error("此手机绑定的用户不是服务商.");
			}{
				return ResultBuilder.ok(user.getId().toString());
			}
		} else {
			return ResultBuilder.error("没有找到此手机号的服务商.");
		}
	}
	
}
