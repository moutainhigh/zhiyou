package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.act.ActivityTeamApply;
import com.zy.model.query.ActivityTeamApplyQueryModel;

import java.util.List;

public interface ActivityTeamApplyService {

	Page<ActivityTeamApply> findPage(ActivityTeamApplyQueryModel ActivityTeamApplyQueryModel);

	List<ActivityTeamApply> findAll(ActivityTeamApplyQueryModel ActivityTeamApplyQueryModel);

	ActivityTeamApply findOne(Long id);

	Long insert(ActivityTeamApply activityTeamApply);

	Long findPayNumber(Long activityId);

	int update(ActivityTeamApply activityTeamApply);

}
