package com.zy.mobile.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zy.common.model.query.Page;
import com.zy.component.ActivityComponent;
import com.zy.component.UserComponent;
import com.zy.entity.act.Activity;
import com.zy.entity.usr.User;
import com.zy.model.Principal;
import com.zy.model.query.ActivityApplyQueryModel;
import com.zy.model.query.ActivityCollectQueryModel;
import com.zy.model.query.ActivityQueryModel;
import com.zy.model.query.ActivitySignInQueryModel;
import com.zy.service.ActivityApplyService;
import com.zy.service.ActivityCollectService;
import com.zy.service.ActivityService;
import com.zy.service.ActivitySignInService;
import com.zy.service.UserService;
import com.zy.util.GcUtils;
import com.zy.vo.ActivityListVo;


@RequestMapping("/activity")
@Controller
public class ActivityController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserComponent userComponent;
	
	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private ActivityCollectService activityCollectService;
	
	@Autowired
	private ActivityApplyService activityApplyService;
	
	@Autowired
	private ActivitySignInService activitySignInService;
	
	@Autowired
	private ActivityComponent activityComponent;
	
	@RequestMapping
	public String list(Model model) {
		Page<Activity> page = activityService.findPage(ActivityQueryModel.builder().isReleasedEQ(true).build());
		List<ActivityListVo> list = page.getData().stream().map(v -> {
			return activityComponent.buildListVo(v);
			}).collect(Collectors.toList());
		model.addAttribute("activities", list);
		return "activity/activityList";
	}


	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String detail(@PathVariable Long id, Long inviterId, Model model) {
		
		//浏览+1
		activityService.view(id);
		
		if(inviterId != null) {
			User inviter = userService.findOne(inviterId);
			if(inviter != null) {
				model.addAttribute("inviter", userComponent.buildListVo(inviter));
			}
		}
		
		Activity activity = activityService.findOne(id);
		model.addAttribute("activity", activityComponent.buildDetailVo(activity));
		
		Principal principal = GcUtils.getPrincipal();
		Long userId = null;
		if(principal != null) {
			userId = principal.getUserId();
			ActivityCollectQueryModel activityCollectQueryModel = new ActivityCollectQueryModel();
			activityCollectQueryModel.setActivityIdEQ(id);
			activityCollectQueryModel.setUserIdEQ(principal.getUserId());
			model.addAttribute("isCollected", !activityCollectService.findPage(activityCollectQueryModel).getData().isEmpty());
			
			ActivityApplyQueryModel activityApplyQueryModel = new ActivityApplyQueryModel();
			activityApplyQueryModel.setActivityIdEQ(id);
			activityApplyQueryModel.setUserIdEQ(principal.getUserId());
			model.addAttribute("isApplied", !activityApplyService.findPage(activityApplyQueryModel).getData().isEmpty());
			
			ActivitySignInQueryModel activitySignInQueryModel = new ActivitySignInQueryModel();
			activitySignInQueryModel.setActivityIdEQ(id);
			activitySignInQueryModel.setUserIdEQ(principal.getUserId());
			model.addAttribute("isSigned", !activitySignInService.findPage(activitySignInQueryModel).getData().isEmpty());
		}
		return "activity/activityDetail" + userId == null ? "" : "?__u=" + userId;
	}

}
