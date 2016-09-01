package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.act.Activity;
import com.zy.model.query.ActivityQueryModel;


public interface ActivityMapper {

	int insert(Activity activity);

	int update(Activity activity);

	int merge(@Param("activity") Activity activity, @Param("fields")String... fields);

	int delete(Long id);

	Activity findOne(Long id);

	List<Activity> findAll(ActivityQueryModel activityQueryModel);

	long count(ActivityQueryModel activityQueryModel);

	int view(Long id);

}