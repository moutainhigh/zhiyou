package com.zy.mobile.controller.ucenter;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.component.PolicyComponent;
import com.zy.component.ReportComponent;
import com.zy.entity.act.Policy;
import com.zy.entity.act.Report;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.model.query.PolicyQueryModel;
import com.zy.model.query.ReportQueryModel;
import com.zy.service.PolicyService;
import com.zy.service.ReportService;
import com.zy.service.UserInfoService;

@RequestMapping("/u/policy")
@Controller
public class UcenterPolicyController {

	@Autowired
	private ReportService reportService;

	@Autowired
	private PolicyService policyService;
	
	@Autowired
	private UserInfoService userInfoService;
	
	@Autowired
	private ReportComponent reportComponent;
	
	@Autowired
	private PolicyComponent policyComponent;

	@RequestMapping()
	public String list(Principal principal, Model model) {
		Page<Policy> page = policyService.findPage(PolicyQueryModel.builder().userIdEQ(principal.getUserId()).pageNumber(0).pageSize(6).build());
		model.addAttribute("page", PageBuilder.copyAndConvert(page, policyComponent::buildListVo));
		model.addAttribute("timeLT", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		return "ucenter/policy/policyList";
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Result<?> list(Model model, Date timeLT, @RequestParam(required = true) Integer pageNumber, Principal principal) {
		if (timeLT == null) {
			timeLT = new Date();
		}

		Map<String, Object> map = new HashMap<>();
		Page<Policy> page = policyService.findPage(PolicyQueryModel.builder().userIdEQ(principal.getUserId()).pageNumber(pageNumber).pageSize(6).build());
		map.put("page", PageBuilder.copyAndConvert(page, policyComponent::buildListVo));
		map.put("timeLT", DateFormatUtils.format(timeLT, "yyyy-MM-dd HH:mm:ss"));

		return ResultBuilder.result(map);
	}

	@RequestMapping(value = "/create", method = GET)
	public String create(Principal principal, Model model, RedirectAttributes redirectAttributes) {
		List<Report> reports = reportService.findAll(ReportQueryModel.builder().userIdEQ(principal.getUserId()).build());
		model.addAttribute("reports", reports.stream().map(reportComponent::buildListVo).collect(Collectors.toList()));
		return "ucenter/policy/policyCreate";
	}

	@RequestMapping(value = "/create", method = POST)
	public String create(Policy policy, Principal principal, Model model, RedirectAttributes redirectAttributes) {
		policy.setUserId(principal.getUserId());
		try {
			//TODO policyService.create(policy);
		} catch (Exception e) {
			model.addAttribute("policy", policy);
			model.addAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
			return create(principal, model, redirectAttributes);
		}
		redirectAttributes.addFlashAttribute(ResultBuilder.ok("添加保单成功"));
		return "redirect:/u/policy";
	}

}
