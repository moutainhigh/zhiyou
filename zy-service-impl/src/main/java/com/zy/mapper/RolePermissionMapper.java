package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.adm.RolePermission;


public interface RolePermissionMapper {

	int insert(RolePermission rolePermission);

	int update(RolePermission rolePermission);

	int merge(@Param("rolePermission") RolePermission rolePermission, @Param("fields")String... fields);

	int delete(Long id);

	RolePermission findOne(Long id);

	List<RolePermission> findAll();

	int deleteByRoleId(Long id);

	List<RolePermission> findByRoleId(Long roleId);

	List<RolePermission> findByAdminId(Long userId);

}