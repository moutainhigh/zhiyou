package com.gc.service;

import com.zy.common.model.query.Page;
import com.gc.entity.act.ActivityCollect;
import com.gc.model.query.ActivityCollectQueryModel;

public interface ActivityCollectService {

	Page<ActivityCollect> findPage(ActivityCollectQueryModel activityCollectQueryModel);

}
