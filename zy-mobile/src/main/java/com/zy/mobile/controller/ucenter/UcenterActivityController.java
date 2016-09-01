package com.zy.mobile.controller.ucenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.component.ActivityComponent;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.service.ActivityApplyService;
import com.zy.service.ActivityCollectService;
import com.zy.service.ActivityService;
import com.zy.service.ActivitySignInService;

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

	@RequestMapping(value = "/apply/{id}", method = RequestMethod.POST)
	public String apply(@PathVariable Long id, String phone, Principal principal, Model model, RedirectAttributes redirectAttributes) {
		
		try {
			activityService.apply(id, principal.getUserId());
			model.addAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("申请成功!"));
			return "activity/activityApplySuccess";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("申请异常," + e.getMessage()));
			return "redirect:/activity/" + id;
		}
		
	}

	@RequestMapping("/applyList")
	public String applyList(Principal principal, Model model) {
		//TODO
		return "activity/applyActivityList";
	}
	
	@RequestMapping(value = "/collect")
	@ResponseBody
	public Result<Boolean> collect(Long id, Principal principal) {
		//TODO
		return ResultBuilder.result(true);
	}
	
	@RequestMapping("/uncollect")
	@ResponseBody
	public Result<Boolean> unCollect(Long id, Principal principal, Model model) {
		//TODO
		return ResultBuilder.result(true);
	}
	
	@RequestMapping("/collectList")
	public String collectList(Principal principal, Model model) {
		//TODO
		return "activity/applyActivityList";
	}

	@RequestMapping(value = "/signIn")
	@ResponseBody
	public Result<Boolean> signIn(Long id, Principal principal) {
		//TODO
		return ResultBuilder.result(true);
	}
}
