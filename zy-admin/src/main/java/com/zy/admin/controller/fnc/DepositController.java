package com.zy.admin.controller.fnc;


import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zy.admin.model.AdminPrincipal;
import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.common.util.ExcelUtils;
import com.zy.common.util.WebUtils;
import com.zy.component.DepositComponent;
import com.zy.entity.fnc.Deposit;
import com.zy.entity.fnc.PayType;
import com.zy.entity.usr.User;
import com.zy.model.query.DepositQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.DepositService;
import com.zy.service.UserService;
import com.zy.vo.DepositAdminVo;

@RequestMapping("/deposit")
@Controller
public class DepositController {

	@Autowired
	@Lazy
	private DepositService depositService;
	
	@Autowired
	private DepositComponent depositComponent;
	
	@Autowired
	private UserService userService;

	@RequiresPermissions("deposit:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("payTypes", Arrays.asList(PayType.values()));
		return "fnc/depositList";
	}

	@RequiresPermissions("deposit:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<DepositAdminVo> list(DepositQueryModel depositQueryModel, String phoneEQ, String nicknameLK) {

		if(StringUtils.isNotBlank(phoneEQ) || StringUtils.isNotBlank(nicknameLK)) {
			UserQueryModel userQueryModel = new UserQueryModel();
			userQueryModel.setPhoneEQ(phoneEQ);
			userQueryModel.setNicknameLK(nicknameLK);
			List<User> users = userService.findAll(userQueryModel);
			if(users.isEmpty()) {
				return new Grid<DepositAdminVo>(PageBuilder.empty(depositQueryModel.getPageSize(), depositQueryModel.getPageNumber()));
			}
			Long[] userIds = users.stream().map(v -> v.getId()).toArray(Long[]::new);
			depositQueryModel.setUserIdIN(userIds);
		}
		Page<Deposit> page = depositService.findPage(depositQueryModel);
		Page<DepositAdminVo> voPage = PageBuilder.copyAndConvert(page, depositComponent::buildAdminVo);
		
		return new Grid<DepositAdminVo>(voPage);
	}
	
	@RequiresPermissions("deposit:confirmPaid")
	@RequestMapping(value = "/confirmPaid", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> confirmPaid(@RequestParam(required = true) Long id, @RequestParam(required = true) boolean isSuccess, String remark) {
		AdminPrincipal principal = (AdminPrincipal)SecurityUtils.getSubject().getPrincipal();
		Deposit deposit = depositService.findOne(id);
		validate(deposit, NOT_NULL, "deposit id not found,id = " + id);
		if(isSuccess){
			depositService.offlineSuccess(id, principal.getUserId(), remark);
		} else {
			depositService.offlineFailure(id, principal.getUserId(), remark);
		}
		return ResultBuilder.ok("充值单[" + deposit.getTitle() + "]确认支付成功");
	}
	
	@RequiresPermissions("deposit:export")
	@RequestMapping("/export")
	public String export(DepositQueryModel depositQueryModel, String phoneEQ, String nicknameLK,
			HttpServletResponse response) throws IOException {

		List<Deposit> deposits = new ArrayList<>();
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

			depositQueryModel.setUserIdIN(userIds);
		}
		if (! empty) {
			depositQueryModel.setPageSize(null);
			depositQueryModel.setPageNumber(null);
			deposits = depositService.findAll(depositQueryModel);
		}
		String fileName = "充值数据.xlsx";
		WebUtils.setFileDownloadHeader(response, fileName);

		List<DepositAdminVo> depositAdminVos = deposits.stream().map(depositComponent::buildAdminVo).collect(Collectors.toList());
		OutputStream os = response.getOutputStream();
		ExcelUtils.exportExcel(depositAdminVos, DepositAdminVo.class, os);

		return null;
	}
	
}
