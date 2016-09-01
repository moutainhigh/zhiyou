package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.adm.AdminRole;


public interface AdminRoleMapper {

	int insert(AdminRole adminRole);

	int update(AdminRole adminRole);

	int merge(@Param("adminRole") AdminRole adminRole, @Param("fields")String... fields);

	int delete(Long id);

	AdminRole findOne(Long id);

	List<AdminRole> findAll();

	int deleteByRoleId(Long id);

	List<AdminRole> findByAdminId(Long adminId);

	int deleteByAdminId(Long adminId);

}