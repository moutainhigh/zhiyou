package com.zy.service.impl;

import com.zy.common.model.query.Page;
import com.zy.entity.act.ActivityTeamApply;
import com.zy.mapper.ActivityTeamApplyMapper;
import com.zy.model.query.ActivityTeamApplyQueryModel;
import com.zy.service.ActivityTeamApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@Validated
public class ActivityTeamApplyServiceImpl implements ActivityTeamApplyService {

	@Autowired
	private ActivityTeamApplyMapper activityTeamApplyMapper;

	@Override
	public Page<ActivityTeamApply> findPage(
			@NotNull ActivityTeamApplyQueryModel activityTeamApplyQueryModel) {
		if (activityTeamApplyQueryModel.getPageNumber() == null)
			activityTeamApplyQueryModel.setPageNumber(0);
		if (activityTeamApplyQueryModel.getPageSize() == null)
			activityTeamApplyQueryModel.setPageSize(20);
		long total = activityTeamApplyMapper.count(activityTeamApplyQueryModel);
		List<ActivityTeamApply> data = activityTeamApplyMapper
				.findAll(activityTeamApplyQueryModel);
		Page<ActivityTeamApply> page = new Page<>();
		page.setPageNumber(activityTeamApplyQueryModel.getPageNumber());
		page.setPageSize(activityTeamApplyQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public List<ActivityTeamApply> findAll(@NotNull ActivityTeamApplyQueryModel activityTeamApplyQueryModel) {
		return activityTeamApplyMapper
				.findAll(activityTeamApplyQueryModel);
	}

	@Override
	public ActivityTeamApply findOne(@NotNull Long id) {
		return activityTeamApplyMapper.findOne(id);
	}

	@Override
	public Long insert(ActivityTeamApply activityTeamApply) {
		activityTeamApplyMapper.insert(activityTeamApply);
		Long id = activityTeamApply.getId();
		return id;
	}

	@Override
	public Long findPayNumber(Long activityId) {
		Long count = activityTeamApplyMapper.findPayNumber(activityId);
		return count == null ? 0L : count;
	}

	@Override
	public int update(ActivityTeamApply activityTeamApply) {
		return activityTeamApplyMapper.update(activityTeamApply);
	}

}
