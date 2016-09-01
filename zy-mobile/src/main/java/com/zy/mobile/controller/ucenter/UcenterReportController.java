package com.zy.mobile.controller.ucenter;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.model.result.ResultBuilder;
import com.zy.component.ReportComponent;
import com.zy.entity.act.Report;
import com.zy.model.Principal;
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
		//TODO
		return "ucenter/report/reportList";
	}

	@RequestMapping(value = "create", method = GET)
	public String create(Model model) {
		//TODO
		return "ucenter/report/reportCreate";
	}

	@RequestMapping(value = "/create", method = POST)
	public String create(Report report, Principal principal, Model model, RedirectAttributes redirectAttributes) {
		report.setUserId(principal.getUserId());
		try {
			reportService.create(report);
		} catch (Exception e) {
			model.addAttribute("report", report);
			model.addAttribute(ResultBuilder.error(e.getMessage()));
			return create(model);
		}
		redirectAttributes.addFlashAttribute(ResultBuilder.ok("上传检测报告成功"));
		return "redirect:/u/report";
	}

	@RequestMapping(value = "/{id}", method = GET)
	public String detail(@PathVariable Long id, Principal principal, Model model) {
		//TODO
		return "ucenter/report/reportDetail";
	}

	@RequestMapping(value = "/edit", method = GET)
	public String edit(Long id, Principal principal, Model model) {
		//TODO
		return "ucenter/report/reportEdit";
	}

	@RequestMapping(value = "/edit", method = POST)
	public String edit(Report report, Principal principal, Model model, RedirectAttributes redirectAttributes) {
		try {
			//TODO
		} catch (Exception e) {
			model.addAttribute(ResultBuilder.error(e.getMessage()));
			return edit(report.getId(), principal, model);
		}
		redirectAttributes.addFlashAttribute(ResultBuilder.ok("更新检测报告成功"));
		return "redirect:/u/report";
	}

}
