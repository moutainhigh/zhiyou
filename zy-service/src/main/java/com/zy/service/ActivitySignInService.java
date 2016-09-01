package com.gc.service;

import com.zy.common.model.query.Page;
import com.gc.entity.act.ActivitySignIn;
import com.gc.model.query.ActivitySignInQueryModel;

public interface ActivitySignInService {

	Page<ActivitySignIn> findPage(ActivitySignInQueryModel activitySignInQueryModel);

}
