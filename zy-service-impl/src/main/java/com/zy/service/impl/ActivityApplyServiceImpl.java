package com.zy.service.impl;

import com.zy.common.exception.BizException;
import com.zy.common.model.query.Page;
import com.zy.entity.act.ActivityApply;
import com.zy.entity.usr.User;
import com.zy.mapper.ActivityApplyMapper;
import com.zy.mapper.UserMapper;
import com.zy.model.BizCode;
import com.zy.model.query.ActivityApplyQueryModel;
import com.zy.service.ActivityApplyService;
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

	@Autowired
	private UserMapper userMapper;

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
	public void success(@NotNull Long id, String outerSn) {
		ActivityApply activityApply = findOne(id);
		validate(activityApply, NOT_NULL, "activity apply id " + id + " not found");
		if (activityApply.getActivityApplyStatus() == ActivityApply.ActivityApplyStatus.已支付) {
			return;
		}

		activityApply.setActivityApplyStatus(ActivityApply.ActivityApplyStatus.已支付);
		activityApply.setOuterSn(outerSn);
		activityApplyMapper.update(activityApply);
	}

	@Override
	public void modifyPayerUserId(@NotNull Long activityApplyId, @NotNull Long payerUserId) {
		User user = userMapper.findOne(payerUserId);
		validate(user, NOT_NULL, "user id " + payerUserId + " not found");
		if (user.getUserType() != User.UserType.代理) {
			throw new BizException(BizCode.ERROR, "代付人用户类型必须为代理");
		}

		ActivityApply activityApply = findOne(activityApplyId);
		validate(activityApply, NOT_NULL, "activity apply id " + activityApplyId + " not found");
		if (activityApply.getUserId().equals(payerUserId)) {
			throw new BizException(BizCode.ERROR, "请输入他人手机号");
		}

		activityApply.setPayerUserId(payerUserId);
		activityApplyMapper.update(activityApply);
	}

}
