package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.act.ActivityApply;
import com.zy.model.query.ActivityApplyQueryModel;

public interface ActivityApplyService {

	Page<ActivityApply> findPage(ActivityApplyQueryModel activityApplyQueryModel);

}
