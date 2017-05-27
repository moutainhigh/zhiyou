package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.act.ActivityTeamApply;
import com.zy.entity.act.ActivityTicket;
import com.zy.model.query.ActivityTeamApplyQueryModel;
import com.zy.model.query.ActivityTicketQueryModel;

import java.util.List;

public interface ActivityTicketService {

	Page<ActivityTicket> findPage(ActivityTicketQueryModel ActivityTicketQueryModel);

	List<ActivityTicket> findAll(ActivityTicketQueryModel ActivityTicketQueryModel);


	ActivityTicket findOne(Long id);

	ActivityTicket insert(ActivityTicket ActivityTicket);

	int update(ActivityTicket ActivityTicket);

}
