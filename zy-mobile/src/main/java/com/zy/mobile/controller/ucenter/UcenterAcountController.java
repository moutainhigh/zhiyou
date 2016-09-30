package com.zy.mobile.controller.ucenter;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.component.ProfitComponent;
import com.zy.component.TransferComponent;
import com.zy.entity.fnc.Account;
import com.zy.entity.fnc.AccountLog;
import com.zy.entity.fnc.AccountLog.InOut;
import com.zy.entity.fnc.Profit;
import com.zy.entity.fnc.Transfer;
import com.zy.model.Principal;
import com.zy.model.query.AccountLogQueryModel;
import com.zy.model.query.BankCardQueryModel;
import com.zy.model.query.TransferQueryModel;
import com.zy.service.*;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.sun.tools.doclets.formats.html.markup.HtmlStyle.title;
import static com.zy.entity.fnc.CurrencyType.现金;

@RequestMapping("/u/account")
@Controller
public class UcenterAcountController {
	
	Logger logger = LoggerFactory.getLogger(UcenterAcountController.class);
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private AccountLogService accountLogService;

	@Autowired
	private ProfitService prfoitService;

	@Autowired
	private TransferService transferService;
	
	@Autowired
	private BankCardService bankCardService;
	
	@Autowired
	private TransferComponent transferComponent;

	@Autowired
	private ProfitComponent profitComponent;
	
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
	public String out(@RequestParam Transfer.TransferType type, Principal principal, Model model) {

		Date timeLT = new Date();

		TransferQueryModel transferQueryModel = new TransferQueryModel();
		transferQueryModel.setFromUserIdEQ(principal.getUserId());
		transferQueryModel.setPageNumber(0);
		transferQueryModel.setTransferTypeEQ(type);
		transferQueryModel.setCreatedTimeLT(timeLT);

		Page<Transfer> page  = transferService.findPage(transferQueryModel);
		
		model.addAttribute("page", PageBuilder.copyAndConvert(page, transferComponent::buildListVo));
		model.addAttribute("timeLT", DateFormatUtils.format(timeLT, "yyyy-MM-dd HH:mm:ss"));
		model.addAttribute("title", title);
		model.addAttribute("type", type);
		return "ucenter/account/accountOut";
	}

	@RequestMapping(value = "/out", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> out(@RequestParam Transfer.TransferType type, Principal principal, Model model, Date timeLT, @RequestParam(required = true) Integer pageNumber) {
		if (timeLT == null) {
			timeLT = new Date();
		}
		TransferQueryModel transferQueryModel = new TransferQueryModel();
		transferQueryModel.setFromUserIdEQ(principal.getUserId());
		transferQueryModel.setPageNumber(0);
		transferQueryModel.setTransferTypeEQ(type);
		transferQueryModel.setCreatedTimeLT(timeLT);
		transferQueryModel.setPageNumber(pageNumber);
		Page<AccountLog> page  = accountLogService.findPage(accountLogQueryModel);
		Map<String, Object> map = new HashMap<>();
		map.put("page", PageBuilder.copyAndConvert(page, accountLogComponent::buildSimpleVo));
		map.put("timeLT", DateFormatUtils.format(timeLT, "yyyy-MM-dd HH:mm:ss"));
		model.addAttribute("type", type);
		return ResultBuilder.result(map);
	}
	
	@RequestMapping(value = "/in", method = RequestMethod.GET)
	public String in(@RequestParam Profit.ProfitType type, Principal principal, Model model) {
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
	

}
