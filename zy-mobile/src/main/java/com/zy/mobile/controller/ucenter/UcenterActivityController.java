package com.zy.mobile.controller.ucenter;

import com.zy.common.model.query.Page;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.component.ActivityComponent;
import com.zy.entity.act.Activity;
import com.zy.entity.act.ActivityApply;
import com.zy.entity.act.ActivityCollect;
import com.zy.entity.act.ActivitySignIn;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.model.query.ActivityApplyQueryModel;
import com.zy.model.query.ActivityCollectQueryModel;
import com.zy.model.query.ActivityQueryModel;
import com.zy.model.query.ActivitySignInQueryModel;
import com.zy.service.ActivityApplyService;
import com.zy.service.ActivityCollectService;
import com.zy.service.ActivityService;
import com.zy.service.ActivitySignInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/u/activity")
@Controller
public class UcenterActivityController {
	
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

	@RequestMapping(value = "/apply", method = RequestMethod.POST)
	public String apply(Long id, String phone, Principal principal, Model model, RedirectAttributes redirectAttributes) {
		
		try {
			activityService.apply(id, principal.getUserId(), null);
			model.addAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("申请成功!"));
			return "activity/activityApplySuccess";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("申请异常," + e.getMessage()));
			return "redirect:/activity/" + id;
		}
		
	}

	@RequestMapping("/applyList")
	public String applyList(Principal principal, Model model) {
		ActivityApplyQueryModel activityApplyQueryModel = new ActivityApplyQueryModel();
		activityApplyQueryModel.setUserIdEQ(principal.getUserId());
		Page<ActivityApply> page = activityApplyService.findPage(activityApplyQueryModel);

		if(!page.getData().isEmpty()) {
			ActivityQueryModel activityQueryModel = new ActivityQueryModel();
			activityQueryModel.setIdIN(page.getData().stream().map(v -> v.getActivityId()).toArray(Long[]::new));
			Page<Activity> activityPage = activityService.findPage(activityQueryModel);
			List<Activity> activities = activityPage.getData();
			model.addAttribute("activities", activities.stream().map(v -> {
				return activityComponent.buildListVo(v);
			}).collect(Collectors.toList()));
		}
		return "activity/applyActivityList";
	}
	
	@RequestMapping(value = "/collect")
	@ResponseBody
	public Result<? > collect(Long id, Principal principal, Model model) {
		activityService.collect(id, principal.getUserId());
		return ResultBuilder.ok("ok");
	}
	
	@RequestMapping("/uncollect")
	@ResponseBody
	public Result<?> uncollect(Long id, Principal principal, Model model) {
		activityService.uncollect(id, principal.getUserId());
		return ResultBuilder.ok("ok");
	}
	
	@RequestMapping("/collectList")
	public String collectList(Principal principal, Model model) {
		ActivityCollectQueryModel activityCollectQueryModel = new ActivityCollectQueryModel();
		activityCollectQueryModel.setUserIdEQ(principal.getUserId());
		Page<ActivityCollect> page = activityCollectService.findPage(activityCollectQueryModel);

		if(!page.getData().isEmpty()) {
			ActivityQueryModel activityQueryModel = new ActivityQueryModel();
			activityQueryModel.setIdIN(page.getData().stream().map(v -> v.getActivityId()).toArray(Long[]::new));
			Page<Activity> activityPage = activityService.findPage(activityQueryModel);
			List<Activity> activities = activityPage.getData();
			model.addAttribute("activities", activities.stream().map(v -> {
				return activityComponent.buildListVo(v);
			}).collect(Collectors.toList()));
		}
		return "activity/collectActivityList";
	}

	@RequestMapping(value = "/signIn")
	@ResponseBody
	public Result<?> signIn(Long id, Principal principal) {
		
		ActivitySignInQueryModel activitySignInQueryModel = new ActivitySignInQueryModel();
		activitySignInQueryModel.setUserIdEQ(principal.getUserId());
		Page<ActivitySignIn> page = activitySignInService.findPage(activitySignInQueryModel);
		if(!page.getData().isEmpty()) {
			return ResultBuilder.ok("已签到");
		}
		activityService.signIn(id, principal.getUserId());
		return ResultBuilder.ok("ok");
	}
}
