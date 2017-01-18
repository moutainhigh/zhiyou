package com.zy.admin.controller.act;

import com.zy.admin.model.AdminPrincipal;
import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.component.ReportComponent;
import com.zy.component.ReportVisitedLogComponent;
import com.zy.entity.act.ReportVisitedLog;
import com.zy.model.Constants;
import com.zy.model.query.ReportVisitedLogQueryModel;
import com.zy.service.ReportService;
import com.zy.service.ReportVisitedLogService;
import com.zy.util.GcUtils;
import com.zy.vo.ReportVisitedLogListVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.zy.common.model.result.ResultBuilder.ok;
import static com.zy.model.Constants.MODEL_ATTRIBUTE_RESULT;

@RequestMapping("/reportVisitedLog")
@Controller
public class ReportVisitedLogController {

	@Autowired
	private ReportVisitedLogService reportVisitedLogService;

	@Autowired
	private ReportService reportService;

	@Autowired
	private ReportVisitedLogComponent reportVisitedLogComponent;

	@Autowired
	private ReportComponent reportComponent;

	@RequiresPermissions("reportVisitedLog:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model, Long reportId) {
		model.addAttribute("reportId", reportId);
		return "act/reportVisitedLogList";
	}

	@RequiresPermissions("reportVisitedLog:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<ReportVisitedLogListVo> list(ReportVisitedLogQueryModel reportVisitedLogQueryModel) {
//		Long reportId = reportVisitedLogQueryModel.getReportIdEQ();
//
//		Long principalUserId = getPrincipalUserId();
//		if(!principalUserId.equals(Constants.SETTING_SUPER_ADMIN_ID)) {
//			List<Report> reports = reportService.findAll(ReportQueryModel.builder().visitUserIdEQ(principalUserId).build());
//			if(reportId != null) {
//				if(!reports.contains(reportId)) {
//					reportVisitedLogQueryModel.setIdEQ(66666666L);
//				}
//			} else {
//				reportVisitedLogQueryModel.setReportIdIN(reports.stream().map(v -> v.getId()).toArray(Long[]::new));
//			}
//		}
		Page<ReportVisitedLog> page = reportVisitedLogService.findPage(reportVisitedLogQueryModel);
		return new Grid<ReportVisitedLogListVo>(PageBuilder.copyAndConvert(page, reportVisitedLogComponent::buildListVo));
	}

	@RequiresPermissions("reportVisitedLog:edit")
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(@RequestParam Long reportId, Model model) {
		ReportVisitedLog byReportId = reportVisitedLogService.findByReportId(reportId);
		if (byReportId == null) {
			model.addAttribute("report", reportComponent.buildAdminVo(reportService.findOne(reportId)));
			model.addAttribute("map", GcUtils.getVisitedMap());
			model.addAttribute("visitedStatusList", Constants.visitedStatusList);
			return "act/reportVisitedLogCreate";
		}
		return "redirect:/reportVisitedLog";
	}

	@RequiresPermissions("reportVisitedLog:edit")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(ReportVisitedLog reportVisitedLog, Model model, RedirectAttributes redirectAttributes) {
		try{
			reportVisitedLogService.create(reportVisitedLog);
			redirectAttributes.addFlashAttribute(MODEL_ATTRIBUTE_RESULT, ok("操作成功"));
			return "redirect:/reportVisitedLog";
		} catch (Exception e) {
			model.addAttribute("report", reportComponent.buildAdminVo(reportService.findOne(reportVisitedLog.getReportId())));
			model.addAttribute("map", GcUtils.getVisitedMap());
			model.addAttribute("visitedStatusList", Constants.visitedStatusList);
			model.addAttribute("reportVisitedLog", reportVisitedLog);
			model.addAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
			return "redirect:/reportVisitedLog/create?reportId=" + reportVisitedLog.getReportId();
		}

	}

	@RequiresPermissions("reportVisitedLog:edit")
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public String update(@RequestParam Long id, Model model) {
		ReportVisitedLog one = reportVisitedLogService.findOne(id);
		model.addAttribute("report", reportComponent.buildAdminVo(reportService.findOne(one.getReportId())));
		model.addAttribute("reportVisitedLog", reportVisitedLogComponent.buildAdminVo(one));
		model.addAttribute("map", GcUtils.getVisitedMap());
		model.addAttribute("visitedStatusList", Constants.visitedStatusList);
		return "act/reportVisitedLogUpdate";

	}

	@RequiresPermissions("reportVisitedLog:edit")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> update(ReportVisitedLog reportVisitedLog, Model model) {
		try {
			reportVisitedLogService.modify(reportVisitedLog);
			return ResultBuilder.ok("保存成功");
		} catch (Exception e) {
			return ResultBuilder.error(e.getMessage());
		}

	}

	private Long getPrincipalUserId() {
		AdminPrincipal principal = (AdminPrincipal) SecurityUtils.getSubject().getPrincipal();
		return principal.getUserId();
	}
}
