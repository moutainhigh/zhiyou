package com.zy.service.impl;

import static com.zy.common.util.ValidateUtils.validate;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.zy.entity.usr.Tag;
import com.zy.mapper.TagMapper;
import com.zy.service.TagService;

@Service
@Validated
public class TagServiceImpl implements TagService {

	@Autowired
	private TagMapper tagMapper;

	@Override
	public Tag findOne(@NotNull Long id) {
		return tagMapper.findOne(id);
	}

	@Override
	public List<Tag> findAll() {
		return tagMapper.findAll();
	}

	@Override
	public Tag create(@NotNull Tag tag) {
		validate(tag);
		Tag one = tagMapper.findOne(tag.getId());
		if (one != null)
			return one;
		this.tagMapper.insert(tag);
		return tag;
	}

	@Override
	public void delete(@NotNull Long id) {
		this.tagMapper.delete(id);
	}
}
