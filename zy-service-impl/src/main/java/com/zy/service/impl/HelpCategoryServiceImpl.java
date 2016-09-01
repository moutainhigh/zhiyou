package com.gc.service.impl;

import com.gc.entity.cms.HelpCategory;
import com.gc.mapper.HelpCategoryMapper;
import com.gc.model.query.HelpCategoryQueryModel;
import com.gc.service.HelpCategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@Service
@Validated
public class HelpCategoryServiceImpl implements HelpCategoryService{

	@Autowired
	private HelpCategoryMapper helpCategoryMapper;
	
	@Override
	public HelpCategory create(@NotNull HelpCategory helpCategory) {
		helpCategory.setCreatedTime(new Date());
		validate(helpCategory);
		helpCategoryMapper.insert(helpCategory);
		return helpCategory;
	}

	@Override
	public HelpCategory update(@NotNull HelpCategory helpCategory) {
		validate(helpCategory,"code", "name", "userType", "indexNumber", "id");
		helpCategoryMapper.merge(helpCategory, "code", "name", "userType", "indexNumber");
		return helpCategory;
	}

	@Override
	public HelpCategory findOne(@NotNull Long id) {
		validate(id, NOT_NULL, "id is null");
		return helpCategoryMapper.findOne(id);
	}

	@Override
	public void delete(@NotNull Long id) {
		helpCategoryMapper.delete(id);
		
	}

	@Override
	public List<HelpCategory> findAll(@NotNull HelpCategoryQueryModel helpCategoryQueryModel) {
		return helpCategoryMapper.findAll(helpCategoryQueryModel);
	}

}
