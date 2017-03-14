package com.zy.service.impl;

import com.zy.common.exception.BizException;
import com.zy.common.exception.ConcurrentException;
import com.zy.common.model.query.Page;
import com.zy.entity.act.Activity;
import com.zy.entity.act.ActivityApply;
import com.zy.entity.act.ActivityCollect;
import com.zy.entity.act.ActivitySignIn;
import com.zy.entity.sys.Area;
import com.zy.entity.usr.User;
import com.zy.mapper.*;
import com.zy.model.BizCode;
import com.zy.model.query.ActivityQueryModel;
import com.zy.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@Service
@Validated
public class ActivityServiceImpl implements ActivityService {

	@Autowired
	private ActivityMapper activityMapper;

	@Autowired
	private ActivitySignInMapper activitySignInMapper;

	@Autowired
	private ActivityApplyMapper activityApplyMapper;

	@Autowired
	private ActivityCollectMapper activityCollectMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private AreaMapper areaMapper;

	@Override
	public Activity create(@NotNull Activity activity) {
		activity.setVersion(0);
		activity.setIsReleased(false);
		activity.setAppliedCount(0L);
		activity.setViewedCount(0L);
		activity.setCollectedCount(0L);
		activity.setSignedInCount(0L);
		validate(activity);
		checkArea(activity.getAreaId());
		activityMapper.insert(activity);
		return activity;
	}

	@Override
	public void view(@NotNull Long activityId) {
		checkAndFindActivity(activityId);
		activityMapper.view(activityId);
	}

	@Override
	public void modify(@NotNull Activity activity) {
		String[] fields = new String[] { "areaId", "address", "latitude", "longitude", "image", "detail", "applyDeadline", "startTime", "endTime", "title", "amount" };
		validate(activity, fields);
		checkAndFindActivity(activity.getId());
		checkArea(activity.getAreaId());
		activityMapper.merge(activity, fields);
	}

	@Override
	public void release(@NotNull Long activityId, boolean isReleased) {
		checkAndFindActivity(activityId);
		Activity activityForMerage = new Activity();
		activityForMerage.setId(activityId);
		activityForMerage.setIsReleased(isReleased);
		activityMapper.merge(activityForMerage, "isReleased");
	}

	@Override
	public void apply(@NotNull Long activityId, @NotNull Long userId, Long inviterId) {
		checkUser(userId);
		Activity activity = checkAndFindActivity(activityId);

		Long appliedCount = activity.getAppliedCount();

		ActivityApply activityApply = activityApplyMapper.findByActivityIdAndUserId(activityId, userId);
		if (activityApply != null) {
			if (activityApply.getIsCancelled()) {
				activity.setAppliedCount(appliedCount + 1);
				activityApply.setIsCancelled(false);
				if (activityMapper.update(activity) == 0) {
					throw new ConcurrentException();
				}
				activityApplyMapper.update(activityApply);
			} else {
				return; // 幂等操作
			}
		} else {

			if (inviterId != null) {
				if (inviterId.equals(userId)) {
					inviterId = null;
				} else {
					checkUser(inviterId);
				}
			}

			if (!activity.getIsReleased()) {
				throw new BizException(BizCode.ERROR, "活动暂未开放不能报名");
			}

			if (activity.getApplyDeadline().before(new Date())) {
				throw new BizException(BizCode.ERROR, "活动已经停止报名");
			}

			activity.setAppliedCount(appliedCount + 1);
			if (activityMapper.update(activity) == 0) {
				throw new ConcurrentException();
			}
			activityApply = new ActivityApply();
			activityApply.setActivityId(activityId);
			activityApply.setUserId(userId);
			activityApply.setAppliedTime(new Date());
			activityApply.setInviterId(inviterId);
			activityApply.setIsCancelled(false);
			activityApply.setIsSmsSent(false);
			activityApply.setActivityApplyStatus(ActivityApply.ActivityApplyStatus.已报名);
			activityApply.setAmount(activity.getAmount());
			validate(activityApply);
			activityApplyMapper.insert(activityApply);
		}
	}

	@Override
	public void cancel(@NotNull Long activityId, @NotNull Long userId) {
		checkUser(userId);
		Activity activity = checkAndFindActivity(activityId);
		Long appliedCount = activity.getAppliedCount();

		ActivityApply activityApply = activityApplyMapper.findByActivityIdAndUserId(activityId, userId);
		if (activityApply != null) {
			if (activityApply.getIsCancelled()) {
				return;
			} else {
				activity.setAppliedCount(appliedCount - 1);
				if (activityMapper.update(activity) == 0) {
					throw new ConcurrentException();
				}
				activityApply.setIsCancelled(true);
				activityApplyMapper.update(activityApply);
			}
		}
	}

	@Override
	public void collect(@NotNull Long activityId, @NotNull Long userId) {
		checkUser(userId);
		Activity activity = checkAndFindActivity(activityId);
		if (!activity.getIsReleased()) {
			throw new BizException(BizCode.ERROR, "活动暂未开放不能关注");
		}
		Long collectedCount = activity.getCollectedCount();
		ActivityCollect activityCollect = activityCollectMapper.findByActivityIdAndUserId(activityId, userId);
		if (activityCollect != null) {
			return;
		} else {
			activity.setCollectedCount(collectedCount + 1);
			if (activityMapper.update(activity) == 0) {
				throw new ConcurrentException();
			}
			activityCollect = new ActivityCollect();
			activityCollect.setActivityId(activityId);
			activityCollect.setUserId(userId);
			activityCollect.setCollectedTime(new Date());
			validate(activityCollect);
			activityCollectMapper.insert(activityCollect);
		}
	}

	@Override
	public void uncollect(@NotNull Long activityId, @NotNull Long userId) {
		checkUser(userId);
		Activity activity = checkAndFindActivity(activityId);
		Long collectedCount = activity.getCollectedCount();
		ActivityCollect activityCollect = activityCollectMapper.findByActivityIdAndUserId(activityId, userId);
		if (activityCollect != null) {
			activity.setCollectedCount(collectedCount - 1);
			if (activityMapper.update(activity) == 0) {
				throw new ConcurrentException();
			}
			activityCollectMapper.delete(activityCollect.getId());
		}
	}

	@Override
	public void signIn(@NotNull Long activityId, @NotNull Long userId) {
		checkUser(userId);
		Activity activity = checkAndFindActivity(activityId);

		ActivityApply activityApply = activityApplyMapper.findByActivityIdAndUserId(activityId, userId);
		if (activityApply == null) {
			throw new BizException(BizCode.ERROR, "您还没有报名该活动, 不能签到");
		}
		if (activityApply.getActivityApplyStatus() == ActivityApply.ActivityApplyStatus.已报名) {
			throw new BizException(BizCode.ERROR, "您还没付费, 不能签到");
		}
		Long signedInCount = activity.getSignedInCount();

		ActivitySignIn activitySignIn = activitySignInMapper.findByActivityIdAndUserId(activityId, userId);
		if (activitySignIn != null) {
			return;
		} else {
			activity.setSignedInCount(signedInCount + 1);
			if (activityMapper.update(activity) == 0) {
				throw new ConcurrentException();
			}
			activitySignIn = new ActivitySignIn();
			activitySignIn.setActivityId(activityId);
			activitySignIn.setUserId(userId);
			activitySignIn.setSignedInTime(new Date());
			validate(activitySignIn);
			activitySignInMapper.insert(activitySignIn);
		}
	}

	@Override
	public Activity findOne(@NotNull Long activityId) {
		return activityMapper.findOne(activityId);
	}

	@Override
	public Page<Activity> findPage(@NotNull ActivityQueryModel activityQueryModel) {
		if (activityQueryModel.getPageNumber() == null)
			activityQueryModel.setPageNumber(0);
		if (activityQueryModel.getPageSize() == null)
			activityQueryModel.setPageSize(20);
		long total = activityMapper.count(activityQueryModel);
		List<Activity> data = activityMapper.findAll(activityQueryModel);
		Page<Activity> page = new Page<>();
		page.setPageNumber(activityQueryModel.getPageNumber());
		page.setPageSize(activityQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	private void checkUser(Long userId) {
		validate(userId, NOT_NULL, "user id is null");
		User user = userMapper.findOne(userId);
		validate(user, NOT_NULL, "user id " + userId + " is not found");
	}

	private Activity checkAndFindActivity(Long activityId) {
		validate(activityId, NOT_NULL, "activity id is null");
		Activity activity = activityMapper.findOne(activityId);
		validate(activity, NOT_NULL, "activity id " + activityId + " is not found");
		return activity;
	}

	private void checkArea(Long areaId) {
		validate(areaId, NOT_NULL, "area id is null");
		Area area = areaMapper.findOne(areaId);
		validate(area, NOT_NULL, "area id " + areaId + " is not found");
		validate(area, v -> v.getAreaType() == Area.AreaType.区, "area id " + areaId + " must be district");
	}

}
