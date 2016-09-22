package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.act.ActivityApply;
import com.zy.model.query.ActivityApplyQueryModel;

import java.util.List;

public interface ActivityApplyService {

	Page<ActivityApply> findPage(ActivityApplyQueryModel activityApplyQueryModel);

	List<ActivityApply> findAll(ActivityApplyQueryModel activityApplyQueryModel);
	
	ActivityApply findByActivityIdAndUserId(Long activityId, Long userId);

}
