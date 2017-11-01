package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.mergeusr.MergeUser;
import com.zy.model.query.MergeUserQueryModel;

import java.util.List;

public interface MergeUserService {

	MergeUser findOne(Long id);

	void create(MergeUser mergeUser);

	Page<MergeUser> findPage(MergeUserQueryModel mergeUserQueryModel);

	List<MergeUser> findAll(MergeUserQueryModel mergeUserQueryModel);

	void modifyParentId(Long id, Long parentId);

	long count(MergeUserQueryModel mergeUserQueryModel);

}
