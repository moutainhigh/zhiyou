package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.act.ActivityApply;
import com.zy.model.query.ActivityApplyQueryModel;


public interface ActivityApplyMapper {

	int insert(ActivityApply activityApply);

	int update(ActivityApply activityApply);

	int merge(@Param("activityApply") ActivityApply activityApply, @Param("fields")String... fields);

	int delete(Long id);

	ActivityApply findOne(Long id);

	List<ActivityApply> findAll(ActivityApplyQueryModel activityApplyQueryModel);

	long count(ActivityApplyQueryModel activityApplyQueryModel);

	ActivityApply findByActivityIdAndUserId(@Param("activityId") Long activityId, @Param("userId") Long userId);

	Long queryCount(Long activityId);

	Long queryNoPayCount(Long activityId);

	List<ActivityApply> findAllByReport(ActivityReportQueryModel activityApplyQueryModel);

	long queryCountReport(ActivityReportQueryModel activityReportQueryModel);
}