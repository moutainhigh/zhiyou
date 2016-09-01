package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.act.ActivitySignIn;
import com.zy.model.query.ActivitySignInQueryModel;


public interface ActivitySignInMapper {

	int insert(ActivitySignIn activitySignIn);

	int update(ActivitySignIn activitySignIn);

	int merge(@Param("activitySignIn") ActivitySignIn activitySignIn, @Param("fields")String... fields);

	int delete(Long id);

	ActivitySignIn findOne(Long id);

	List<ActivitySignIn> findAll(ActivitySignInQueryModel activitySignInQueryModel);

	long count(ActivitySignInQueryModel activitySignInQueryModel);

	ActivitySignIn findByActivityIdAndUserId(@Param("activityId") Long activityId, @Param("userId") Long userId);

}