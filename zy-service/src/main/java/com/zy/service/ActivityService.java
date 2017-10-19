package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.act.Activity;
import com.zy.model.query.ActivityQueryModel;

import java.util.List;

public interface ActivityService {

	Activity create(Activity activity);

	void view(Long activityId);

	void release(Long activityId, boolean isReleased, Long userId);

	void modify(Activity activity);

	void apply(Long activityId, Long userId, Long inviterId);

	void cancel(Long activityId, Long userId);

	void collect(Long activityId, Long userId);

	void uncollect(Long activityId, Long userId);

	void signIn(Long activityId, Long userId);

	Activity findOne(Long activityId);

	Page<Activity> findPage(ActivityQueryModel activityQueryModel);

	Page<Activity> findReport(ActivityQueryModel activityQueryModel);

	List<Activity> findExReport(ActivityQueryModel activityQueryModel);
}
