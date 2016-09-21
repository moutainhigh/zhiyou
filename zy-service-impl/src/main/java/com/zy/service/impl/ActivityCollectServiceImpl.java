package com.zy.service.impl;

import com.zy.common.model.query.Page;
import com.zy.entity.act.ActivityCollect;
import com.zy.mapper.ActivityCollectMapper;
import com.zy.model.query.ActivityCollectQueryModel;
import com.zy.service.ActivityCollectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import java.util.List;

@Service
@Validated
public class ActivityCollectServiceImpl implements ActivityCollectService {

	@Autowired
	private ActivityCollectMapper activityCollectMapper;

	@Override
	public Page<ActivityCollect> findPage(
			@NotNull ActivityCollectQueryModel activityCollectQueryModel) {
		if (activityCollectQueryModel.getPageNumber() == null)
			activityCollectQueryModel.setPageNumber(0);
		if (activityCollectQueryModel.getPageSize() == null)
			activityCollectQueryModel.setPageSize(20);
		long total = activityCollectMapper.count(activityCollectQueryModel);
		List<ActivityCollect> data = activityCollectMapper
				.findAll(activityCollectQueryModel);
		Page<ActivityCollect> page = new Page<>();
		page.setPageNumber(activityCollectQueryModel.getPageNumber());
		page.setPageSize(activityCollectQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public List<ActivityCollect> findAll(@NotNull ActivityCollectQueryModel activityCollectQueryModel) {
		return activityCollectMapper
				.findAll(activityCollectQueryModel);
	}

	@Override
	public ActivityCollect findByActivityIdAndUserId(@NotNull Long activityId, @NotNull Long userId) {
		return activityCollectMapper.findByActivityIdAndUserId(activityId, userId);
	}

}
