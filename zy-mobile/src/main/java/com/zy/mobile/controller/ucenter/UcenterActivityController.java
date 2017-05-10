package com.zy.mobile.controller.ucenter;

import com.zy.common.exception.BizException;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.component.ActivityApplyComponent;
import com.zy.component.ActivityComponent;
import com.zy.component.CacheComponent;
import com.zy.component.UserComponent;
import com.zy.entity.act.Activity;
import com.zy.entity.act.ActivityApply;
import com.zy.entity.act.ActivityCollect;
import com.zy.model.BizCode;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.model.query.ActivityApplyQueryModel;
import com.zy.model.query.ActivityCollectQueryModel;
import com.zy.service.ActivityApplyService;
import com.zy.service.ActivityCollectService;
import com.zy.service.ActivityService;
import com.zy.service.UserService;
import com.zy.vo.ActivityListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

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

	@Autowired
	private ActivityApplyComponent activityApplyComponent;

	@Autowired
	private CacheComponent cacheComponent;

	@RequestMapping(value = "/apply")
	public String apply(Long id, Principal principal, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		try {
			activityService.apply(id, principal.getUserId(), null);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("报名成功,请点击付费完成报名"));
			return "redirect:/u/activity/activityApply/" + id;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("报名异常," + e.getMessage()));
			return "redirect:/activity/" + id;
		}
	}

	@RequestMapping(value = "/payer")
	public String payer(Principal principal, Model model) {
		List<ActivityApply> all = activityApplyService.findAll(ActivityApplyQueryModel.builder().payerUserIdEQ(principal.getUserId()).build());
		if (!all.isEmpty()) {
			model.addAttribute("activityApplyVos", all.stream().map(activityApplyComponent::buildListVo).collect(Collectors.toList()));
		}
		return "ucenter/activity/payerList";
	}

	@RequestMapping(path = "/activityApply/{activityId}", method = RequestMethod.GET)
	public String orderPay(@PathVariable Long activityId, Model model, Principal principal) {

		Activity activity = activityService.findOne(activityId);
		validate(activity, NOT_NULL, "activity id " + activityId + " not found");

		ActivityApply activityApply = activityApplyService.findByActivityIdAndUserId(activityId, principal.getUserId());
		validate(activityApply, NOT_NULL, "activity apply id" + activityId + " not found");
		if(!activityApply.getUserId().equals(principal.getUserId())){
			throw new BizException(BizCode.ERROR, "非自己的订单, 不能操作");
		}
		if(activityApply.getActivityApplyStatus() != ActivityApply.ActivityApplyStatus.已报名){
			return "redirect:/activity/" + activityId;
		}

		model.addAttribute("title", activity.getTitle());
		model.addAttribute("activityId", activityId);
		model.addAttribute("amount", activityApply.getAmount());
		return "ucenter/pay/activityPay";
	}

	@RequestMapping("/applyList")
	public String applyList(Principal principal, Model model) {
		
		List<ActivityApply> list = activityApplyService.findAll(ActivityApplyQueryModel.builder().userIdEQ(principal.getUserId()).build());
		List<ActivityListVo> vos = list.stream().map(v -> {
			Activity activity = cacheComponent.getActivity(v.getActivityId());
			return activityComponent.buildListVo(activity);
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
			Activity activity = cacheComponent.getActivity(v.getActivityId());
			return activityComponent.buildListVo(activity);
		}).collect(Collectors.toList());
		
		model.addAttribute("historyActivities", vos.stream().filter(v -> "活动已结束".equals(v.getStatus())).collect(Collectors.toList()));
		model.addAttribute("activities", vos.stream().filter(v -> !"活动已结束".equals(v.getStatus())).collect(Collectors.toList()));
		
		return "activity/collectActivityList";
	}

	@RequestMapping(value = "/signIn")
	public String signIn(Long id, Principal principal, Model model, RedirectAttributes redirectAttributes) {
		ActivityApply activityApply = activityApplyService.findByActivityIdAndUserId(id, principal.getUserId());
		model.addAttribute("user", userComponent.buildSimpleVo(userService.findOne(principal.getUserId())));
		if (activityApply == null) {
			model.addAttribute("id", id);
			return "activity/signInFail";
		}
		if (activityApply.getActivityApplyStatus() == ActivityApply.ActivityApplyStatus.已报名) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("您还没有付费, 不能签到"));
			return "redirect:/activity/";
		}
		activityService.signIn(id, principal.getUserId());
		return "activity/signInSuccess";
	}
}
