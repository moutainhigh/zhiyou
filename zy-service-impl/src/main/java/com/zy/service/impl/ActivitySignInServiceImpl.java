package com.gc.service.impl;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.zy.common.model.query.Page;
import com.gc.entity.act.ActivitySignIn;
import com.gc.mapper.ActivitySignInMapper;
import com.gc.model.query.ActivitySignInQueryModel;
import com.gc.service.ActivitySignInService;

@Service
@Validated
public class ActivitySignInServiceImpl implements ActivitySignInService {

	@Autowired
	private ActivitySignInMapper activitySignInMapper;

	@Override
	public Page<ActivitySignIn> findPage(
			@NotNull ActivitySignInQueryModel activitySignInQueryModel) {
		if (activitySignInQueryModel.getPageNumber() == null)
			activitySignInQueryModel.setPageNumber(0);
		if (activitySignInQueryModel.getPageSize() == null)
			activitySignInQueryModel.setPageSize(20);
		long total = activitySignInMapper.count(activitySignInQueryModel);
		List<ActivitySignIn> data = activitySignInMapper
				.findAll(activitySignInQueryModel);
		Page<ActivitySignIn> page = new Page<>();
		page.setPageNumber(activitySignInQueryModel.getPageNumber());
		page.setPageSize(activitySignInQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

}
