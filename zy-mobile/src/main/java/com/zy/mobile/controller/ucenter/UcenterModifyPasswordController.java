package com.zy.mobile.controller.ucenter;

import com.zy.common.model.result.ResultBuilder;
import com.zy.entity.usr.User;
import com.zy.model.Principal;
import com.zy.service.UserService;
import me.chanjar.weixin.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.zy.common.util.ValidateUtils.NOT_BLANK;
import static com.zy.common.util.ValidateUtils.validate;
import static com.zy.model.Constants.MODEL_ATTRIBUTE_RESULT;


@RequestMapping("/u/password")
@Controller
public class UcenterModifyPasswordController {
	
	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.GET)
	public String index(Principal principal, Model model) {
		User user = userService.findOne(principal.getUserId());
		String persistentPassword = user.getPassword();
		if (StringUtils.isEmpty(persistentPassword)) {
			model.addAttribute("hasPassword", false);
		} else {
			model.addAttribute("hasPassword", true);
		}
		return "ucenter/user/userPassword";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String modify(String oldPassword, String password, Principal principal, Model model, RedirectAttributes redirectAttributes) {
		User user = userService.findOne(principal.getUserId());
		String persistentPassword = user.getPassword();
		String label;
		validate(password, NOT_BLANK, "password must not be blank");

		if (StringUtils.isEmpty(persistentPassword)) {
			label = "设置";
			userService.modifyPassword(principal.getUserId(), password);
		} else {
			label = "修改";
			validate(password, NOT_BLANK, "old password must not be blank");
			if (!userService.hashPassword(oldPassword).equals(persistentPassword)) {
				redirectAttributes.addFlashAttribute(MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("修改密码失败, 旧密码错误"));
				return "redirect:/u/password";
			}
		}
		redirectAttributes.addFlashAttribute(MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok(label + "密码成功"));
		return "redirect:/u";
	}

}
