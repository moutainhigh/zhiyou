package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.mergeusr.MergeUserView;
import com.zy.model.query.MergeUserViewQueryModel;

import java.util.List;
import java.util.Map;

public interface MergeUserViewService {

	MergeUserView findOne(Long id);

	Page<MergeUserView> findPage(MergeUserViewQueryModel mergeUserViewQueryModel);

	List<MergeUserView> findAll(MergeUserViewQueryModel mergeUserViewQueryModel);

	long count(MergeUserViewQueryModel mergeUserViewQueryModel);

	Map<String,Object> findNewSup(long[] ids);

	long countByActive(MergeUserViewQueryModel mergeUserViewQueryModel);

	Page<MergeUserView> findActive(MergeUserViewQueryModel mergeUserViewQueryModel, boolean b);

}
