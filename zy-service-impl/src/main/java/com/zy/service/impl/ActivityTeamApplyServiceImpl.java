package com.zy.service.impl;

import com.zy.common.model.query.Page;
import com.zy.entity.act.ActivityTeamApply;
import com.zy.mapper.ActivityMapper;
import com.zy.mapper.ActivityTeamApplyMapper;
import com.zy.mapper.PaymentMapper;
import com.zy.mapper.UserMapper;
import com.zy.model.query.ActivityTeamApplyQueryModel;
import com.zy.service.ActivityTeamApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@Validated
public class ActivityTeamApplyServiceImpl implements ActivityTeamApplyService {


	@Autowired
	private ActivityTeamApplyMapper activityTeamApplyMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private ActivityMapper activityMapper;

	@Autowired
	private PaymentMapper paymentMapper;

	@Override
	public Page<ActivityTeamApply> findPage(
			@NotNull ActivityTeamApplyQueryModel activityTeamApplyQueryModel) {
		if (activityTeamApplyQueryModel.getPageNumber() == null)
			activityTeamApplyQueryModel.setPageNumber(0);
		if (activityTeamApplyQueryModel.getPageSize() == null)
			activityTeamApplyQueryModel.setPageSize(20);
		long total = activityTeamApplyMapper.count(activityTeamApplyQueryModel);
		List<ActivityTeamApply> data = activityTeamApplyMapper
				.findAll(activityTeamApplyQueryModel);
		Page<ActivityTeamApply> page = new Page<>();
		page.setPageNumber(activityTeamApplyQueryModel.getPageNumber());
		page.setPageSize(activityTeamApplyQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public List<ActivityTeamApply> findAll(@NotNull ActivityTeamApplyQueryModel activityTeamApplyQueryModel) {
		return activityTeamApplyMapper
				.findAll(activityTeamApplyQueryModel);
	}

	@Override
	public ActivityTeamApply findByActivityIdAndBuyerId(@NotNull Long activityId, @NotNull Long userId) {
		return activityTeamApplyMapper.findByActivityIdAndBuyerId(activityId, userId);
	}

	@Override
	public ActivityTeamApply findOne(@NotNull Long id) {
		return activityTeamApplyMapper.findOne(id);
	}

	@Override
	public void insert(ActivityTeamApply activityTeamApply) {
		activityTeamApplyMapper.insert(activityTeamApply);
	}

//	@Override
//	public void success(@NotNull Long id, String outerSn) {
//		ActivityTeamApply ActivityTeamApply = findOne(id);
//		validate(ActivityTeamApply, NOT_NULL, "activity apply id " + id + " not found");
//		if (ActivityTeamApply.getActivityTeamApplyStatus() == ActivityTeamApply.ActivityTeamApplyStatus.已支付) {
//			return;
//		}
//
//		List<Payment> payments = paymentMapper.findByRefId(id);
//		payments = payments.stream().filter(v -> v.getPaymentStatus() == Payment.PaymentStatus.待支付)
//				.filter(v -> v.getPaymentType() == Payment.PaymentType.活动报名).collect(Collectors.toList());
//		for(Payment payment : payments) {
//			payment.setPaymentStatus(Payment.PaymentStatus.已支付);
//			payment.setPaidTime(new Date());
//			if (paymentMapper.update(payment) == 0) {
//				throw new ConcurrentException();
//			}
//		}
//
//		ActivityTeamApply.setActivityTeamApplyStatus(ActivityTeamApply.ActivityTeamApplyStatus.已支付);
//		ActivityTeamApply.setOuterSn(outerSn);
//		ActivityTeamApplyMapper.update(ActivityTeamApply);
//	}


}
