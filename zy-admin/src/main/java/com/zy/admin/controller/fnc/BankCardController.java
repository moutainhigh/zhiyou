package com.zy.admin.controller.fnc;

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
import com.zy.component.BankCardComponent;
import com.zy.entity.fnc.BankCard;
import com.zy.model.query.BankCardQueryModel;
import com.zy.service.BankCardService;
import com.zy.vo.BankCardAdminVo;

@RequestMapping("/bankCard")
@Controller
public class BankCardController {

	@Autowired
	private BankCardService bankCardService;
	
	@Autowired
	private BankCardComponent bankCardComponent;
	
	@RequiresPermissions("bankCard:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "fnc/bankCardList";
	}
	
	@RequiresPermissions("bankCard:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<BankCardAdminVo> list(BankCardQueryModel bankCardQueryModel) {
		Page<BankCard> page = bankCardService.findPage(bankCardQueryModel);
		return new Grid<BankCardAdminVo>(PageBuilder.copyAndConvert(page, bankCardComponent::buildAdminVo));
	}
	
	@RequiresPermissions("bankCard:confirm")
	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> confirm(@RequestParam Long id, @RequestParam boolean isSuccess, String confirmRemark) {
		try{
			bankCardService.confirm(id, isSuccess, confirmRemark);
		}catch (BizException e) {
			return ResultBuilder.error("审核失败:" + e.getMessage());
		}
		return ResultBuilder.ok("审核成功");
	}
}
