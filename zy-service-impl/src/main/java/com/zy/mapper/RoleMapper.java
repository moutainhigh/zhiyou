package com.gc.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.gc.entity.adm.Role;
import com.gc.model.query.RoleQueryModel;


public interface RoleMapper {

	int insert(Role role);

	int update(Role role);

	int merge(@Param("role") Role role, @Param("fields")String... fields);

	int delete(Long id);

	Role findOne(Long id);

	List<Role> findAll(RoleQueryModel roleQueryModel);

	long count(RoleQueryModel roleQueryModel);

	Role findByName(String name);

	List<Role> findByIdIn(Long[] roleIds);

}