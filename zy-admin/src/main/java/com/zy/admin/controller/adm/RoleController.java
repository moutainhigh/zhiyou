package com.zy.admin.controller.adm;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.admin.model.AdminConstants;
import com.zy.common.exception.ValidationException;
import com.zy.common.model.query.Page;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.gc.entity.adm.Role;
import com.gc.entity.adm.RolePermission;
import com.gc.model.Constants;
import com.gc.model.dto.RoleDto;
import com.gc.model.query.RoleQueryModel;
import com.gc.service.RoleService;


/**
 * 角色Controller
 * @author 薛定谔的猫
 * @version 2014-09-23
 */
@RequiresPermissions("role:*")
@Controller
@RequestMapping("/role")
public class RoleController {
	
	@Autowired
	private RoleService roleService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "adm/roleList";
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public Grid<Role> list(RoleQueryModel roleQueryModel) {
		Page<Role> page = roleService.findPage(roleQueryModel);
		return new Grid<Role>(page);
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(Model model) {
		model.addAttribute("permissionMap", AdminConstants.SETTING_PERMISSION_MAP);
		return "adm/roleCreate";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(RoleDto role, Model model) {
		model.addAttribute("permissionMap", AdminConstants.SETTING_PERMISSION_MAP);
		String name = role.getName();
		model.addAttribute("role", role);
		if(role.getPermissions().isEmpty()){
			model.addAttribute(ResultBuilder.error("请选择权限"));
			return "adm/roleCreate";
		}
		if(StringUtils.isBlank(role.getName())){
			model.addAttribute(ResultBuilder.error("请输入名称"));
			return "adm/roleCreate";
		}
		Role persistentRole = roleService.findByName(name);
		if(persistentRole != null){
			model.addAttribute(ResultBuilder.error("角色名称已存在：" + persistentRole.getName()));
			return "adm/roleCreate";
		}
		roleService.create(role);
		model.addAttribute(ResultBuilder.error("角色信息保存成功：" + role.getName()));
		return "redirect:/role";
	}
	

	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String update(@PathVariable Long id, Model model) {
		model.addAttribute("permissionMap", AdminConstants.SETTING_PERMISSION_MAP);
		List<RolePermission> rolePermissions = roleService.findRolePermissionByRoleId(id);
		model.addAttribute("rolePermissions", rolePermissions);
		Role role = roleService.findOne(id);
		validate(role, NOT_NULL, "通过id没有查询到数据，id=" + id);
		model.addAttribute("role", role);
		return "adm/roleUpdate";
	}
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String update(Model model, RoleDto roleDto) {
		if(roleDto.getPermissions().isEmpty()){
			throw new ValidationException("数据异常，role为空");
		}
		try {
			roleService.update(roleDto);
			model.addAttribute(ResultBuilder.error("角色信息保存成功：" + roleDto.getName()));
		} catch (Exception e) {
			
		}
		return "redirect:/role";
	}
	
	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			roleService.delete(id);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("删除失败"));
		}
		redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("删除成功"));
		return "redirect:/role";
	}
	
}
