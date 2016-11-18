package com.zy.admin.controller.act;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.common.util.ExcelUtils;
import com.zy.common.util.WebUtils;
import com.zy.component.PolicyCodeComponent;
import com.zy.entity.act.PolicyCode;
import com.zy.model.Constants;
import com.zy.model.query.PolicyCodeQueryModel;
import com.zy.service.PolicyCodeService;
import com.zy.vo.PolicyCodeAdminVo;
import com.zy.vo.PolicyCodeExportVo;


@RequestMapping("/policyCode")
@Controller
public class PolicyCodeController {

	@Autowired
	private PolicyCodeService policyCodeService;
	
	@Autowired
	private PolicyCodeComponent policyCodeComponent;
	
	@RequiresPermissions("policyCode:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "act/policyCodeList";
	}

	@RequiresPermissions("policyCode:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<PolicyCodeAdminVo> list(PolicyCodeQueryModel policyCodeQueryModel) {
		Page<PolicyCode> page = policyCodeService.findPage(policyCodeQueryModel);
		Page<PolicyCodeAdminVo> voPage = PageBuilder.copyAndConvert(page, policyCodeComponent::buildAdminVo);
		return new Grid<>(voPage);
	}
	
	@RequiresPermissions("policyCode:edit")
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create() {
		return "act/policyCodeCreate";
	}
	
	@RequiresPermissions("policyCode:edit")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(String batchCode, Long time, RedirectAttributes redirectAttributes) {
		
		try {
			policyCodeService.createByBatchCode(batchCode, time);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("新增成功"));
			return "redirect:/policyCode";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
			return "redirect:/policyCode/create";
		}
		
	}
	
	@RequiresPermissions("policy:export")
	@RequestMapping("/export")
	public String export(PolicyCodeQueryModel policyCodeQueryModel,
			HttpServletResponse response) throws IOException {

		List<PolicyCode> polisyCodes = policyCodeService.findAll(policyCodeQueryModel);
		String fileName = "保险单号.xlsx";
		WebUtils.setFileDownloadHeader(response, fileName);

		List<PolicyCodeExportVo> policyExportVos = polisyCodes.stream().map(policyCodeComponent::buildExportVo).collect(Collectors.toList());
		OutputStream os = response.getOutputStream();
		ExcelUtils.exportExcel(policyExportVos, PolicyCodeExportVo.class, os);

		return null;
	}
}
