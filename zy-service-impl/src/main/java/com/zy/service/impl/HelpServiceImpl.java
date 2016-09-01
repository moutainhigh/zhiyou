package com.gc.service.impl;

import com.gc.entity.cms.Help;
import com.gc.mapper.HelpMapper;
import com.gc.service.HelpService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

import static com.zy.common.util.ValidateUtils.validate;

@Service
@Validated
public class HelpServiceImpl implements HelpService {

	@Autowired
	private HelpMapper helpMapper;
	
	@Override
	public Help create(@NotNull Help help) {
		help.setCreatedTime(new Date());
		validate(help);
		helpMapper.insert(help);
		return help;
	}

	@Override
	public Help update(@NotNull Help help) {
		validate(help, "title", "content", "indexNumber", "id");
		helpMapper.merge(help, "title", "content", "indexNumber");
		return help;
	}

	@Override
	public Help merge(@NotNull Help help,@NotNull String... fields) {
		helpMapper.merge(help, fields);
		return help;
	}

	@Override
	public Help findOne(@NotNull Long id) {
		return helpMapper.findOne(id);
	}

	@Override
	public List<Help> findByHelpCategoryId(@NotNull Long helpCategoryId) {
		return helpMapper.findByHelpCategoryId(helpCategoryId);
	}

	@Override
	public List<Help> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
