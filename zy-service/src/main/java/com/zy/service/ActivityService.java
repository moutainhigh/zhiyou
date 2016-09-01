package com.gc.service;

import com.zy.common.model.query.Page;
import com.gc.entity.act.Activity;
import com.gc.model.query.ActivityQueryModel;

public interface ActivityService {

	Activity create(Activity activity);

	void view(Long activityId);

	void release(Long activityId, boolean isReleased);

	void modify(Activity activity);

	void apply(Long activityId, Long userId);

	void cancel(Long activityId, Long userId);

	void collect(Long activityId, Long userId);

	void uncollect(Long activityId, Long userId);

	void signIn(Long activityId, Long userId);

	Activity findOne(Long activityId);

	Page<Activity> findPage(ActivityQueryModel activityQueryModel);
}
