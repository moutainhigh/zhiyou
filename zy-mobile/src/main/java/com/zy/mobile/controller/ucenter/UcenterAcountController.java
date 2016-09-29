package com.zy.mobile.controller.ucenter;

import static com.zy.common.util.ValidateUtils.NOT_BLANK;
import static com.zy.common.util.ValidateUtils.validate;
import static com.zy.entity.fnc.CurrencyType.现金;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.zy.component.AccountLogComponent;
import com.zy.entity.fnc.Account;
import com.zy.entity.fnc.AccountLog;
import com.zy.entity.fnc.AccountLog.InOut;
import com.zy.model.Principal;
import com.zy.model.query.AccountLogQueryModel;
import com.zy.model.query.BankCardQueryModel;
import com.zy.service.AccountLogService;
import com.zy.service.AccountService;
import com.zy.service.BankCardService;

@RequestMapping("/u/account")
@Controller
public class UcenterAcountController {
	
	Logger logger = LoggerFactory.getLogger(UcenterAcountController.class);
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private AccountLogService accountLogService;
	
	@Autowired
	private BankCardService bankCardService;
	
	@Autowired
	private AccountLogComponent accountLogComponent;
	
	@RequestMapping(method = RequestMethod.GET)
	public String index(Principal principal, Model model) {
		Long userId = principal.getUserId();
		Account account = accountService.findByUserIdAndCurrencyType(userId, 现金);
		model.addAttribute("amount", account.getAmount());
		
		BankCardQueryModel bankCardQueryModel = new BankCardQueryModel();
		bankCardQueryModel.setUserIdEQ(principal.getUserId());
		bankCardQueryModel.setIsDeletedEQ(false);
		Long bankCardCount = bankCardService.count(bankCardQueryModel);
		model.addAttribute("bankCardCount", bankCardCount);
		return "ucenter/account/account";
	}
	
	@RequestMapping(value = "/out", method = RequestMethod.GET)
	public String out(@RequestParam String type, Principal principal, Model model) {
		String title = getOutTitle(type);
		AccountLogQueryModel accountLogQueryModel = new AccountLogQueryModel();
		accountLogQueryModel.setTitleLK(title);
		accountLogQueryModel.setInOutEQ(InOut.支出);
		accountLogQueryModel.setCurrencyTypeEQ(现金);
		accountLogQueryModel.setUserIdEQ(principal.getUserId());
		accountLogQueryModel.setPageNumber(0);
		Page<AccountLog> page  = accountLogService.findPage(accountLogQueryModel);
		
		model.addAttribute("page", PageBuilder.copyAndConvert(page, accountLogComponent::buildSimpleVo));
		model.addAttribute("timeLT", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		model.addAttribute("title", title);
		model.addAttribute("type", type);
		return "ucenter/account/accountOut";
	}

	@RequestMapping(value = "/out", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> out(@RequestParam String type, Principal principal, Model model, Date timeLT, @RequestParam(required = true) Integer pageNumber) {
		if (timeLT == null) {
			timeLT = new Date();
		}
		AccountLogQueryModel accountLogQueryModel = new AccountLogQueryModel();
		accountLogQueryModel.setTitleLK(getOutTitle(type));
		accountLogQueryModel.setInOutEQ(InOut.支出);
		accountLogQueryModel.setCurrencyTypeEQ(现金);
		accountLogQueryModel.setTransTimeLT(timeLT);
		accountLogQueryModel.setUserIdEQ(principal.getUserId());
		accountLogQueryModel.setPageNumber(pageNumber);
		Page<AccountLog> page  = accountLogService.findPage(accountLogQueryModel);
		Map<String, Object> map = new HashMap<>();
		map.put("page", PageBuilder.copyAndConvert(page, accountLogComponent::buildSimpleVo));
		map.put("timeLT", DateFormatUtils.format(timeLT, "yyyy-MM-dd HH:mm:ss"));
		model.addAttribute("type", type);
		return ResultBuilder.result(map);
	}
	
	@RequestMapping(value = "/in", method = RequestMethod.GET)
	public String in(@RequestParam String type, Principal principal, Model model) {
		String title = getInTitle(type);
		AccountLogQueryModel accountLogQueryModel = new AccountLogQueryModel();
		accountLogQueryModel.setTitleLK(title);
		accountLogQueryModel.setInOutEQ(InOut.收入);
		accountLogQueryModel.setCurrencyTypeEQ(现金);
		accountLogQueryModel.setUserIdEQ(principal.getUserId());
		accountLogQueryModel.setPageNumber(0);
		Page<AccountLog> page  = accountLogService.findPage(accountLogQueryModel);
		
		model.addAttribute("page", PageBuilder.copyAndConvert(page, accountLogComponent::buildSimpleVo));
		model.addAttribute("timeLT", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		model.addAttribute("title", title);
		model.addAttribute("type", type);
		return "ucenter/account/accountIn";
	}
	
	@RequestMapping(value = "/in", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> in(@RequestParam String type, Principal principal, Model model, Date timeLT, @RequestParam(required = true) Integer pageNumber) {
		if (timeLT == null) {
			timeLT = new Date();
		}
		AccountLogQueryModel accountLogQueryModel = new AccountLogQueryModel();
		accountLogQueryModel.setTitleLK(getInTitle(type));
		accountLogQueryModel.setInOutEQ(InOut.收入);
		accountLogQueryModel.setCurrencyTypeEQ(现金);
		accountLogQueryModel.setTransTimeLT(timeLT);
		accountLogQueryModel.setUserIdEQ(principal.getUserId());
		accountLogQueryModel.setPageNumber(pageNumber);
		Page<AccountLog> page  = accountLogService.findPage(accountLogQueryModel);
		Map<String, Object> map = new HashMap<>();
		map.put("page", PageBuilder.copyAndConvert(page, accountLogComponent::buildSimpleVo));
		map.put("timeLT", DateFormatUtils.format(timeLT, "yyyy-MM-dd HH:mm:ss"));
		model.addAttribute("type", type);
		return ResultBuilder.result(map);
	}
	
	private String getInTitle(String type) {
		String title = null;
		switch (type) {
		case "0":
			title = "订单";
			break;
		case "1":
			title = "数据奖";
			break;
		case "2":
			title = "销量奖";
			break;
		case "3":
			title = "特级平级奖";
			break;
		default:
			break;
		}
		validate(title, NOT_BLANK, "参数错误");
		return title;
	}
	
	private String getOutTitle(String type) {
		String title = null;
		switch (type) {
		case "0":
			title = "数据奖";
			break;
		case "1":
			title = "一级平级奖";
			break;
		case "2":
			title = "一级越级奖";
			break;
		case "3":
			title = "邮费";
			break;
		default:
			break;
		}
		validate(title, NOT_BLANK, "参数错误");
		return title;
	}
}
