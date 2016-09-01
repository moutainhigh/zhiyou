package com.gc.service;

import java.util.List;

import com.gc.entity.cms.Help;

public interface HelpService {

	Help create(Help help);
	
	Help update(Help help);
	
	Help merge(Help help, String... fields);
	
	Help findOne(Long id);
	
	List<Help> findByHelpCategoryId(Long helpCategoryId);
	
	List<Help> findAll();
}
