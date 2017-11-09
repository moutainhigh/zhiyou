package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.mergeusr.MergeUser;
import com.zy.model.dto.UserTeamDto;
import com.zy.model.query.MergeUserQueryModel;
import com.zy.model.query.UserlongQueryModel;

import java.util.List;
import java.util.Map;

public interface MergeUserService {

	//MergeUser findOne(Long id);

	MergeUser findByUserIdAndProductType(Long userId,Integer productType);

	MergeUser findBycodeAndProductType(String code,Integer productType);

	long[] countdirTotal(Long userId,Integer productType);

	void create(MergeUser mergeUser);

	void createByUserId(Long userId,Integer productType);

	Page<MergeUser> findPage(MergeUserQueryModel mergeUserQueryModel);


	List<MergeUser> findAll(MergeUserQueryModel mergeUserQueryModel);

	void modifyParentId(Long id, Long parentId,Integer productType);

	void modifyV4Id(Long id, Long v4Id,Integer productType);

	long count(MergeUserQueryModel mergeUserQueryModel);

	Map<String,Object> countNewMemTotal(Long userId, boolean b);

	boolean findNewOne(Long id);

}
