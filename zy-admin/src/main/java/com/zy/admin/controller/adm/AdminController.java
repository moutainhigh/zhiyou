package com.zy.admin.controller.adm;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.gc.component.AdminComponent;
import com.gc.entity.adm.Admin;
import com.gc.entity.usr.User;
import com.gc.model.query.AdminQueryModel;
import com.gc.model.query.UserQueryModel;
import com.gc.service.AdminService;
import com.gc.service.RoleService;
import com.gc.service.UserService;
import com.gc.vo.AdminAdminVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiresPermissions("admin:*")
@RequestMapping("/admin")
@Controller
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private AdminComponent adminComponent;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "adm/adminList";
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<AdminAdminVo> list(UserQueryModel userQueryModel) {
		Page<User> userPage = userService.findPage(userQueryModel);
		Page<Admin> page = null;
		if (userPage.getData().isEmpty()) {
			page = PageBuilder.empty(userQueryModel.getPageSize(), userQueryModel.getPageNumber());
		} else {
			Long[] userIdIN = userPage.getData().stream().map(v -> v.getId()).toArray(Long[]::new);
			AdminQueryModel adminQueryModel = AdminQueryModel.builder().pageNumber(userQueryModel.getPageNumber())
					.pageSize(userQueryModel.getPageSize()).userIdIN(userIdIN).build();
			page = adminService.findPage(adminQueryModel);
		}

		return new Grid<>(PageBuilder.copyAndConvert(page, adminComponent::buildAdminVo));
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(Model model) {
		model.addAttribute("roles", roleService.findAll());
		return "adm/adminCreate";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(Admin admin, Long[] roleIds, RedirectAttributes redirectAttributes, Model model, String phone) {
		User user = userService.findByPhone(phone);
		if(user == null) {
			model.addAttribute(ResultBuilder.error("管理员新增失败, 未找到该用户"));
			return create(model);
		}
		admin.setUserId(user.getId());
		adminService.create(admin, roleIds);
		redirectAttributes.addFlashAttribute(ResultBuilder.ok("管理员新增成功"));
		return "redirect:/admin";
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String update(@PathVariable Long id, Model model) {
		Admin admin = adminService.findOne(id);
		model.addAttribute("roles", roleService.findAll());
		model.addAttribute("admin", admin);
		model.addAttribute("adminRoles", adminService.findAdminRoles(admin.getId()));
		return "adm/adminUpdate";
	}
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String update(Admin admin, Long[] roleIds) {
		adminService.update(admin, roleIds);
		return "redirect:/admin";
	}
	
	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		adminService.delete(id);
		redirectAttributes.addFlashAttribute(ResultBuilder.ok("管理员删除成功"));
		return "redirect:/admin";
	}
	
	@ResponseBody
	@RequestMapping("/checkUserPhone")
	public boolean checkUserPhone(String phone) {
		User user = userService.findByPhone(phone);
		Admin admin = null;
		if(user != null){
			admin = adminService.findByUserId(user.getId());
		}
		return admin == null;
	}
	
}
