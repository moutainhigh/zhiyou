package com.gc.service;

import java.util.List;

import com.gc.entity.cms.HelpCategory;
import com.gc.model.query.HelpCategoryQueryModel;

public interface HelpCategoryService {

	HelpCategory create(HelpCategory helpCategory);
	
	HelpCategory update(HelpCategory helpCategory);
	
	HelpCategory findOne(Long id);
	
	List<HelpCategory> findAll(HelpCategoryQueryModel helpCategoryQueryModel);
	
	void delete(Long id);
}
