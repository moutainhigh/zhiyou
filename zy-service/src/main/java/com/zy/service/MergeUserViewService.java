package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.mergeusr.MergeUser;
import com.zy.entity.mergeusr.MergeUserView;
import com.zy.model.dto.UserTeamDto;
import com.zy.model.query.MergeUserQueryModel;
import com.zy.model.query.MergeUserViewQueryModel;
import com.zy.model.query.UserlongQueryModel;

import java.util.List;
import java.util.Map;

public interface MergeUserViewService {

	MergeUserView findOne(Long id);

	Page<MergeUserView> findPage(MergeUserViewQueryModel mergeUserViewQueryModel);

	List<MergeUserView> findAll(MergeUserViewQueryModel mergeUserViewQueryModel);

	long count(MergeUserViewQueryModel mergeUserViewQueryModel);

}
