package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.cms.Help;


public interface HelpMapper {

	int insert(Help help);

	int update(Help help);

	int merge(@Param("help") Help help, @Param("fields")String... fields);

	int delete(Long id);

	Help findOne(Long id);

	List<Help> findAll();

	List<Help> findByHelpCategoryId(Long helpCategoryId);

}