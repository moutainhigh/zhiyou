package com.zy.mapper;


import com.zy.entity.act.ActivityApply;
import com.zy.entity.act.ActivityTeamApply;
import com.zy.model.query.ActivityApplyQueryModel;
import com.zy.model.query.ActivityTeamApplyQueryModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ActivityTeamApplyMapper {

	Long insert(ActivityTeamApply activityTeamApply);

	int update(ActivityTeamApply activityTeamApply);

	int delete(Long id);

	ActivityTeamApply findOne(Long id);

	List<ActivityTeamApply> findAll(ActivityTeamApplyQueryModel activityTeamApplyQueryModel);

	long count(ActivityTeamApplyQueryModel activityTeamApplyQueryModel);

	ActivityTeamApply findByActivityIdAndBuyerId(@Param("activityId") Long activityId, @Param("buyerId") Long buyerId);

	Long findPayNumber(Long activityId);

}