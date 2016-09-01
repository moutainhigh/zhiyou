package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.act.ActivityCollect;
import com.zy.model.query.ActivityCollectQueryModel;

public interface ActivityCollectService {

	Page<ActivityCollect> findPage(ActivityCollectQueryModel activityCollectQueryModel);

}
