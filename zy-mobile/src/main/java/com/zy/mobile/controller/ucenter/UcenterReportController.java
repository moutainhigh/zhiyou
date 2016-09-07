package com.zy.mobile.controller.ucenter;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.ResultBuilder;
import com.zy.component.ReportComponent;
import com.zy.entity.act.Report;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.model.query.ReportQueryModel;
import com.zy.service.ReportService;

@RequestMapping("/u/report")
@Controller
public class UcenterReportController {

	@Autowired
	private ReportService reportService;

	@Autowired
	private ReportComponent reportComponent;

	@RequestMapping()
	public String list(Principal principal, Model model) {
		Page<Report> page = reportService.findPage(ReportQueryModel.builder().userIdEQ(principal.getUserId()).build());
		model.addAttribute("page", PageBuilder.copyAndConvert(page, reportComponent::buildVo));
		return "ucenter/report/reportList";
	}

	@RequestMapping(value = "create", method = GET)
	public String create(Model model) {

		return "ucenter/report/reportCreate";
	}

	@RequestMapping(value = "/create", method = POST)
	public String create(Report report, Principal principal, Model model, RedirectAttributes redirectAttributes) {
		report.setUserId(principal.getUserId());
		try {
			reportService.create(report);
		} catch (Exception e) {
			model.addAttribute("report", report);
			model.addAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
			return create(model);
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
	public String edit(Long id, Principal principal, Model model) {
		Report report = findAndValidate(id, principal.getUserId());
		
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return "ucenter/report/reportEdit";
	}

	@RequestMapping(value = "/edit", method = POST)
	public String edit(Report report, Principal principal, Model model, RedirectAttributes redirectAttributes) {
		try {
			
		} catch (Exception e) {
			model.addAttribute(ResultBuilder.error(e.getMessage()));
			return edit(report.getId(), principal, model);
		}
		redirectAttributes.addFlashAttribute(ResultBuilder.ok("更新检测报告成功"));
		return "redirect:/u/report";
	}

	private Report findAndValidate(Long id, Long userId) {
		Report report = reportService.findOne(id);
		Validate.notNull(report, "report id" + id + " not found");
		Validate.isTrue(userId.equals(report.getUserId()), "权限不足");
		
		return report;
	}
}
