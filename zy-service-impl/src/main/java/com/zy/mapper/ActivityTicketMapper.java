package com.zy.mapper;


import com.zy.entity.act.ActivityTeamApply;
import com.zy.entity.act.ActivityTicket;
import com.zy.model.query.ActivityTeamApplyQueryModel;
import com.zy.model.query.ActivityTicketQueryModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ActivityTicketMapper {

	int insert (ActivityTicket activityTicket);

	int update(ActivityTicket activityTicket);

	int delete(Long id);

	ActivityTicket findOne(Long id);

	List<ActivityTicket> findAll(ActivityTicketQueryModel activityTicketQueryModel);

	long count(ActivityTicketQueryModel activityTicketQueryModel);



}