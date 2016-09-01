package com.gc.service.impl;

import com.zy.common.model.query.Page;
import com.gc.entity.adm.Admin;
import com.gc.entity.adm.AdminRole;
import com.gc.entity.adm.Role;
import com.gc.entity.adm.RolePermission;
import com.gc.mapper.AdminMapper;
import com.gc.mapper.AdminRoleMapper;
import com.gc.mapper.RoleMapper;
import com.gc.mapper.RolePermissionMapper;
import com.gc.model.query.AdminQueryModel;
import com.gc.service.AdminService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@Service
@Validated
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminRoleMapper adminRoleMapper;
	@Autowired
	private AdminMapper adminMapper;
	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private RolePermissionMapper rolePermissionMapper;

	@Override
	public void create(@NotNull Admin admin, @NotNull Long[] roleIds) {
		if (roleIds != null) {
			List<Role> roles = roleMapper.findByIdIn(roleIds);
			List<String> roleNames = new ArrayList<>();
			for (Role role : roles) {
				roleNames.add(role.getName());
			}
			admin.setRoleNames(StringUtils.join(roleNames, ","));
			adminMapper.insert(admin);
			for (Role role : roles) {
				AdminRole userRole = new AdminRole();
				userRole.setAdminId(admin.getId());
				userRole.setRoleId(role.getId());
				adminRoleMapper.insert(userRole);
			}
		}
	}


	@Override
	public void update(@NotNull Admin admin, @NotNull Long[] roleIds) {
		Long adminId = admin.getId();
		Admin persistentAdmin = adminMapper.findOne(admin.getId());
		validate(persistentAdmin, NOT_NULL, "admin " + adminId + " is not found");

		adminRoleMapper.deleteByAdminId(adminId);
		if (roleIds != null) {
			List<Role> roles = roleMapper.findByIdIn(roleIds);
			List<String> roleNames = new ArrayList<>();
			for (Role role : roles) {
				AdminRole userRole = new AdminRole();
				userRole.setAdminId(adminId);
				userRole.setRoleId(role.getId());
				adminRoleMapper.insert(userRole);
				roleNames.add(role.getName());
			}
			persistentAdmin.setRoleNames(StringUtils.join(roleNames, ","));
		}
		if (admin.getSiteId() != null) {
			persistentAdmin.setSiteId(admin.getSiteId());
		}
		adminMapper.update(persistentAdmin);
	}

	@Override
	public void delete(@NotNull Long id) {
		Admin admin = adminMapper.findOne(id);
		validate(admin, NOT_NULL, "admin " + id + " is not found");
		adminRoleMapper.deleteByAdminId(id);
		adminMapper.delete(admin.getId());
	}

	@Override
	public Set<String> findPermissions(@NotNull Long id) {
		List<RolePermission> rolePermissions = rolePermissionMapper.findByAdminId(id);
		Set<String> permissions = new HashSet<>();
		for (RolePermission rolePermission : rolePermissions) {
			permissions.add(rolePermission.getPermission());
		}
		return permissions;
	}


	@Override
	public Admin findOne(@NotNull Long id) {
		return adminMapper.findOne(id);
	}

	@Override
	public List<AdminRole> findAdminRoles(@NotNull Long id) {
		return adminRoleMapper.findByAdminId(id);
	}


	@Override
	public Page<Admin> findPage(@NotNull AdminQueryModel adminQueryModel) {
		if (adminQueryModel.getPageNumber() == null)
			adminQueryModel.setPageNumber(0);
		if (adminQueryModel.getPageSize() == null)
			adminQueryModel.setPageSize(20);
		long total = adminMapper.count(adminQueryModel);
		List<Admin> data = adminMapper.findAll(adminQueryModel);
		Page<Admin> page = new Page<>();
		page.setPageNumber(adminQueryModel.getPageNumber());
		page.setPageSize(adminQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}


	@Override
	public Admin findByUserId(@NotNull Long userId) {
		return adminMapper.findByUserId(userId);
	}


}
