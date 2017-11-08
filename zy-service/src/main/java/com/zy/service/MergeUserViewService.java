package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.mergeusr.MergeUserView;
import com.zy.model.dto.UserDto;
import com.zy.model.query.MergeUserViewQueryModel;
import com.zy.model.query.UserQueryModel;

import java.util.List;
import java.util.Map;

public interface MergeUserViewService {

	MergeUserView findOne(Long id);

	Page<MergeUserView> findPage(MergeUserViewQueryModel mergeUserViewQueryModel);

	Page<MergeUserView> findAllPage(MergeUserViewQueryModel mergeUserViewQueryModel);

	List<MergeUserView> findAll(MergeUserViewQueryModel mergeUserViewQueryModel);

	long count(MergeUserViewQueryModel mergeUserViewQueryModel);

	Map<String,Object> findNewSup(long[] ids);

	long countByActive(MergeUserViewQueryModel mergeUserViewQueryModel);

	Page<MergeUserView> findActive(MergeUserViewQueryModel mergeUserViewQueryModel, boolean b);

	List<UserDto> findUserAll(UserQueryModel userQueryModel);

	long countUserAll(UserQueryModel userQueryModel);

	Page<MergeUserView> findAddpeople(UserQueryModel userQueryModel);

}
