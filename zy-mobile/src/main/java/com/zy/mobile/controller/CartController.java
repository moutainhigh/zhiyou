package com.zy.mobile.controller;

import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.component.CartComponent;
import com.zy.entity.mal.CartItem;
import com.zy.entity.usr.User;
import com.zy.model.CartVo;
import com.zy.model.Principal;
import com.zy.service.CartItemService;
import com.zy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@Controller
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private CartComponent cartComponent;

	@RequestMapping
	public String list(Model model, Principal principal) {
		Long userId = principal.getUserId();
		User user = userService.findOne(userId);
		model.addAttribute("userRank", user.getUserRank());
		
		List<CartItem> cartItems = cartItemService.findByUserId(principal.getUserId());
		model.addAttribute("cart", cartComponent.buildCartVo(cartItems));
		return "cart";
	}

	@RequestMapping(value = "/add")
	@ResponseBody
	public Result<CartVo> add(Long skuId, long quantity, Principal principal) {
		validate(skuId, NOT_NULL, "sku id is null!");
		validate(quantity, v -> v > 0, "quantity must be greater than 0");
		cartItemService.add(principal.getUserId(), skuId, quantity);
		List<CartItem> cartItems = cartItemService.findByUserId(principal.getUserId());
		return ResultBuilder.result(cartComponent.buildCartVo(cartItems));
	}

	@RequestMapping(value = "/modify")
	@ResponseBody
	public Result<CartVo> modify(Long skuId, Long quantity, Principal principal) {
		validate(skuId, NOT_NULL, "skuId id is null!");
		validate(quantity, v -> v > 0, "quantity must be greater than 0");
		cartItemService.modify(principal.getUserId(), skuId, quantity);
		List<CartItem> cartItems = cartItemService.findByUserId(principal.getUserId());
		return ResultBuilder.result(cartComponent.buildCartVo(cartItems));
	}

	@RequestMapping(value = "/remove")
	@ResponseBody
	public Result<CartVo> remove(Long skuId, Principal principal) {
		validate(skuId, NOT_NULL, "skuId id is null!");
		cartItemService.remove(principal.getUserId(), skuId);
		List<CartItem> cartItems = cartItemService.findByUserId(principal.getUserId());
		return ResultBuilder.result(cartComponent.buildCartVo(cartItems));
	}

	@RequestMapping(value = "/removeAll")
	@ResponseBody
	public Result<CartVo> removeAll(Principal principal) {
		cartItemService.deleteByUserId(principal.getUserId());
		List<CartItem> cartItems = new ArrayList<>();
		return ResultBuilder.result(cartComponent.buildCartVo(cartItems));
	}

}
