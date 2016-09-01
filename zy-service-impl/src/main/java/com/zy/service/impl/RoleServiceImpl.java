package com.gc.service.impl;

import com.zy.common.model.query.Page;
import com.gc.entity.adm.Role;
import com.gc.entity.adm.RolePermission;
import com.gc.mapper.AdminRoleMapper;
import com.gc.mapper.RoleMapper;
import com.gc.mapper.RolePermissionMapper;
import com.gc.model.dto.RoleDto;
import com.gc.model.query.RoleQueryModel;
import com.gc.service.RoleService;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

@Service
@Validated
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleMapper roleMapper;
	
	@Autowired
	private RolePermissionMapper rolePermissionMapper;
	
	@Autowired
	private AdminRoleMapper adminRoleMapper;
	
	@Override
	public void create(@NotNull RoleDto role) {
		roleMapper.insert(role);
		Long id = role.getId();
		if(role.getPermissions() != null) {
			for(Map.Entry<String, String> entry : role.getPermissions().entrySet()){
				RolePermission rolePermission = new RolePermission();
				rolePermission.setPermission(entry.getKey());
				rolePermission.setPermissionName(entry.getValue());
				rolePermission.setRoleId(id);
				rolePermissionMapper.insert(rolePermission);
			}
		}
	}

	@Override
	public void update(@NotNull RoleDto role) {
		Long id = role.getId();
		rolePermissionMapper.deleteByRoleId(role.getId());
		if(role.getPermissions() != null) {
			for(Map.Entry<String, String> entry : role.getPermissions().entrySet()){
				RolePermission rolePermission = new RolePermission();
				rolePermission.setPermission(entry.getKey());
				rolePermission.setPermissionName(entry.getValue());
				rolePermission.setRoleId(id);
				rolePermissionMapper.insert(rolePermission);
			}
		}
	}

	@Override
	public void delete(@NotNull Long id) {
		rolePermissionMapper.deleteByRoleId(id);
		adminRoleMapper.deleteByRoleId(id);
		roleMapper.delete(id);
	}
	
	@Override
	public Role findOne(@NotNull Long id){
		return roleMapper.findOne(id);
	}
	
	@Override
	public Role findByName(@NotBlank String name){
		return roleMapper.findByName(name);
	}
	
	@Override
	public List<Role> findAll() {
		RoleQueryModel roleQueryModel = new RoleQueryModel();
		return roleMapper.findAll(roleQueryModel);
	}

	@Override
	public List<RolePermission> findRolePermissionByRoleId(@NotNull Long roleId) {
		return rolePermissionMapper.findByRoleId(roleId);
	}

	@Override
	public Page<Role> findPage(@NotNull RoleQueryModel roleQueryModel) {
		if(roleQueryModel.getPageNumber() == null)
			roleQueryModel.setPageNumber(0);
		if(roleQueryModel.getPageSize() == null)
			roleQueryModel.setPageSize(20);
		long total = roleMapper.count(roleQueryModel);
		List<Role> data = roleMapper.findAll(roleQueryModel);
		Page<Role> page = new Page<>();
		page.setPageNumber(roleQueryModel.getPageNumber());
		page.setPageSize(roleQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}
}
