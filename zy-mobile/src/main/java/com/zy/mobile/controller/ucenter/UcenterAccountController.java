package com.zy.mobile.controller.ucenter;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.component.DepositComponent;
import com.zy.component.ProfitComponent;
import com.zy.component.TransferComponent;
import com.zy.component.WithdrawComponent;
import com.zy.entity.fnc.*;
import com.zy.model.Principal;
import com.zy.model.query.*;
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

import static com.zy.entity.fnc.CurrencyType.现金;

@RequestMapping("/u/account")
@Controller
public class UcenterAccountController {
	
	Logger logger = LoggerFactory.getLogger(UcenterAccountController.class);
	
	private final int pageSize = 20;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private ProfitService profitService;

	@Autowired
	private WithdrawService withdrawService;

	@Autowired
	private DepositService depositService;

	@Autowired
	private TransferService transferService;
	
	@Autowired
	private BankCardService bankCardService;
	
	@Autowired
	private TransferComponent transferComponent;

	@Autowired
	private ProfitComponent profitComponent;

	@Autowired
	private WithdrawComponent withdrawComponent;

	@Autowired
	private DepositComponent depositComponent;
	
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
		transferQueryModel.setPageSize(pageSize);

		Page<Transfer> page  = transferService.findPage(transferQueryModel);
		
		model.addAttribute("page", PageBuilder.copyAndConvert(page, transferComponent::buildListVo));
		model.addAttribute("timeLT", DateFormatUtils.format(timeLT, "yyyy-MM-dd HH:mm:ss"));
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
		transferQueryModel.setTransferTypeEQ(type);
		transferQueryModel.setCreatedTimeLT(timeLT);
		transferQueryModel.setPageSize(pageSize);
		transferQueryModel.setPageNumber(pageNumber);
		
		Page<Transfer> page  = transferService.findPage(transferQueryModel);
		Map<String, Object> map = new HashMap<>();
		map.put("page", PageBuilder.copyAndConvert(page, transferComponent::buildListVo));
		map.put("timeLT", DateFormatUtils.format(timeLT, "yyyy-MM-dd HH:mm:ss"));
		model.addAttribute("type", type);
		return ResultBuilder.result(map);
	}
	
	@RequestMapping(value = "/in", method = RequestMethod.GET)
	public String in(@RequestParam Profit.ProfitType type, Principal principal, Model model) {
		
		Date timeLT = new Date();
		
		ProfitQueryModel profitQueryModel = new ProfitQueryModel();
		profitQueryModel.setProfitTypeEQ(type);
		profitQueryModel.setUserIdEQ(principal.getUserId());
		profitQueryModel.setCreatedTimeLT(timeLT);
		profitQueryModel.setPageNumber(0);
		profitQueryModel.setPageSize(pageSize);
		Page<Profit> page  = profitService.findPage(profitQueryModel);
		
		model.addAttribute("page", PageBuilder.copyAndConvert(page, profitComponent::buildListVo));
		model.addAttribute("timeLT", DateFormatUtils.format(timeLT, "yyyy-MM-dd HH:mm:ss"));
		model.addAttribute("type", type);
		return "ucenter/account/accountIn";
	}
	
	@RequestMapping(value = "/in", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> in(@RequestParam Profit.ProfitType type, Principal principal, Model model, Date timeLT, @RequestParam(required = true) Integer pageNumber) {
		if (timeLT == null) {
			timeLT = new Date();
		}
		ProfitQueryModel profitQueryModel = new ProfitQueryModel();
		profitQueryModel.setProfitTypeEQ(type);
		profitQueryModel.setUserIdEQ(principal.getUserId());
		profitQueryModel.setCreatedTimeLT(timeLT);
		profitQueryModel.setPageNumber(pageNumber);
		profitQueryModel.setPageSize(pageSize);
		
		Page<Profit> page  = profitService.findPage(profitQueryModel);
		Map<String, Object> map = new HashMap<>();
		map.put("page", PageBuilder.copyAndConvert(page, profitComponent::buildListVo));
		map.put("timeLT", DateFormatUtils.format(timeLT, "yyyy-MM-dd HH:mm:ss"));
		model.addAttribute("type", type);
		return ResultBuilder.result(map);
	}


	@RequestMapping(value = "/withdraw", method = RequestMethod.GET)
	public String withdraw(Principal principal, Model model) {

		Date timeLT = new Date();

		WithdrawQueryModel withdrawQueryModel = new WithdrawQueryModel();
		withdrawQueryModel.setUserIdEQ(principal.getUserId());
		withdrawQueryModel.setCreatedTimeLT(timeLT);
		withdrawQueryModel.setPageNumber(0);
		withdrawQueryModel.setPageSize(pageSize);
		Page<Withdraw> page  = withdrawService.findPage(withdrawQueryModel);

		model.addAttribute("page", PageBuilder.copyAndConvert(page, withdrawComponent::buildListVo));
		model.addAttribute("timeLT", DateFormatUtils.format(timeLT, "yyyy-MM-dd HH:mm:ss"));
		return "ucenter/account/withdraw";
	}

	@RequestMapping(value = "/withdraw", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> withdraw(Principal principal, Model model, Date timeLT, @RequestParam(required = true) Integer pageNumber) {
		if (timeLT == null) {
			timeLT = new Date();
		}
		WithdrawQueryModel withdrawQueryModel = new WithdrawQueryModel();
		withdrawQueryModel.setUserIdEQ(principal.getUserId());
		withdrawQueryModel.setCreatedTimeLT(timeLT);
		withdrawQueryModel.setPageNumber(pageNumber);
		withdrawQueryModel.setPageSize(pageSize);
		Page<Withdraw> page  = withdrawService.findPage(withdrawQueryModel);

		Map<String, Object> map = new HashMap<>();
		map.put("page", PageBuilder.copyAndConvert(page, withdrawComponent::buildListVo));
		map.put("timeLT", DateFormatUtils.format(timeLT, "yyyy-MM-dd HH:mm:ss"));
		return ResultBuilder.result(map);
	}

	@RequestMapping(value = "/deposit", method = RequestMethod.GET)
	public String deposit(Principal principal, Model model) {

		Date timeLT = new Date();

		DepositQueryModel depositQueryModel = new DepositQueryModel();
		depositQueryModel.setUserIdEQ(principal.getUserId());
		depositQueryModel.setCreatedTimeLT(timeLT);
		depositQueryModel.setPageNumber(0);
		depositQueryModel.setPageSize(pageSize);
		Page<Deposit> page  = depositService.findPage(depositQueryModel);

		model.addAttribute("page", PageBuilder.copyAndConvert(page, depositComponent::buildListVo));
		model.addAttribute("timeLT", DateFormatUtils.format(timeLT, "yyyy-MM-dd HH:mm:ss"));
		return "ucenter/account/deposit";
	}

	@RequestMapping(value = "/deposit", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deposit(Principal principal, Model model, Date timeLT, @RequestParam(required = true) Integer pageNumber) {
		if (timeLT == null) {
			timeLT = new Date();
		}
		DepositQueryModel depositQueryModel = new DepositQueryModel();
		depositQueryModel.setUserIdEQ(principal.getUserId());
		depositQueryModel.setCreatedTimeLT(timeLT);
		depositQueryModel.setPageNumber(pageNumber);
		depositQueryModel.setPageSize(pageSize);
		Page<Deposit> page  = depositService.findPage(depositQueryModel);

		Map<String, Object> map = new HashMap<>();
		map.put("page", PageBuilder.copyAndConvert(page, depositComponent::buildListVo));
		map.put("timeLT", DateFormatUtils.format(timeLT, "yyyy-MM-dd HH:mm:ss"));
		return ResultBuilder.result(map);
	}

}
