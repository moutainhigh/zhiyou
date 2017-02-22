package com.zy.admin.controller.act;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.common.util.ExcelUtils;
import com.zy.common.util.WebUtils;
import com.zy.component.PolicyComponent;
import com.zy.entity.act.Policy;
import com.zy.model.query.PolicyQueryModel;
import com.zy.service.PolicyService;
import com.zy.vo.PolicyAdminVo;
import com.zy.vo.PolicyExportVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

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

	@RequiresPermissions("policy:modify")
	@RequestMapping(value = "/modifyValidTime")
	@ResponseBody
	public Result<?> modifyValidTime(@NotBlank String ids, Date beginTime, Date endTime) {
		if(StringUtils.isBlank(ids)){
			return ResultBuilder.error("请至少选择一条记录！");
		} else {
			String[] idStringArray = ids.split(",");
			for(String idString : idStringArray){
				Long id = new Long(idString);
				Policy policy = policyService.findOne(id);
				validate(policy, NOT_NULL, "policy id" + id + " not found");
				validate(policy, v -> (v.getPolicyStatus() == Policy.PolicyStatus.审核中), "PolicyStatus is error, policy id is " + id + "");
				try {
					policyService.modifyValidTime(id, beginTime, endTime);
				} catch (Exception e) {
					return ResultBuilder.error(e.getMessage());
				}
			}
			return ResultBuilder.ok("ok");
		}
	}

	@RequiresPermissions("policy:modify")
	@RequestMapping(value = "/fail")
	@ResponseBody
	public Result<?> fail(@NotBlank String ids) {
		if(StringUtils.isBlank(ids)){
			return ResultBuilder.error("请至少选择一条记录！");
		} else {
			String[] idStringArray = ids.split(",");
			for(String idString : idStringArray){
				Long id = new Long(idString);
				Policy policy = policyService.findOne(id);
				validate(policy, NOT_NULL, "policy id" + id + " not found");
				validate(policy, v -> (v.getPolicyStatus() == Policy.PolicyStatus.审核中), "PolicyStatus is error, policy id is " + id + "");
				try {
					policyService.fail(id);
				} catch (Exception e) {
					return ResultBuilder.error(e.getMessage());
				}
			}
			return ResultBuilder.ok("ok");
		}
	}

}
