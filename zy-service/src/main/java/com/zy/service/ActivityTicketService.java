package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.act.ActivityTeamApply;
import com.zy.entity.act.ActivityTicket;
import com.zy.model.query.ActivityTeamApplyQueryModel;

import java.util.List;

public interface ActivityTicketService {

	//Page<ActivityTeamApply> findPage(ActivityTeamApplyQueryModel ActivityTeamApplyQueryModel);

	//List<ActivityTeamApply> findAll(ActivityTeamApplyQueryModel ActivityTeamApplyQueryModel);

	//ActivityTeamApply findByActivityIdAndBuyerId(Long activityId, Long userId);

	ActivityTicket findOne(Long id);

	ActivityTicket insert(ActivityTicket ActivityTicket);

	void update(ActivityTicket ActivityTicket);

//	Long findPayNumber(Long activityId);


//	void createAndPaid(Long activityId, Long userId);

//	void success(Long id, String outerSn);

//	void modifyPayerUserId(Long ActivityTeamApplyId, Long payerUserId);
}
