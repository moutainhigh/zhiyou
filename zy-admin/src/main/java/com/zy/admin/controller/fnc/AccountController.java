package com.zy.admin.controller.fnc;

import com.zy.common.extend.BigDecimalBinder;
import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.component.AccountComponent;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserType;
import com.zy.model.query.UserQueryModel;
import com.zy.service.ProfitService;
import com.zy.service.UserService;
import com.zy.vo.AccountAdminVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Date;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@RequestMapping("/account")
@Controller
public class AccountController {

	@Autowired
	private AccountComponent accountComponent;

	@Autowired
	private UserService userService;

	@Autowired
	private ProfitService profitService;

	@RequiresPermissions("account:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "fnc/accountList";
	}

	@RequiresPermissions("account:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<AccountAdminVo> list(UserQueryModel userQueryModel) {
		Page<User> page = userService.findPage(userQueryModel);
		Page<AccountAdminVo> voPage = PageBuilder.copyAndConvert(page, accountComponent.buildAdminVo(page.getData()));
		return new Grid<>(voPage);
	}

	@RequiresPermissions("account:deposit")
	@RequestMapping("/deposit")
	@ResponseBody
	public Result<?> deposit(Long userId, @BigDecimalBinder BigDecimal amount, String remark) {

		User user = userService.findOne(userId);
		validate(user, NOT_NULL, "user id" + userId + " not found");
		if (user.getUserType() == UserType.平台) {
			return ResultBuilder.error("不能给平台账号充值");
		}

		try {
			profitService.createAndGrant(userId, remark, CurrencyType.现金, amount, new Date());
		} catch (Exception e) {
			return ResultBuilder.error("充值失败," + e.getMessage());
		}
		return ResultBuilder.ok("充值成功!");
	}

}
