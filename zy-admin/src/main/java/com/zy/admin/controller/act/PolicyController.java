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

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.common.util.ExcelUtils;
import com.zy.common.util.WebUtils;
import com.zy.component.PolicyComponent;
import com.zy.entity.act.Policy;
import com.zy.model.query.PolicyQueryModel;
import com.zy.service.PolicyService;
import com.zy.vo.PolicyAdminVo;
import com.zy.vo.PolicyExportVo;

@RequestMapping("/policy")
@Controller
public class PolicyController {

	@Autowired
	private PolicyService policyService;
	
	@Autowired
	private PolicyComponent policyComponent;
	
	@RequiresPermissions("policy:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "act/policyList";
	}

	@RequiresPermissions("policy:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<PolicyAdminVo> list(PolicyQueryModel policyQueryModel) {
		Page<Policy> page = policyService.findPage(policyQueryModel);
		Page<PolicyAdminVo> voPage = PageBuilder.copyAndConvert(page, policyComponent::buildAdminVo);
		return new Grid<>(voPage);
	}
	
	@RequiresPermissions("policy:export")
	@RequestMapping("/export")
	public String export(PolicyQueryModel policyQueryModel,
			HttpServletResponse response) throws IOException {

		policyQueryModel.setPageSize(null);
		policyQueryModel.setPageNumber(null);
		List<Policy> polisys = policyService.findAll(policyQueryModel);
		String fileName = "投保名单.xlsx";
		WebUtils.setFileDownloadHeader(response, fileName);

		List<PolicyExportVo> policyExportVos = polisys.stream().map(policyComponent::buildExportVo).collect(Collectors.toList());
		OutputStream os = response.getOutputStream();
		ExcelUtils.exportExcel(policyExportVos, PolicyExportVo.class, os);

		return null;
	}
}
