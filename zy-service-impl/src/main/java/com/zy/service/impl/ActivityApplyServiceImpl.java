package com.zy.service.impl;

import com.zy.common.exception.BizException;
import com.zy.common.exception.ConcurrentException;
import com.zy.common.model.query.Page;
import com.zy.entity.act.Activity;
import com.zy.entity.act.ActivityApply;
import com.zy.entity.act.ActivitySignIn;
import com.zy.entity.act.ActivityTicket;
import com.zy.entity.fnc.Payment;
import com.zy.entity.usr.User;
import com.zy.mapper.*;
import com.zy.model.BizCode;
import com.zy.model.query.*;
import com.zy.service.ActivityApplyService;
import io.gd.generator.api.query.Direction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@Service
@Validated
public class ActivityApplyServiceImpl implements ActivityApplyService {

	@Autowired
	private ActivityApplyMapper activityApplyMapper;

	@Autowired
	private ActivityMapper activityMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private PaymentMapper paymentMapper;

	@Autowired
	private ActivitySignInMapper activitySignInMapper;

	@Autowired
	private ActivityTicketMapper activityTicketMapper;

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
	public void createAndPaid(@NotNull Long activityId, @NotNull Long userId) {
		Activity activity = activityMapper.findOne(activityId);
		validate(activity, NOT_NULL, "activity id " + userId + " not found");

		User user = userMapper.findOne(userId);
		validate(user, NOT_NULL, "user id " + userId + " not found");

		ActivityApply persistence = activityApplyMapper.findByActivityIdAndUserId(activityId, userId);
		if (persistence != null) {
			if (persistence.getActivityApplyStatus() == ActivityApply.ActivityApplyStatus.已报名) {
				persistence.setActivityApplyStatus(ActivityApply.ActivityApplyStatus.已支付);
				activityApplyMapper.update(persistence);
			}
			return ;
		}

		ActivityApply activityApply = new ActivityApply();
		activityApply.setActivityApplyStatus(ActivityApply.ActivityApplyStatus.已支付);
		activityApply.setActivityId(activityId);
		activityApply.setAmount(new BigDecimal("0.00"));
		activityApply.setAppliedTime(new Date());
		activityApply.setIsCancelled(false);
		activityApply.setIsSmsSent(false);
		activityApply.setUserId(userId);
		validate(activityApply);
		activityApplyMapper.insert(activityApply);
	}

	/**
	 * 识别二维码报名活动
	 * @param activityId
	 * @param userId
	 * @param inviterId
     */
	@Override
	public void useTicket(@NotNull Long activityId, @NotNull Long userId, @NotNull Long inviterId ,@NotNull ActivityTicket activityTicket) {
		Activity activity = activityMapper.findOne(activityId);

		validate(activity, NOT_NULL, "activity id " + userId + " not found");

		User user = userMapper.findOne(userId);
		validate(user, NOT_NULL, "user id " + userId + " not found");
		//TODO
		Long appliedCount = activity.getAppliedCount();
		ActivityApply activityApply = activityApplyMapper.findByActivityIdAndUserId(activityId, userId);
		if (activityApply != null) {
			if (activityApply.getActivityApplyStatus() == ActivityApply.ActivityApplyStatus.已报名) {
				activityApply.setActivityApplyStatus(ActivityApply.ActivityApplyStatus.已支付);
				activityApply.setInviterId(inviterId);
				activityApplyMapper.update(activityApply);
			}
			if (activityApply.getIsCancelled()) {
				activity.setAppliedCount(appliedCount + 1);
				activityApply.setIsCancelled(false);
				if (activityMapper.update(activity) == 0) {
					throw new ConcurrentException();
				}
				activityApplyMapper.update(activityApply);
			}
			return ;
		} else {
			if (!activity.getIsReleased()) {
				throw new BizException(BizCode.ERROR, "活动暂未开放不能报名");
			}

//			if (activity.getApplyDeadline().before(new Date())) {
//				throw new BizException(BizCode.ERROR, "活动已经停止报名");
//			}

			activity.setAppliedCount(appliedCount + 1);
			if (activityMapper.update(activity) == 0) {
				throw new ConcurrentException();
			}
			ActivityApply newActivityApply = new ActivityApply();
			newActivityApply.setActivityApplyStatus(ActivityApply.ActivityApplyStatus.已支付);
			newActivityApply.setActivityId(activityId);
			newActivityApply.setAmount(activity.getAmount());
			newActivityApply.setAppliedTime(new Date());
			newActivityApply.setIsCancelled(false);
			newActivityApply.setIsSmsSent(false);
			newActivityApply.setUserId(userId);
			newActivityApply.setInviterId(inviterId);
			validate(newActivityApply);
			activityApplyMapper.insert(newActivityApply);
			activityTicket.setIsUsed(1);
			activityTicket.setUserId(userId);
			activityTicketMapper.update(activityTicket);
		}
	}

	@Override
	public void success(@NotNull Long id, String outerSn) {
		ActivityApply activityApply = findOne(id);
		validate(activityApply, NOT_NULL, "activity apply id " + id + " not found");
		if (activityApply.getActivityApplyStatus() == ActivityApply.ActivityApplyStatus.已支付) {
			return;
		}

		List<Payment> payments = paymentMapper.findByRefId(id);
		payments = payments.stream().filter(v -> v.getPaymentStatus() == Payment.PaymentStatus.待支付)
				.filter(v -> v.getPaymentType() == Payment.PaymentType.活动报名).collect(Collectors.toList());
		for(Payment payment : payments) {
			payment.setPaymentStatus(Payment.PaymentStatus.已支付);
			payment.setPaidTime(new Date());
			if (paymentMapper.update(payment) == 0) {
				throw new ConcurrentException();
			}
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

	@Override
	public int update(ActivityApply activityApply) {
		return activityApplyMapper.update(activityApply);
	}

	@Override
	public void editApplyAndTicket(ActivityApply activityApply, ActivityTicket activityTicket) {

		activityApplyMapper.update(activityApply);
		activityTicketMapper.update(activityTicket);
	}

	@Override
	public Long queryCount(Long activityId) {
		return  activityApplyMapper.queryCount(activityId);
	}

	@Override
	public Long queryNoPayCount(Long activityId) {
		return  activityApplyMapper.queryNoPayCount(activityId);
	}

	/**
	 * 查询报活动表数据
	 * @param activityReportQueryModel
	 * @return
	 */
	@Override
	public Map<String,Object> findPageByReport(ActivityReportQueryModel activityReportQueryModel) {
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<Activity>  activityList=new ArrayList<Activity>();
		List<ActivityApply> data = new ArrayList<ActivityApply>();//查询数据
		long total=0L;//总条数
		boolean flag = true;
		if (null!=activityReportQueryModel.getActivityAddress()||null!=activityReportQueryModel.getActivityName()||null!=activityReportQueryModel.getActivityTime()){
			ActivityQueryModel activityQueryModel = new ActivityQueryModel();
			activityQueryModel.setTitleLK(activityReportQueryModel.getActivityName());
			activityQueryModel.setStartTimeGTE(activityReportQueryModel.getActivityTime());
			activityQueryModel.setAddress(activityReportQueryModel.getActivityAddress());
			activityList= activityMapper.findAll(activityQueryModel);
			if(activityList==null||activityList.isEmpty()){
				flag=false;
			}
		}
		List<User> userList = new ArrayList<User>();
		if(null!=activityReportQueryModel.getUserName()||null!=activityReportQueryModel.getUserLevel()||null!=activityReportQueryModel.getUserPhone() ) {
			UserQueryModel userQueryModel = new UserQueryModel();
			if ("V0".equals(activityReportQueryModel.getUserLevel())) {
				userQueryModel.setUserRankEQ(User.UserRank.V0);
			} else if ("V1".equals(activityReportQueryModel.getUserLevel())) {
				userQueryModel.setUserRankEQ(User.UserRank.V1);
			} else if ("V2".equals(activityReportQueryModel.getUserLevel())) {
				userQueryModel.setUserRankEQ(User.UserRank.V2);
			} else if ("V3".equals(activityReportQueryModel.getUserLevel())) {
				userQueryModel.setUserRankEQ(User.UserRank.V3);
			} else if ("V4".equals(activityReportQueryModel.getUserLevel())) {
				userQueryModel.setUserRankEQ(User.UserRank.V4);
			}
			userQueryModel.setNicknameLK(activityReportQueryModel.getUserName());
			userQueryModel.setPhoneEQ(activityReportQueryModel.getUserPhone());
			userList= userMapper.findAll(userQueryModel);
			if(userList==null||userList.isEmpty()){
				flag=false;
			}
		}
		if (flag) {
			activityReportQueryModel.setActivityIdIN(activityList.stream().map(v -> v.getId()).toArray(Long[]::new));
			activityReportQueryModel.setUserIdIN(userList.stream().map(v -> v.getId()).toArray(Long[]::new));
			activityReportQueryModel.setOrderBy("id");
			activityReportQueryModel.setDirection(Direction.ASC);
			ActivitySignInQueryModel activitySignInQueryModel = new ActivitySignInQueryModel(); //构建查询参数
			activitySignInQueryModel.setActivityIdIN((activityReportQueryModel.getActivityIdIN() == null || activityReportQueryModel.getActivityIdIN().length == 0) ? null : activityReportQueryModel.getActivityIdIN());
			activitySignInQueryModel.setUserIdIN((activityReportQueryModel.getUserIdIN() == null || activityReportQueryModel.getUserIdIN().length == 0) ? null : activityReportQueryModel.getUserIdIN());
			activitySignInQueryModel.setPageNumber(activityReportQueryModel.getPageNumber());
			activitySignInQueryModel.setPageSize(activityReportQueryModel.getPageSize());
			activitySignInQueryModel.setOrderBy(activityReportQueryModel.getOrderBy());
			activitySignInQueryModel.setDirection(activityReportQueryModel.getDirection());
			activitySignInQueryModel.setOrderBy(activityReportQueryModel.getOrderBy());
			activitySignInQueryModel.setDirection(activityReportQueryModel.getDirection());
			dataMap.put("signNum", activitySignInMapper.count(activitySignInQueryModel));
			int activityApplyStatusOld = activityReportQueryModel.getActivityApplyStatus();//讲原始查询状态暂存下来
			if (activityApplyStatusOld == 2) {//已签到的去签到查询
				List<ActivitySignIn> activitySignInsList = activitySignInMapper.findAll(activitySignInQueryModel);
				for (ActivitySignIn activitySignIn : activitySignInsList) {
					ActivityApply activityApply = new ActivityApply();
					activityApply.setId(activitySignIn.getId());
					activityApply.setActivityId(activitySignIn.getActivityId());
					activityApply.setUserId(activitySignIn.getUserId());
					data.add(activityApply);
				}

			} else {
				data = activityApplyMapper.findAllByReport(activityReportQueryModel);//查询数据
			}
			activityReportQueryModel.setActivityApplyStatus(0);
			dataMap.put("notPayNum", activityApplyMapper.queryCountReport(activityReportQueryModel));//统计未支付 数据
			activityReportQueryModel.setActivityApplyStatus(1);
			dataMap.put("payNum", activityApplyMapper.queryCountReport(activityReportQueryModel));//统计已支付的
			if (activityApplyStatusOld==0){
				total =(Long) dataMap.get("notPayNum");
			}else if(activityApplyStatusOld==1){
				total =(Long) dataMap.get("payNum");
			}else if(activityApplyStatusOld==2){
				total =(Long) dataMap.get("signNum");
			}
		}
		Page<ActivityApply> page = new Page<>();
		page.setPageNumber(activityReportQueryModel.getPageNumber());
		page.setPageSize(activityReportQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		dataMap.put("page",page);
		return dataMap;
	}

	@Override
	public Long queryAppliedCount(Long activityId) {
		return activityApplyMapper.queryAppliedCount(activityId);
	}

}
