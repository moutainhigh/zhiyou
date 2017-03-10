package com.zy.service.impl;

import com.zy.common.model.query.Page;
import com.zy.entity.act.ActivityApply;
import com.zy.mapper.ActivityApplyMapper;
import com.zy.model.query.ActivityApplyQueryModel;
import com.zy.service.ActivityApplyService;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@Service
@Validated
public class ActivityApplyServiceImpl implements ActivityApplyService {

	@Autowired
	private ActivityApplyMapper activityApplyMapper;

	@Override
	public Page<ActivityApply> findPage(
			@NotNull ActivityApplyQueryModel activityApplyQueryModel) {
		if (activityApplyQueryModel.getPageNumber() == null)
			activityApplyQueryModel.setPageNumber(0);
		if (activityApplyQueryModel.getPageSize() == null)
			activityApplyQueryModel.setPageSize(20);
		long total = activityApplyMapper.count(activityApplyQueryModel);
		List<ActivityApply> data = activityApplyMapper
				.findAll(activityApplyQueryModel);
		Page<ActivityApply> page = new Page<>();
		page.setPageNumber(activityApplyQueryModel.getPageNumber());
		page.setPageSize(activityApplyQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public List<ActivityApply> findAll(@NotNull ActivityApplyQueryModel activityApplyQueryModel) {
		return activityApplyMapper
				.findAll(activityApplyQueryModel);
	}

	@Override
	public ActivityApply findByActivityIdAndUserId(@NotNull Long activityId, @NotNull Long userId) {
		return activityApplyMapper.findByActivityIdAndUserId(activityId, userId);
	}

	@Override
	public ActivityApply findOne(@NotNull Long id) {
		return activityApplyMapper.findOne(id);
	}

	@Override
	public void success(@NotNull Long id, @NotBlank String outerSn) {
		ActivityApply activityApply = findOne(id);
		validate(activityApply, NOT_NULL, "activity apply id " + id + " not found");
		if (activityApply.getActivityApplyStatus() == ActivityApply.ActivityApplyStatus.已支付) {
			return;
		}

		activityApply.setActivityApplyStatus(ActivityApply.ActivityApplyStatus.已支付);
		activityApply.setOuterSn(outerSn);
		activityApplyMapper.update(activityApply);
	}

}
