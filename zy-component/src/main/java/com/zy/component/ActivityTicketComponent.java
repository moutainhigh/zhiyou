package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.act.Activity;
import com.zy.entity.act.ActivityTeamApply;
import com.zy.entity.act.ActivityTicket;
import com.zy.entity.usr.User;
import com.zy.service.ActivityService;
import com.zy.service.ActivityTeamApplyService;
import com.zy.service.UserService;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.ActivityTeamApplyAdminVo;
import com.zy.vo.ActivityTeamApplyListVo;
import com.zy.vo.ActivityTicketAdminVo;
import com.zy.vo.ActivityTicketListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActivityTicketComponent {

	@Autowired
	private CacheComponent cacheComponent;

	@Autowired
	private ActivityTeamApplyService activityTeamApplyService;

	@Autowired
	private ActivityTeamApplyComponent activityTeamApplyComponent;


	public ActivityTicketListVo buildListVo(ActivityTicket activityTicket) {
		ActivityTicketListVo activityTicketListVo = new ActivityTicketListVo();
		BeanUtils.copyProperties(activityTicket, activityTicketListVo);
		Long userId = activityTicket.getUserId();
		Integer used = activityTicket.getIsUsed();
		if( userId != null && used == 1){
			activityTicketListVo.setUsedUser(VoHelper.buildUserListVo(cacheComponent.getUser(activityTicket.getUserId())));
		}else if( null == userId && used == 0){

		}
		return activityTicketListVo;
	}

	public ActivityTicketAdminVo buildAdminVo(ActivityTicket activityTicket) {
		ActivityTicketAdminVo activityTicketAdminVo = new ActivityTicketAdminVo();
		BeanUtils.copyProperties(activityTicket, activityTicketAdminVo);
		Long userId = activityTicket.getUserId();
		Integer used = activityTicket.getIsUsed();
		Long  teamApplyId = activityTicket.getTeamApplyId();
		if( userId != null && used == 1){
			activityTicketAdminVo.setUsedUser(VoHelper.buildUserListVo(cacheComponent.getUser(activityTicket.getUserId())));
		}else if( null == userId && used == 0){

		}
		if(teamApplyId != null){
			ActivityTeamApply activityTeamApply = activityTeamApplyService.findOne(teamApplyId);
			activityTicketAdminVo.setActivityTeamApply(activityTeamApplyComponent.buildAdminVo(activityTeamApply));
		}
		return activityTicketAdminVo;
	}
}
