package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.act.Activity;
import com.zy.entity.act.ActivityTeamApply;
import com.zy.entity.usr.User;
import com.zy.service.ActivityService;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.ActivityTeamApplyAdminVo;
import com.zy.vo.ActivityTeamApplyListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActivityTeamApplyComponent {

	@Autowired
	private CacheComponent cacheComponent;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private ActivityComponent activityComponent;

	public ActivityTeamApplyListVo buildListVo(ActivityTeamApply activityTeamApply) {
		ActivityTeamApplyListVo activityTeamApplyListVo = new ActivityTeamApplyListVo();
		BeanUtils.copyProperties(activityTeamApply,activityTeamApplyListVo);
		activityTeamApplyListVo.setAmountLabel(GcUtils.formatCurreny(activityTeamApply.getAmount()));
		Activity activity = activityService.findOne(activityTeamApply.getActivityId());
		if (activity != null) {
			activityTeamApplyListVo.setActivity(activityComponent.buildListVo(activity));
		}
		activityTeamApplyListVo.setCreateTimeLabel(GcUtils.formatDate(activityTeamApply.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
		activityTeamApplyListVo.setPaidTimeLabel(GcUtils.formatDate(activityTeamApply.getPaidTime(),"yyyy-MM-dd HH:mm:ss"));

		return activityTeamApplyListVo;
	}

	public ActivityTeamApplyAdminVo buildAdminVo(ActivityTeamApply activityTeamApply) {
		ActivityTeamApplyAdminVo activityTeamApplyAdmin = new ActivityTeamApplyAdminVo();
		BeanUtils.copyProperties(activityTeamApply,activityTeamApplyAdmin);
		Long userId = activityTeamApply.getBuyerId();
		if (userId != null) {
			User user = cacheComponent.getUser(userId);
			activityTeamApplyAdmin.setBuyer(VoHelper.buildUserAdminSimpleVo(user));
		}
		activityTeamApplyAdmin.setAmountLabel(GcUtils.formatCurreny(activityTeamApply.getAmount()));
		Activity activity = activityService.findOne(activityTeamApply.getActivityId());
		if (activity != null) {
			activityTeamApplyAdmin.setActivity(activityComponent.buildListVo(activity));
		}
		activityTeamApplyAdmin.setCreateTimeLabel(GcUtils.formatDate(activityTeamApply.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
		activityTeamApplyAdmin.setPaidTimeLabel(GcUtils.formatDate(activityTeamApply.getPaidTime(),"yyyy-MM-dd HH:mm:ss"));
		return activityTeamApplyAdmin;
	}
}
