package com.zy.mobile.controller.ucenter;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.component.ReportComponent;
import com.zy.entity.act.Report;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserRank;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.model.query.ReportQueryModel;
import com.zy.service.ReportService;
import com.zy.service.UserService;

@RequestMapping("/u/report")
@Controller
public class UcenterReportController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ReportService reportService;

	@Autowired
	private ReportComponent reportComponent;

	@RequestMapping()
	public String list(Principal principal, Model model) {
		Page<Report> page = reportService.findPage(ReportQueryModel.builder().userIdEQ(principal.getUserId()).pageNumber(0).pageSize(6).build());
		model.addAttribute("page", PageBuilder.copyAndConvert(page, reportComponent::buildVo));
		model.addAttribute("timeLT", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		return "ucenter/report/reportList";
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Result<?> product(Model model, Date timeLT, @RequestParam(required = true) Integer pageNumber, Principal principal) {
		if (timeLT == null) {
			timeLT = new Date();
		}
		
		Map<String, Object> map = new HashMap<>();
		Page<Report> page = reportService.findPage(ReportQueryModel.builder().userIdEQ(principal.getUserId()).pageNumber(pageNumber).pageSize(6).build());
		map.put("page", PageBuilder.copyAndConvert(page, reportComponent::buildVo));
		map.put("timeLT", DateFormatUtils.format(timeLT, "yyyy-MM-dd HH:mm:ss"));
			
		return ResultBuilder.result(map);
	}
	
	@RequestMapping(value = "create", method = GET)
	public String create(Model model, Principal principal) {
		User user = userService.findOne(principal.getUserId());
		model.addAttribute("userRank", user.getUserRank());
		return "ucenter/report/reportCreate";
	}

	@RequestMapping(value = "/create", method = POST)
	public String create(Report report, Principal principal, Model model, RedirectAttributes redirectAttributes) {
		User user = userService.findOne(principal.getUserId());
		if(user.getUserRank() == UserRank.V0) {
			model.addAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("只有成为代理后才能提交检测报告"));
			return "redirect:/u/report";
		}
		report.setUserId(principal.getUserId());
		try {
			reportService.create(report);
		} catch (Exception e) {
			model.addAttribute("report", report);
			model.addAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
			return create(model, principal);
		}
		redirectAttributes.addFlashAttribute(ResultBuilder.ok("上传检测报告成功"));
		return "redirect:/u/report";
	}

	@RequestMapping(value = "/{id}", method = GET)
	public String detail(@PathVariable Long id, Principal principal, Model model) {
		model.addAttribute("report", reportComponent.buildVo(findAndValidate(id, principal.getUserId())));
		return "ucenter/report/reportDetail";
	}

	@RequestMapping(value = "/edit", method = GET)
	public String edit(@RequestParam Long id, Principal principal, Model model) {
		Report report = findAndValidate(id, principal.getUserId());
		validate(report, NOT_NULL, "report id" + id + " not found");

		model.addAttribute("report", reportComponent.buildVo(report));
		return "ucenter/report/reportEdit";
	}

	@RequestMapping(value = "/edit", method = POST)
	public String edit(Report report, Principal principal, Model model, RedirectAttributes redirectAttributes) {
		Long id = report.getId();
		validate(id, NOT_NULL, "id is null");
		Report persistence = reportService.findOne(id);
		validate(persistence, NOT_NULL, "report id" + id + " not found");
		
		try {
			reportService.modify(report);
		} catch (Exception e) {
			model.addAttribute(ResultBuilder.error(e.getMessage()));
			return edit(report.getId(), principal, model);
		}
		redirectAttributes.addFlashAttribute(ResultBuilder.ok("更新检测报告成功")); 
		return "redirect:/u/report";
	}

	private Report findAndValidate(Long id, Long userId) {
		Report report = reportService.findOne(id);
		validate(report, NOT_NULL, "report id" + id + " not found");
		validate(report.getUserId(), v -> userId.equals(v), "权限不足");
		return report;
	}
}
