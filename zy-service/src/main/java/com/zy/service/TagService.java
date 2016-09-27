package com.zy.service;

import java.util.List;

import com.zy.entity.usr.Tag;

public interface TagService {

	Tag findOne(Long id);

	List<Tag> findAll();

	Tag create(Tag tag);

	void delete(Long id);

}
