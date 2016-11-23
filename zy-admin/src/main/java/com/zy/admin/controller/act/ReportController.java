package com.zy.admin.controller.act;

import com.zy.admin.model.AdminPrincipal;
import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.common.util.ExcelUtils;
import com.zy.common.util.WebUtils;
import com.zy.component.ProductComponent;
import com.zy.component.ReportComponent;
import com.zy.entity.act.Report;
import com.zy.entity.act.Report.ReportResult;
import com.zy.entity.mal.Product;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.usr.User;
import com.zy.model.Constants;
import com.zy.model.query.ProductQueryModel;
import com.zy.model.query.ReportQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.JobService;
import com.zy.service.ProductService;
import com.zy.service.ReportService;
import com.zy.service.UserService;
import com.zy.vo.ReportAdminVo;
import com.zy.vo.ReportExportVo;
import org.apache.commons.lang3.StringUtils;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@RequestMapping("/report")
@Controller
public class ReportController {

	@Autowired
	private ReportService reportService;

	@Autowired
	private UserService userService;

	@Autowired
	private JobService jobService;
	
	@Autowired
	private ReportComponent reportComponent;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductComponent productComponent;
	
	@RequiresPermissions("report:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("confirmStatus", ConfirmStatus.values());
		model.addAttribute("reportResults", ReportResult.values());
		return "act/reportList";
	}

	@RequiresPermissions("report:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<ReportAdminVo> list(ReportQueryModel reportQueryModel/*, String phoneEQ, String nicknameLK*/) {

		/*if (StringUtils.isNotBlank(phoneEQ) || StringUtils.isNotBlank(nicknameLK)) {
			UserQueryModel userQueryModel = new UserQueryModel();
			userQueryModel.setPhoneEQ(phoneEQ);
			userQueryModel.setNicknameLK(nicknameLK);
			List<User> users = userService.findAll(userQueryModel);
			if (users.isEmpty()) {
				return new Grid<>(PageBuilder.empty(reportQueryModel.getPageSize(), reportQueryModel.getPageNumber()));
			}
			Long[] userIds = users.stream().map(v -> v.getId()).toArray(Long[]::new);
			reportQueryModel.setUserIdIN(userIds);
		}*/

		Page<Report> page = reportService.findPage(reportQueryModel);
		return new Grid<ReportAdminVo>(PageBuilder.copyAndConvert(page, reportComponent::buildAdminVo));
	}

	@RequiresPermissions("report:edit")
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(Model model) {
		model.addAttribute("jobs", jobService.findAll());
		model.addAttribute("reportResults", ReportResult.values());
		
		ProductQueryModel productQueryModel = new ProductQueryModel();
		productQueryModel.setIsOnEQ(true);
		List<Product> products = productService.findAll(productQueryModel);
		model.addAttribute("products", products.stream().map(productComponent::buildListVo).collect(Collectors.toList()));
		return "act/reportCreate";
	}

	@RequiresPermissions("report:edit")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(Report report, Model model, RedirectAttributes redirectAttributes) {
		Product product = productService.findOne(report.getProductId());
		validate(product, NOT_NULL, "product id " + report.getProductId() + " not found");
		
		AdminPrincipal principal = (AdminPrincipal) SecurityUtils.getSubject().getPrincipal();
		report.setUserId(principal.getUserId());
		try {
			reportService.adminCreate(report);
		} catch (Exception e) {
			model.addAttribute("report", reportComponent.buildDetailVo(report));
			model.addAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
			return create(model);
		}
		redirectAttributes.addFlashAttribute(ResultBuilder.ok("上传检测报告成功"));
		return "redirect:/report";
	}
	
	@RequiresPermissions("report:edit")
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public String update(@RequestParam Long id, Model model) {
		Report report = reportService.findOne(id);
		validate(report, NOT_NULL, "report id " + id + " not found");
		model.addAttribute("report", reportComponent.buildDetailVo(report));
		model.addAttribute("jobs", jobService.findAll());
		model.addAttribute("reportResults", ReportResult.values());
		return "act/reportUpdate";
	}

	@RequiresPermissions("report:edit")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Report report, Model model, RedirectAttributes redirectAttributes) {
		Long id = report.getId();
		validate(id, NOT_NULL, "id is null");
		try {
			reportService.adminModify(report);
		} catch (Exception e) {
			model.addAttribute("report", reportComponent.buildDetailVo(report));
			model.addAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
			return update(report.getId(),model);
		}
		redirectAttributes.addFlashAttribute(ResultBuilder.ok("检测报告编辑成功"));
		return "redirect:/report";
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
