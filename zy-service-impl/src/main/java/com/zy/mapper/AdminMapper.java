package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.adm.Admin;
import com.zy.model.query.AdminQueryModel;


public interface AdminMapper {

	int insert(Admin admin);

	int update(Admin admin);

	int merge(@Param("admin") Admin admin, @Param("fields")String... fields);

	int delete(Long id);

	Admin findOne(Long id);

	List<Admin> findAll(AdminQueryModel adminQueryModel);

	long count(AdminQueryModel adminQueryModel);

	Admin findByUserId(Long userId);

}