package com.zy.mobile.controller.ucenter;

import com.zy.common.exception.BizException;
import com.zy.common.exception.UnauthenticatedException;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.component.ActivityApplyComponent;
import com.zy.component.ActivityComponent;
import com.zy.component.CacheComponent;
import com.zy.component.UserComponent;
import com.zy.entity.act.Activity;
import com.zy.entity.act.ActivityApply;
import com.zy.entity.act.ActivityCollect;
import com.zy.entity.act.ActivitySignIn;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.fnc.PayType;
import com.zy.entity.fnc.Payment;
import com.zy.entity.usr.User;
import com.zy.model.BizCode;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.model.query.ActivityApplyQueryModel;
import com.zy.model.query.ActivityCollectQueryModel;
import com.zy.service.*;
import com.zy.vo.ActivityListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
	private ActivitySignInService activitySignInService;

	@Autowired
	private UserComponent userComponent;
	
	@Autowired
	private ActivityComponent activityComponent;

	@Autowired
	private ActivityApplyComponent activityApplyComponent;

	@Autowired
	private CacheComponent cacheComponent;

	/**
	 * 报名后跳转到选择支付方式页面
	 * @param id
	 * @param principal
	 * @param model
	 * @param redirectAttributes
	 * @param request
     * @return
     */
	@RequestMapping(value = "/apply")
	public String apply(Long id, Principal principal, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		try {
			activityService.apply(id, principal.getUserId(), null);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("报名成功,请点击付费完成报名"));
			return "redirect:/u/activity/" + id + "/activityApply";
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

	@RequestMapping(path = "/{activityId}/activityApply", method = RequestMethod.GET)
	public String activityApply1(@PathVariable Long activityId, Model model, Principal principal) {

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
		model.addAttribute("activityApplyId", activityApply.getId());
		model.addAttribute("amount", activityApply.getAmount());
		return "ucenter/activity/activityApply1";
	}

	/**
	 * 跳转到支付类型页面
	 * @param activityApplyId
	 * @param payerPhone
	 * @param model
	 * @param redirectAttributes
	 * @param principal
     * @return
     */
	@RequestMapping(path = "/activityApply", method = RequestMethod.POST)
	public String activityApply1(@RequestParam Long activityApplyId, String payerPhone, Model model, RedirectAttributes redirectAttributes,
	                             Principal principal) {

		ActivityApply activityApply = activityApplyService.findOne(activityApplyId);
		validate(activityApply, NOT_NULL, "activity apply " + activityApplyId + " id not found");

		Activity activity = activityService.findOne(activityApply.getActivityId());
		Long activityId = activity.getId();
		if (activityApply.getActivityApplyStatus() == ActivityApply.ActivityApplyStatus.已支付) {
			return "redirect:/activity/" + activityId;
		}
		if (activityApply.getPayerUserId() != null) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("请等待他人付款"));
			return "redirect:/activity/" + activityId;
		}

		//选择他人代付
		Long userId = activityApply.getUserId();
		if (StringUtils.isNotBlank(payerPhone)) {
			User payer = userService.findByPhone(payerPhone);
			if (payer == null) {
				redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("手机号不存在"));
				return "redirect:/u/activity/" + activityId + "/activityApply";
			}
			if (payer.getUserType() != User.UserType.代理) {
				redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("待付人必须是代理"));
				return "redirect:/u/activity/" + activityId + "/activityApply";
			}
			Long payerId = payer.getId();
			if (userId.equals(payerId)) {
				redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("待付人不能是自己"));
				return "redirect:/u/activity/" + activityId + "/activityApply";
			}

			activityApply.setPayerUserId(payerId);
			activityApplyService.modifyPayerUserId(activityApply.getId(), payerId);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("请等待他人付款"));
			return "redirect:/activity/" + activityId;
		}

		// 自己支付
		if(!userId.equals(principal.getUserId())){
			throw new BizException(BizCode.ERROR, "非自己的订单不能操作");
		}
		model.addAttribute("title", activity.getTitle());
		model.addAttribute("activityApplyId", activityApply.getId());
		model.addAttribute("amount", activityApply.getAmount());
		return "ucenter/pay/activityPay";
	}

	//他人代付 去支付
	@RequestMapping(path = "/activityApply/{activityApplyId}/payer", method = RequestMethod.GET)
	public String payerPay(@PathVariable Long activityApplyId, Principal principal, Model model, RedirectAttributes redirectAttributes) {

		ActivityApply activityApply = activityApplyService.findOne(activityApplyId);
		validate(activityApply, NOT_NULL, "activity apply " + activityApplyId + " id not found");

		Activity activity = activityService.findOne(activityApply.getActivityId());
		Long activityId = activity.getId();
		if (activityApply.getActivityApplyStatus() == ActivityApply.ActivityApplyStatus.已支付) {
			return "redirect:/activity/" + activityId;
		}
		if (activityApply.getPayerUserId() == null) {
			throw new UnauthenticatedException("权限不足");
		}
		if (!principal.getUserId().equals(activityApply.getPayerUserId())) {
			throw new UnauthenticatedException("权限不足");
		}

		model.addAttribute("title", activity.getTitle());
		model.addAttribute("activityApplyId", activityApply.getId());
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
		ActivitySignIn activitySignIn = activitySignInService.findByActivityIdAndUserId(id, principal.getUserId());
		if (activitySignIn != null) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("您已签到，请勿重复操作"));
			return "redirect:/activity/";
		} else {
			activityService.signIn(id, principal.getUserId());
			return "activity/signInSuccess";
		}
	}

}
