package com.zy.service;

import java.util.List;

import com.zy.entity.cms.HelpCategory;
import com.zy.model.query.HelpCategoryQueryModel;

public interface HelpCategoryService {

	HelpCategory create(HelpCategory helpCategory);
	
	HelpCategory update(HelpCategory helpCategory);
	
	HelpCategory findOne(Long id);
	
	List<HelpCategory> findAll(HelpCategoryQueryModel helpCategoryQueryModel);
	
	void delete(Long id);
}
