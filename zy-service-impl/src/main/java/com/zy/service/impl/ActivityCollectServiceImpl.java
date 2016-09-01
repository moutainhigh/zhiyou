package com.gc.service.impl;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.zy.common.model.query.Page;
import com.gc.entity.act.ActivityCollect;
import com.gc.mapper.ActivityCollectMapper;
import com.gc.model.query.ActivityCollectQueryModel;
import com.gc.service.ActivityCollectService;

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

}
