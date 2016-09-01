package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.act.ActivitySignIn;
import com.zy.model.query.ActivitySignInQueryModel;

public interface ActivitySignInService {

	Page<ActivitySignIn> findPage(ActivitySignInQueryModel activitySignInQueryModel);

}
