package com.zy.service.impl;

import com.zy.common.model.query.Page;
import com.zy.entity.act.ActivityTicket;
import com.zy.mapper.ActivityTicketMapper;
import com.zy.model.query.ActivityTicketQueryModel;
import com.zy.service.ActivityTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@Validated
public class ActivityTicketServiceImpl implements ActivityTicketService {

	@Autowired
	private ActivityTicketMapper ActivityTicketMapper;

	@Override
	public Page<ActivityTicket> findPage(
			@NotNull ActivityTicketQueryModel ActivityTicketQueryModel) {
		if (ActivityTicketQueryModel.getPageNumber() == null)
			ActivityTicketQueryModel.setPageNumber(0);
		if (ActivityTicketQueryModel.getPageSize() == null)
			ActivityTicketQueryModel.setPageSize(20);
		long total = ActivityTicketMapper.count(ActivityTicketQueryModel);
		List<ActivityTicket> data = ActivityTicketMapper
				.findAll(ActivityTicketQueryModel);
		Page<ActivityTicket> page = new Page<>();
		page.setPageNumber(ActivityTicketQueryModel.getPageNumber());
		page.setPageSize(ActivityTicketQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public List<ActivityTicket> findAll(@NotNull ActivityTicketQueryModel ActivityTicketQueryModel) {
		return ActivityTicketMapper.findAll(ActivityTicketQueryModel);
	}

	@Override
	public ActivityTicket findOne(@NotNull Long id) {
		return ActivityTicketMapper.findOne(id);
	}

	@Override
	public int update(ActivityTicket activityTicket) {
		return ActivityTicketMapper.update(activityTicket);
	}

	@Override
	public ActivityTicket insert(ActivityTicket activityTicket) {
		ActivityTicketMapper.insert(activityTicket);
		//Long id = activityTicket.getId();
		return activityTicket;
	}

}
