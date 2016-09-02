package com.zy.mobile.controller.ucenter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.model.query.Page;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.component.ActivityComponent;
import com.zy.component.UserComponent;
import com.zy.entity.act.Activity;
import com.zy.entity.act.ActivityApply;
import com.zy.entity.act.ActivityCollect;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.model.query.ActivityApplyQueryModel;
import com.zy.model.query.ActivityCollectQueryModel;
import com.zy.model.query.ActivityQueryModel;
import com.zy.service.ActivityApplyService;
import com.zy.service.ActivityCollectService;
import com.zy.service.ActivityService;
import com.zy.service.UserService;
import com.zy.vo.ActivityListVo;

@RequestMapping("/u/activity")
@Controller
public class UcenterActivityController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private ActivityCollectService activityCollectService;
	
	@Autowired
	private ActivityApplyService activityApplyService;

	@Autowired
	private UserComponent userComponent;
	
	@Autowired
	private ActivityComponent activityComponent;

	@RequestMapping(value = "/apply", method = RequestMethod.POST)
	public String apply(Long id, String inviterPhone, Principal principal, Model model, RedirectAttributes redirectAttributes) {
		try {
			activityService.apply(id, principal.getUserId(), null);
			model.addAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("报名成功!"));
			model.addAttribute("activity", activityComponent.buildListVo(activityService.findOne(id)));
			model.addAttribute("user", userComponent.buildSimpleVo(userService.findOne(principal.getUserId())));
			return "activity/activityApplySuccess";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("报名异常," + e.getMessage()));
			return "redirect:/activity/" + id;
		}
	}

	@RequestMapping("/applyList")
	public String applyList(Principal principal, Model model) {
		
		List<ActivityApply> list = activityApplyService.findAll(ActivityApplyQueryModel.builder().userIdEQ(principal.getUserId()).build());
		List<ActivityListVo> vos = list.stream().map(v -> {
			return activityComponent.buildApply(v);
		}).collect(Collectors.toList());
		
		model.addAttribute("historyActivities", vos.stream().filter(v -> "活动已结束".equals(v.getStatus())).collect(Collectors.toList()));
		model.addAttribute("activities", vos.stream().filter(v -> !"活动已结束".equals(v.getStatus())).collect(Collectors.toList()));
		
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
		
		List<ActivityCollect> list = activityCollectService.findAll(ActivityCollectQueryModel.builder().userIdEQ(principal.getUserId()).build());
		List<ActivityListVo> vos = list.stream().map(v -> {
			return activityComponent.buildCollect(v);
		}).collect(Collectors.toList());
		
		model.addAttribute("historyActivities", vos.stream().filter(v -> "活动已结束".equals(v.getStatus())).collect(Collectors.toList()));
		model.addAttribute("activities", vos.stream().filter(v -> !"活动已结束".equals(v.getStatus())).collect(Collectors.toList()));
		
		return "activity/collectActivityList";
	}

	@RequestMapping(value = "/signIn")
	@ResponseBody
	public Result<?> signIn(Long id, Principal principal) {
		
		ActivityApplyQueryModel activityApplyQueryModel = new ActivityApplyQueryModel();
		activityApplyQueryModel.setUserIdEQ(principal.getUserId());
		Page<ActivityApply> page = activityApplyService.findPage(activityApplyQueryModel);
		if(!page.getData().isEmpty()) {
			return ResultBuilder.ok("请先报名参加活动,再签到");
		}
		
		activityService.signIn(id, principal.getUserId());
		return ResultBuilder.ok("ok");
	}
}
