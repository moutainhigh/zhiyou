package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.cms.HelpCategory;
import com.zy.model.query.HelpCategoryQueryModel;


public interface HelpCategoryMapper {

	int insert(HelpCategory helpCategory);

	int update(HelpCategory helpCategory);

	int merge(@Param("helpCategory") HelpCategory helpCategory, @Param("fields")String... fields);

	int delete(Long id);

	HelpCategory findOne(Long id);

	List<HelpCategory> findAll(HelpCategoryQueryModel helpCategoryQueryModel);

	long count(HelpCategoryQueryModel helpCategoryQueryModel);

	HelpCategory findByCode(String code);

}