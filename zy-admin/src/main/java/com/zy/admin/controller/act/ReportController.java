package com.zy.admin.controller.act;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.common.util.ExcelUtils;
import com.zy.common.util.WebUtils;
import com.zy.component.ReportComponent;
import com.zy.entity.act.Report;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.usr.User;
import com.zy.model.query.ReportQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.ReportService;
import com.zy.service.UserService;
import com.zy.vo.ReportAdminVo;
import com.zy.vo.ReportExportVo;

@RequestMapping("/report")
@Controller
public class ReportController {

	@Autowired
	private ReportService reportService;

	@Autowired
	private UserService userService;

	@Autowired
	private ReportComponent reportComponent;

	@RequiresPermissions("report:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("confirmStatus", ConfirmStatus.values());
		return "act/reportList";
	}

	@RequiresPermissions("report:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<ReportAdminVo> list(ReportQueryModel reportQueryModel, String phoneEQ, String nicknameLK) {

		if (StringUtils.isNotBlank(phoneEQ) || StringUtils.isNotBlank(nicknameLK)) {
			UserQueryModel userQueryModel = new UserQueryModel();
			userQueryModel.setPhoneEQ(phoneEQ);
			userQueryModel.setNicknameLK(nicknameLK);
			List<User> users = userService.findAll(userQueryModel);
			if (users.isEmpty()) {
				return new Grid<>(PageBuilder.empty(reportQueryModel.getPageSize(), reportQueryModel.getPageNumber()));
			}
			Long[] userIds = users.stream().map(v -> v.getId()).toArray(Long[]::new);
			reportQueryModel.setUserIdIN(userIds);
		}

		Page<Report> page = reportService.findPage(reportQueryModel);
		return new Grid<ReportAdminVo>(PageBuilder.copyAndConvert(page, reportComponent::buildAdminVo));
	}

	@RequiresPermissions("report:confirm")
	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> confirm(@RequestParam Long id, @RequestParam boolean isSuccess, String confirmRemark) {
		reportService.confirm(id, isSuccess, confirmRemark);
		return ResultBuilder.ok("操作成功");
	}
	
	@RequiresPermissions("report:preConfirm")
	@RequestMapping(value = "/preConfirm", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> preConfirm(@RequestParam Long id, @RequestParam boolean isSuccess, String confirmRemark) {
		reportService.preConfirm(id, isSuccess, confirmRemark);
		return ResultBuilder.ok("操作成功");
	}

	@RequiresPermissions("report:export")
	@RequestMapping("/export")
	public String export(ReportQueryModel reportQueryModel, String phoneEQ, String nicknameLK,
			HttpServletResponse response) throws IOException {

		List<Report> reports = new ArrayList<>();
		boolean empty = false;
		if(StringUtils.isNotBlank(phoneEQ) || StringUtils.isNotBlank(nicknameLK)) {
			UserQueryModel userQueryModel = new UserQueryModel();
			userQueryModel.setPhoneEQ(phoneEQ);
			userQueryModel.setNicknameLK(nicknameLK);
			List<User> users = userService.findAll(userQueryModel);
			if(users.isEmpty()) {
				empty = true;
			}
			Long[] userIds = users.stream().map(v -> v.getId()).toArray(Long[]::new);

			reportQueryModel.setUserIdIN(userIds);
		}
		if (! empty) {
			reportQueryModel.setPageSize(null);
			reportQueryModel.setPageNumber(null);
			reports = reportService.findAll(reportQueryModel);
		}
		String fileName = "检测报告.xlsx";
		WebUtils.setFileDownloadHeader(response, fileName);

		List<ReportExportVo> reportExportVos = reports.stream().map(reportComponent::buildExportVo).collect(Collectors.toList());
		OutputStream os = response.getOutputStream();
		ExcelUtils.exportExcel(reportExportVos, ReportExportVo.class, os);

		return null;
	}
}
