package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.mergeusr.MergeUser;
import com.zy.model.query.MergeUserQueryModel;

import java.util.List;

public interface MergeUserService {

	MergeUser findOne(Long id);

	MergeUser findByUserIdAndProductType(Long userId,Integer productType);

	void create(MergeUser mergeUser);

	Page<MergeUser> findPage(MergeUserQueryModel mergeUserQueryModel);

	List<MergeUser> findAll(MergeUserQueryModel mergeUserQueryModel);

	void modifyParentId(Long id, Long parentId);

	void modifyV4Id(Long id, Long v4Id);

	long count(MergeUserQueryModel mergeUserQueryModel);

}
