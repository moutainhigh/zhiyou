package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.act.Activity;
import com.zy.entity.act.ActivityApply;
import com.zy.entity.usr.User;
import com.zy.service.ActivityService;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.ActivityApplyAdminVo;
import com.zy.vo.ActivityApplyListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActivityApplyComponent {

	@Autowired
	private CacheComponent cacheComponent;

	@Autowired
	private ActivityService activityService;

	public ActivityApplyListVo buildListVo(ActivityApply activityApply) {
		ActivityApplyListVo activityApplyListVo = new ActivityApplyListVo();
		BeanUtils.copyProperties(activityApply, activityApplyListVo);

		Long userId = activityApply.getUserId();
		if (userId != null) {
			User user = cacheComponent.getUser(userId);
			activityApplyListVo.setUser(VoHelper.buildUserAdminSimpleVo(user));
		}
		activityApplyListVo.setAmountLabel(GcUtils.formatCurreny(activityApply.getAmount()));
		Activity activity = activityService.findOne(activityApply.getActivityId());
		if (activity != null) {
			activityApplyListVo.setActivityTitle(activity.getTitle());
			activityApplyListVo.setActivityImageThumbnail(GcUtils.getThumbnail(activity.getImage(), 150, 90));
		}
		activityApplyListVo.setAppliedTimeLabel(GcUtils.formatDate(activityApply.getAppliedTime(), "yyyy-MM-dd HH:mm:ss"));

		return activityApplyListVo;
	}

	public ActivityApplyAdminVo buildAdminVo(ActivityApply activityApply) {
		ActivityApplyAdminVo activityApplyAdminVo = new ActivityApplyAdminVo();
		BeanUtils.copyProperties(activityApply, activityApplyAdminVo);

		Long userId = activityApply.getUserId();
		if (userId != null) {
			User user = cacheComponent.getUser(userId);
			activityApplyAdminVo.setUser(VoHelper.buildUserAdminSimpleVo(user));
		}

		Long payerUserId = activityApply.getPayerUserId();
		if (payerUserId != null) {
			User payerUser = cacheComponent.getUser(payerUserId);
			activityApplyAdminVo.setPayerUser(VoHelper.buildUserAdminSimpleVo(payerUser));
		}

		activityApplyAdminVo.setAmountLabel(GcUtils.formatCurreny(activityApply.getAmount()));
		Activity activity = activityService.findOne(activityApply.getActivityId());
		if (activity != null) {
			activityApplyAdminVo.setActivityTitle(activity.getTitle());
			activityApplyAdminVo.setActivityImageThumbnail(GcUtils.getThumbnail(activity.getImage(), 150, 90));
		}
		activityApplyAdminVo.setAppliedTimeLabel(GcUtils.formatDate(activityApply.getAppliedTime(), "yyyy-MM-dd HH:mm:ss"));

		return activityApplyAdminVo;
	}
}
