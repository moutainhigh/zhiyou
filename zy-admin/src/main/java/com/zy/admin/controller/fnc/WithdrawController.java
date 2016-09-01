package com.zy.admin.controller.fnc;


import java.util.List;

import com.zy.admin.model.AdminPrincipal;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zy.common.exception.BizException;
import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.gc.component.WithdrawComponent;
import com.gc.entity.fnc.Withdraw;
import com.gc.entity.usr.User;
import com.gc.model.query.UserQueryModel;
import com.gc.model.query.WithdrawQueryModel;
import com.gc.service.UserService;
import com.gc.service.WithdrawService;
import com.gc.vo.WithdrawAdminVo;


@RequestMapping("/withdraw")
@Controller
public class WithdrawController {
	

	@Autowired
	private WithdrawService withdrawService;
	
	@Autowired
	private WithdrawComponent withdrawComponent;
	
	@Autowired
	private UserService userService;

	@RequiresPermissions("withdraw:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "fnc/withdrawList";
	}

	@RequiresPermissions("withdraw:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<WithdrawAdminVo> list(WithdrawQueryModel withdrawQueryModel, String userPhoneEQ, String userNicknameLK) {
		
		if (StringUtils.isNotBlank(userPhoneEQ) || StringUtils.isNotBlank(userNicknameLK)) {
        	UserQueryModel userQueryModel = new UserQueryModel();
        	userQueryModel.setPhoneEQ(userPhoneEQ);
        	userQueryModel.setNicknameLK(userNicknameLK);
        	
            List<User> users = userService.findAll(userQueryModel);
            if (users == null || users.size() == 0) {
                return new Grid<WithdrawAdminVo>(PageBuilder.empty(withdrawQueryModel.getPageSize(), withdrawQueryModel.getPageNumber()));
            }
            Long[] userIds = users.stream().map(v -> v.getId()).toArray(Long[]::new);
            withdrawQueryModel.setUserIdIN(userIds);
        }
		
		Page<Withdraw> page = withdrawService.findPage(withdrawQueryModel);
		Page<WithdrawAdminVo> adminVoPage = PageBuilder.copyAndConvert(page, withdrawComponent::buildAdminVo);
		return new Grid<WithdrawAdminVo>(adminVoPage);
	}
	
	@RequiresPermissions("withdraw:confirm")
	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> confirm(@RequestParam Long id, @RequestParam boolean isSuccess, String confirmRemark) {
		try {
			AdminPrincipal principal = (AdminPrincipal)SecurityUtils.getSubject().getPrincipal();
			Long operatorUserId = principal.getUserId();
			if(isSuccess == true){
				withdrawService.success(id, operatorUserId, confirmRemark);
			}else{
				withdrawService.cancel(id, operatorUserId, confirmRemark);
			}
		} catch (BizException e) {
			return new ResultBuilder<>().message(e.getMessage()).build();
		}
		return new ResultBuilder<>().message("操作成功").build();
	}

}
