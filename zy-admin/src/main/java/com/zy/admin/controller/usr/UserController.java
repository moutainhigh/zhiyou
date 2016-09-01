package com.zy.admin.controller.usr;

import com.zy.admin.model.AdminPrincipal;
import com.zy.common.exception.UnauthorizedException;
import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.component.UserComponent;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserRank;
import com.zy.entity.usr.User.UserType;
import com.zy.model.dto.RegisterDto;
import com.zy.model.query.UserQueryModel;
import com.zy.service.UserService;
import com.zy.util.GcUtils;
import com.zy.vo.UserAdminVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.zy.common.util.ValidateUtils.NOT_BLANK;
import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;
import static com.zy.model.Constants.MODEL_ATTRIBUTE_RESULT;

@RequestMapping("/user")
@Controller
public class UserController {
	
	@Autowired
	@Lazy
	private UserService userService;
	
	@Autowired
	private UserComponent userComponent;
	
	@RequiresPermissions("user:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "usr/userList";
	}
	
	@RequiresPermissions("user:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<UserAdminVo> list(UserQueryModel userQueryModel) {
		Page<User> page = userService.findPage(userQueryModel);
		Page<UserAdminVo> voPage = PageBuilder.copyAndConvert(page, userComponent::buildAdminVo);
		return new Grid<>(voPage);
	}
	
	@RequiresPermissions("user:edit")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String update(@PathVariable Long id, Model model) {
		checkAndValidateIsPlatform(id);
		model.addAttribute("user", userService.findOne(id));
		return "usr/userUpdate";
	}
	
	@RequiresPermissions("user:edit")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(User user, String newPassword, String phone, RedirectAttributes redirectAttributes) {
		Long userId = user.getId();
		checkAndValidateIsPlatform(userId);
		validate(userId, NOT_NULL, "user id is null");
		validate(phone, NOT_BLANK, "phone is null");
		
		User persistence = userService.findOne(user.getId());
		validate(persistence, NOT_NULL, "user is null, id =" + userId);
		
		User userByPhone = userService.findByPhone(phone);
		if(userByPhone != null) {
			if(userByPhone.getId() != userId) {
				redirectAttributes.addFlashAttribute(ResultBuilder.error("手机号已存在"));
				return "redirect:/user/update/" + userId;
			}
		}
		try {
			userService.modifyPasswordAndPhone(userId, newPassword, phone);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error("保存失败: " + e.getMessage()));
			return "redirect:/user";
		}
		redirectAttributes.addFlashAttribute(ResultBuilder.ok("用户[" + persistence.getNickname() + "]资料修改成功"));
		return "redirect:/user";
	}
	
	@RequiresPermissions("user:addVip")
	@RequestMapping("/addVip")
	@ResponseBody
	public Result<?> addVip(Long id, UserRank userRank, String remark) {
		checkAndValidateIsPlatform(id);
		try {
			userService.modifyUserRank(id, userRank, remark, getPrincipalUserId());
		} catch (Exception e) {
			return new ResultBuilder<>().message(e.getMessage()).build();
		}
		return new ResultBuilder<>().message("加VIP成功").build();
	}
	
	@RequiresPermissions("user:freeze")
	@RequestMapping("/freeze/{id}")
	public String freeze(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		checkAndValidateIsPlatform(id);
		try {
			userService.freeze(id, getPrincipalUserId());
			redirectAttributes.addFlashAttribute(ResultBuilder.ok("用户冻结成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error(e.getMessage()));
		}
		return "redirect:/user";
	}
	
	@RequiresPermissions("user:freeze")
	@RequestMapping("/unFreeze/{id}")
	public String unFreeze(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		checkAndValidateIsPlatform(id);
		try {
			userService.unfreeze(id, getPrincipalUserId());
			redirectAttributes.addFlashAttribute(ResultBuilder.ok("用户解结成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error(e.getMessage()));
		}
		return "redirect:/user";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create() {
		return "usr/userCreate";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(Model model, RedirectAttributes redirectAttributes, RegisterDto registerDto, String passwordSure) {
		validate(registerDto, "phone", "nickname", "password", "qq", "userType");
		model.addAttribute("usesr", registerDto);
		String phone = registerDto.getPhone();
		String password = registerDto.getPassword();
		
		if(userService.findByPhone(phone) != null) {
			model.addAttribute(MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("该手机已注册"));
			return "usr/userCreate";
		}
		if(!password.equals(passwordSure)) {
			model.addAttribute(MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("两次输入的密码不一致"));
			return "usr/userCreate";
		}
		
		try {
			registerDto.setRegisterIp(GcUtils.getHost());
			validate(registerDto);
			userService.registerMerchant(registerDto);
		} catch (Exception e) {
			model.addAttribute(MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("注册失败,原因" + e.getMessage()));
			return "usr/userCreate";
		}
		redirectAttributes.addFlashAttribute(MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("恭喜您,注册成功"));
		return "redirect:/user";
	}
	
	@RequestMapping(value = "/checkPhone")
	@ResponseBody
	public boolean checkPhone(@RequestParam String phone, Long id) {
		User user = userService.findByPhone(phone);
		if(user != null) {
			if(user.getId() != id) {
				return false;
			}
		}
		return true;
	}
	
	private void checkAndValidateIsPlatform(Long userId) {
		User user = userService.findOne(userId);
		validate(user, NOT_NULL, "user is null, id = " + userId);
		if(user.getUserType() == UserType.平台) {
			throw new UnauthorizedException();
		}
	}
	
	private Long getPrincipalUserId() {
		AdminPrincipal principal = (AdminPrincipal)SecurityUtils.getSubject().getPrincipal();
		return principal.getUserId();
	}
}
