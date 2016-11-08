package com.zy.mobile.controller;

import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.entity.usr.User;
import com.zy.service.UserService;
import me.chanjar.weixin.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.image.BufferedImage;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@RequestMapping("/code")
@Controller
public class CodeController {

	@Autowired
	private UserService userService;

	@RequestMapping
	public String index(Model model) {
		return "code";
	}

	@RequestMapping
	@ResponseBody
	public Result<?> check(@RequestParam String code) {
		User user = userService.findByCode(code);
		if (user != null) {
			return ResultBuilder.result(user.getId());
		} else {
			return ResultBuilder.error("授权码不存在");
		}
	}
	
	@RequestMapping("/image")
	@ResponseBody
	public BufferedImage image(@RequestParam Long userId, Model model) {
		User user = userService.findOne(userId);
		validate(user, NOT_NULL, "user id " + userId + " is not found");
		if (StringUtils.isBlank(user.getCode())) {
			userService.generateCode(userId);
			user = userService.findOne(userId);
		}
		String nickname = user.getNickname();
		String avatar = user.getAvatar();
		String code = user.getCode();
		// TODO
		return null;
	}
	
}
