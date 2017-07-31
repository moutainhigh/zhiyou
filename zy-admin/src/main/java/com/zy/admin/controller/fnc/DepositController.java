package com.zy.admin.controller.fnc;


import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import com.zy.common.util.weiXinUtils.SendPaySuccessMsg;
import com.zy.common.util.weiXinUtils.Token;
import com.zy.component.TokenComponent;
import com.zy.vo.DepositExportVo;
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
import com.zy.entity.fnc.Deposit.DepositStatus;
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

	@Autowired
	private TokenComponent tokenComponent;

	@RequiresPermissions("deposit:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("depositStatuses", Arrays.asList(DepositStatus.values()));
		model.addAttribute("payTypes", Arrays.asList(PayType.values()));
		return "fnc/depositList";
	}

	@RequiresPermissions("deposit:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<DepositAdminVo> list(DepositQueryModel depositQueryModel, String phoneEQ, String nicknameLK) {

		fillQueryModel(depositQueryModel, phoneEQ, nicknameLK);
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
			//在这里推送消息  推送不成功则 吞掉异常
			try{
				User user = userService.findOne(deposit.getUserId());
				if (user!=null&&user.getOpenId()!=null){
					Token token = tokenComponent.getToken();
					if (token!=null){//获取到token才推送消息
						SendPaySuccessMsg.send_template_message(deposit.getSn(),"U币充值",deposit.getTotalAmount(),user.getOpenId(),token);
					}
				}
			  }catch (Exception e){
				e.printStackTrace();
			}

		} else {
			depositService.offlineFailure(id, principal.getUserId(), remark);
		}
		return ResultBuilder.ok("充值单[" + deposit.getTitle() + "]确认支付成功");
	}
	
	@RequiresPermissions("deposit:export")
	@RequestMapping("/export")
	public String export(DepositQueryModel depositQueryModel, String phoneEQ, String nicknameLK,
			HttpServletResponse response) throws IOException {


		fillQueryModel(depositQueryModel, phoneEQ, nicknameLK);
		depositQueryModel.setPageNumber(null);
		depositQueryModel.setPageSize(null);
		List<Deposit> deposits = depositService.findAll(depositQueryModel);
		String fileName = "充值数据.xlsx";
		WebUtils.setFileDownloadHeader(response, fileName);

		List<DepositExportVo> depositExportVos = deposits.stream().map(depositComponent::buildExportVo).collect(Collectors.toList());
		OutputStream os = response.getOutputStream();
		ExcelUtils.exportExcel(depositExportVos, DepositExportVo.class, os);
		return null;
	}

	@RequestMapping(value = "/sum", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> sum(DepositQueryModel depositQueryModel, String phoneEQ, String nicknameLK) {
		
		fillQueryModel(depositQueryModel, phoneEQ, nicknameLK);
		
		return ResultBuilder.result(depositService.sum(depositQueryModel));
	}
	
	private void fillQueryModel(DepositQueryModel depositQueryModel, String phoneEQ, String nicknameLK) {
		if(StringUtils.isNotBlank(phoneEQ) || StringUtils.isNotBlank(nicknameLK)) {
			UserQueryModel userQueryModel = new UserQueryModel();
			userQueryModel.setPhoneEQ(phoneEQ);
			userQueryModel.setNicknameLK(nicknameLK);
			List<User> users = userService.findAll(userQueryModel);
			depositQueryModel.setUserIdIN(users.stream().map(v -> v.getId()).toArray(Long[]::new));
		}
	}
	
}
