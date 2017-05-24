package com.zy.service.impl;

import com.zy.common.model.query.Page;
import com.zy.entity.act.ActivityTeamApply;
import com.zy.mapper.*;
import com.zy.mapper.ActivityTeamApplyMapper;
import com.zy.model.query.ActivityTeamApplyQueryModel;
import com.zy.service.ActivityTeamApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.zy.common.util.ValidateUtils.validate;

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
	public Long insert(ActivityTeamApply activityTeamApply) {
		activityTeamApplyMapper.insert(activityTeamApply);
		Long id = activityTeamApply.getId();
		return id;
	}

	@Override
	public Long findPayNumber(Long activityId) {
		return activityTeamApplyMapper.findPayNumber(activityId);
	}

//	@Override
//	public void createAndPaid(@NotNull Long activityId, @NotNull Long userId) {
//		Activity activity = activityMapper.findOne(activityId);
//		validate(activity, NOT_NULL, "activity id " + userId + " not found");
//
//		User user = userMapper.findOne(userId);
//		validate(user, NOT_NULL, "user id " + userId + " not found");
//
//		ActivityTeamApply persistence = activityTeamApplyMapper.findByActivityIdAndBuyerId(activityId, userId);
//		if (persistence != null) {
//			if (persistence.getActivityTeamApplyStatus() == ActivityTeamApply.ActivityTeamApplyStatus.已报名) {
//				persistence.setActivityTeamApplyStatus(ActivityTeamApply.ActivityTeamApplyStatus.已支付);
//				ActivityTeamApplyMapper.update(persistence);
//			}
//			return ;
//		}
//
//		ActivityTeamApply ActivityTeamApply = new ActivityTeamApply();
//		ActivityTeamApply.setActivityTeamApplyStatus(ActivityTeamApply.ActivityTeamApplyStatus.已支付);
//		ActivityTeamApply.setActivityId(activityId);
//		ActivityTeamApply.setAmount(new BigDecimal("0.00"));
//		ActivityTeamApply.setAppliedTime(new Date());
//		ActivityTeamApply.setIsCancelled(false);
//		ActivityTeamApply.setIsSmsSent(false);
//		ActivityTeamApply.setUserId(userId);
//		validate(ActivityTeamApply);
//		ActivityTeamApplyMapper.insert(ActivityTeamApply);
//	}
//
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

//	@Override
//	public void modifyPayerUserId(@NotNull Long ActivityTeamApplyId, @NotNull Long payerUserId) {
//		User user = userMapper.findOne(payerUserId);
//		validate(user, NOT_NULL, "user id " + payerUserId + " not found");
//		if (user.getUserType() != User.UserType.代理) {
//			throw new BizException(BizCode.ERROR, "代付人用户类型必须为代理");
//		}
//
//		ActivityTeamApply ActivityTeamApply = findOne(ActivityTeamApplyId);
//		validate(ActivityTeamApply, NOT_NULL, "activity apply id " + ActivityTeamApplyId + " not found");
//		if (ActivityTeamApply.getUserId().equals(payerUserId)) {
//			throw new BizException(BizCode.ERROR, "请输入他人手机号");
//		}
//
//		ActivityTeamApply.setPayerUserId(payerUserId);
//		ActivityTeamApplyMapper.update(ActivityTeamApply);
//	}

}
