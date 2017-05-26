package com.zy.service.impl;

import com.zy.common.model.query.Page;
import com.zy.entity.act.ActivityTicket;
import com.zy.mapper.ActivityMapper;
import com.zy.mapper.ActivityTicketMapper;
import com.zy.mapper.PaymentMapper;
import com.zy.mapper.UserMapper;
import com.zy.service.ActivityTicketService;
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

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private ActivityMapper activityMapper;

	@Autowired
	private PaymentMapper paymentMapper;

//	@Override
//	public Page<ActivityTicket> findPage(
//			@NotNull ActivityTicketQueryModel ActivityTicketQueryModel) {
//		if (ActivityTicketQueryModel.getPageNumber() == null)
//			ActivityTicketQueryModel.setPageNumber(0);
//		if (ActivityTicketQueryModel.getPageSize() == null)
//			ActivityTicketQueryModel.setPageSize(20);
//		long total = ActivityTicketMapper.count(ActivityTicketQueryModel);
//		List<ActivityTicket> data = ActivityTicketMapper
//				.findAll(ActivityTicketQueryModel);
//		Page<ActivityTicket> page = new Page<>();
//		page.setPageNumber(ActivityTicketQueryModel.getPageNumber());
//		page.setPageSize(ActivityTicketQueryModel.getPageSize());
//		page.setData(data);
//		page.setTotal(total);
//		return page;
//	}
//
//	@Override
//	public List<ActivityTicket> findAll(@NotNull ActivityTicketQueryModel ActivityTicketQueryModel) {
//		return ActivityTicketMapper
//				.findAll(ActivityTicketQueryModel);
//	}
//
//	@Override
//	public ActivityTicket findByActivityIdAndBuyerId(@NotNull Long activityId, @NotNull Long userId) {
//		return ActivityTicketMapper.findByActivityIdAndBuyerId(activityId, userId);
//	}

	@Override
	public ActivityTicket findOne(@NotNull Long id) {
		return ActivityTicketMapper.findOne(id);
	}

	@Override
	public void update(ActivityTicket activityTicket) {
		ActivityTicketMapper.update(activityTicket);
	}

	@Override
	public ActivityTicket insert(ActivityTicket activityTicket) {
		ActivityTicketMapper.insert(activityTicket);
		//Long id = activityTicket.getId();
		return activityTicket;
	}

//	@Override
//	public Long findPayNumber(Long activityId) {
//		return ActivityTicketMapper.findPayNumber(activityId);
//	}

//	@Override
//	public void createAndPaid(@NotNull Long activityId, @NotNull Long userId) {
//		Activity activity = activityMapper.findOne(activityId);
//		validate(activity, NOT_NULL, "activity id " + userId + " not found");
//
//		User user = userMapper.findOne(userId);
//		validate(user, NOT_NULL, "user id " + userId + " not found");
//
//		ActivityTicket persistence = ActivityTicketMapper.findByActivityIdAndBuyerId(activityId, userId);
//		if (persistence != null) {
//			if (persistence.getActivityTicketStatus() == ActivityTicket.ActivityTicketStatus.已报名) {
//				persistence.setActivityTicketStatus(ActivityTicket.ActivityTicketStatus.已支付);
//				ActivityTicketMapper.update(persistence);
//			}
//			return ;
//		}
//
//		ActivityTicket ActivityTicket = new ActivityTicket();
//		ActivityTicket.setActivityTicketStatus(ActivityTicket.ActivityTicketStatus.已支付);
//		ActivityTicket.setActivityId(activityId);
//		ActivityTicket.setAmount(new BigDecimal("0.00"));
//		ActivityTicket.setAppliedTime(new Date());
//		ActivityTicket.setIsCancelled(false);
//		ActivityTicket.setIsSmsSent(false);
//		ActivityTicket.setUserId(userId);
//		validate(ActivityTicket);
//		ActivityTicketMapper.insert(ActivityTicket);
//	}
//
//	@Override
//	public void success(@NotNull Long id, String outerSn) {
//		ActivityTicket ActivityTicket = findOne(id);
//		validate(ActivityTicket, NOT_NULL, "activity apply id " + id + " not found");
//		if (ActivityTicket.getActivityTicketStatus() == ActivityTicket.ActivityTicketStatus.已支付) {
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
//		ActivityTicket.setActivityTicketStatus(ActivityTicket.ActivityTicketStatus.已支付);
//		ActivityTicket.setOuterSn(outerSn);
//		ActivityTicketMapper.update(ActivityTicket);
//	}

//	@Override
//	public void modifyPayerUserId(@NotNull Long ActivityTicketId, @NotNull Long payerUserId) {
//		User user = userMapper.findOne(payerUserId);
//		validate(user, NOT_NULL, "user id " + payerUserId + " not found");
//		if (user.getUserType() != User.UserType.代理) {
//			throw new BizException(BizCode.ERROR, "代付人用户类型必须为代理");
//		}
//
//		ActivityTicket ActivityTicket = findOne(ActivityTicketId);
//		validate(ActivityTicket, NOT_NULL, "activity apply id " + ActivityTicketId + " not found");
//		if (ActivityTicket.getUserId().equals(payerUserId)) {
//			throw new BizException(BizCode.ERROR, "请输入他人手机号");
//		}
//
//		ActivityTicket.setPayerUserId(payerUserId);
//		ActivityTicketMapper.update(ActivityTicket);
//	}

}
