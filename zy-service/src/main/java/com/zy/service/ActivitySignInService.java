package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.act.ActivitySignIn;
import com.zy.model.query.ActivitySignInQueryModel;

import java.util.List;

public interface ActivitySignInService {

	Page<ActivitySignIn> findPage(ActivitySignInQueryModel activitySignInQueryModel);

	List<ActivitySignIn> findAll(ActivitySignInQueryModel activitySignInQueryModel);

}
