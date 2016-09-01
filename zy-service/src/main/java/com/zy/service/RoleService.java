package com.gc.service;

import java.util.List;

import com.zy.common.model.query.Page;
import com.gc.entity.adm.Role;
import com.gc.entity.adm.RolePermission;
import com.gc.model.dto.RoleDto;
import com.gc.model.query.RoleQueryModel;

public interface RoleService {

	Role findByName(String name);
	
	Role findOne(Long id);
	
	void create(RoleDto roleModel);
	
	void update(RoleDto roleModel);
	
	void delete(Long id);
	
	List<Role> findAll();
	
	List<RolePermission> findRolePermissionByRoleId(Long roleId);

	Page<Role> findPage(RoleQueryModel roleQueryModel);
}
