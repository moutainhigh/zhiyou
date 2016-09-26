package com.zy.admin.controller.usr;

import static com.zy.common.util.ValidateUtils.NOT_BLANK;
import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;
import static com.zy.model.Constants.MODEL_ATTRIBUTE_RESULT;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
import com.zy.model.query.UserQueryModel;
import com.zy.service.UserService;
import com.zy.util.GcUtils;
import com.zy.vo.UserAdminVo;

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
	public String list(Model model) {
		model.addAttribute("userRankMap", Arrays.asList(UserRank.values()).stream().collect(Collectors.toMap(v->v, v->GcUtils.getUserRankLabel(v),(u,v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); }, LinkedHashMap::new)) );
		return "usr/userList";
	}
	
	@RequiresPermissions("user:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<UserAdminVo> list(UserQueryModel userQueryModel, String inviterNicknameLK) {
		if(StringUtils.isNotBlank(inviterNicknameLK)) {
			UserQueryModel inviterQueryModel = new UserQueryModel();
			inviterQueryModel.setNicknameLK(inviterNicknameLK);
			List<User> inviters = userService.findAll(inviterQueryModel);
			if(inviters.isEmpty()) {
				return new Grid<UserAdminVo>(PageBuilder.empty(userQueryModel.getPageSize(), userQueryModel.getPageNumber()));
			}
			if(inviters != null && inviters.size() > 0) {
				Long[] inviterIds = inviters.stream().map(v -> v.getId()).toArray(Long[]::new);
				userQueryModel.setInviterIdIN(inviterIds);
			}
		}
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
			userService.modifyPassword(userId, newPassword, getPrincipalUserId());
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
			userService.modifyUserRank(id, userRank, getPrincipalUserId(), remark);
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
	public String create(Model model, RedirectAttributes redirectAttributes, String passwordSure) {
		redirectAttributes.addFlashAttribute(MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("注册失败"));
		// TODO
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
