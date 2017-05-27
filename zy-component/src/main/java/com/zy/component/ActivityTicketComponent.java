package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.act.Activity;
import com.zy.entity.act.ActivityTeamApply;
import com.zy.entity.act.ActivityTicket;
import com.zy.entity.usr.User;
import com.zy.service.ActivityService;
import com.zy.service.UserService;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.ActivityTeamApplyAdminVo;
import com.zy.vo.ActivityTeamApplyListVo;
import com.zy.vo.ActivityTicketListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActivityTicketComponent {

	@Autowired
	private CacheComponent cacheComponent;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private UserService userService;

	@Autowired
	private ActivityComponent activityComponent;


	public ActivityTicketListVo buildListVo(ActivityTicket activityTicket) {
		ActivityTicketListVo activityTicketListVo = new ActivityTicketListVo();
		if( activityTicket.getUserId() != null && activityTicket.getIsUsed() == 1){
			User user = userService.findOne(activityTicket.getUserId());
			activityTicketListVo.setUsedUserName(user.getNickname());
		}else if( null == activityTicket.getUserId() && activityTicket.getIsUsed() == 0){

		}
		return activityTicketListVo;
	}

//	public ActivityTeamApplyAdminVo buildAdminVo(ActivityTeamApply activityTeamApply) {
//		ActivityTeamApplyAdminVo activityTeamApplyAdmin = new ActivityTeamApplyAdminVo();
//		BeanUtils.copyProperties(activityTeamApply,activityTeamApplyAdmin);
//		Long userId = activityTeamApply.getBuyerId();
//		if (userId != null) {
//			User user = cacheComponent.getUser(userId);
//			activityTeamApplyAdmin.setBuyer(VoHelper.buildUserAdminSimpleVo(user));
//		}
//		activityTeamApplyAdmin.setAmountLabel(GcUtils.formatCurreny(activityTeamApply.getAmount()));
//		Activity activity = activityService.findOne(activityTeamApply.getActivityId());
//		if (activity != null) {
//			activityTeamApplyAdmin.setActivity(activityComponent.buildListVo(activity));
//		}
//		activityTeamApplyAdmin.setCreateTimeLabel(GcUtils.formatDate(activityTeamApply.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
//		activityTeamApplyAdmin.setPaidTimeLabel(GcUtils.formatDate(activityTeamApply.getPaidTime(),"yyyy-MM-dd HH:mm:ss"));
//		return activityTeamApplyAdmin;
//	}
}
