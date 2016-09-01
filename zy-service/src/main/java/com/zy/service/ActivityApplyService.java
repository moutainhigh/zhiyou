package com.gc.service;

import com.zy.common.model.query.Page;
import com.gc.entity.act.ActivityApply;
import com.gc.model.query.ActivityApplyQueryModel;

public interface ActivityApplyService {

	Page<ActivityApply> findPage(ActivityApplyQueryModel activityApplyQueryModel);

}
