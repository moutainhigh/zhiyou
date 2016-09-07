package com.zy.mobile.controller.ucenter;

import static com.zy.entity.fnc.CurrencyType.现金;
import static com.zy.entity.usr.User.UserType.代理;
import static com.zy.model.Constants.MODEL_ATTRIBUTE_RESULT;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.Config;
import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.component.AccountLogComponent;
import com.zy.entity.fnc.Account;
import com.zy.entity.fnc.AccountLog;
import com.zy.entity.fnc.BankCard;
import com.zy.entity.fnc.Withdraw;
import com.zy.model.Principal;
import com.zy.model.query.AccountLogQueryModel;
import com.zy.model.query.BankCardQueryModel;
import com.zy.service.AccountLogService;
import com.zy.service.AccountService;
import com.zy.service.BankCardService;
import com.zy.service.WithdrawService;

@RequestMapping("/u/money")
@Controller
public class UcenterMoneyController {
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private AccountLogService accountLogService;
	
	@Autowired
	private WithdrawService withdrawService;
	
	@Autowired
	private BankCardService bankCardService;
	
	@Autowired
	private AccountLogComponent accountLogComponent;

	@Autowired
	private Config config;
	
	@RequestMapping(method = RequestMethod.GET)
	public String index(Principal principal, Model model) {
		Long userId = principal.getUserId();
		Account account = accountService.findByUserIdAndCurrencyType(userId, 现金);
		model.addAttribute("amount", account.getAmount());
		return "ucenter/currency/money";
	}
	
	@RequestMapping(value = "/log", method = RequestMethod.GET)
	public String log(Principal principal, Model model) {
		AccountLogQueryModel accountLogQueryModel = new AccountLogQueryModel();
		accountLogQueryModel.setCurrencyTypeEQ(现金);
		accountLogQueryModel.setUserIdEQ(principal.getUserId());
		accountLogQueryModel.setPageSize(10);
		accountLogQueryModel.setPageNumber(0);
		Page<AccountLog> page  = accountLogService.findPage(accountLogQueryModel);
		
		model.addAttribute("page", PageBuilder.copyAndConvert(page, accountLogComponent::buildSimpleVo));
		model.addAttribute("timeLT", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		return "ucenter/currency/moneyLog";
	}
	
	@RequestMapping(value = "/log", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> log(Principal principal, Model model, Date timeLT, @RequestParam(required = true) Integer pageNumber) {
		if (timeLT == null) {
			timeLT = new Date();
		}
		AccountLogQueryModel accountLogQueryModel = new AccountLogQueryModel();
		accountLogQueryModel.setCurrencyTypeEQ(现金);
		accountLogQueryModel.setTransTimeLT(timeLT);
		accountLogQueryModel.setUserIdEQ(principal.getUserId());
		accountLogQueryModel.setPageSize(10);
		accountLogQueryModel.setPageNumber(pageNumber);
		Page<AccountLog> page  = accountLogService.findPage(accountLogQueryModel);
		Map<String, Object> map = new HashMap<>();
		map.put("page", PageBuilder.copyAndConvert(page, accountLogComponent::buildSimpleVo));
		map.put("timeLT", DateFormatUtils.format(timeLT, "yyyy-MM-dd HH:mm:ss"));
		return ResultBuilder.result(map);
	}
	
	@RequestMapping(value = "/withdraw", method = RequestMethod.GET)
	public String withdraw(Principal principal, Model model) {
		Account account = accountService.findByUserIdAndCurrencyType(principal.getUserId(), 现金);
		model.addAttribute("withdrawFeeRate", config.getWithdrawFeeRate(代理, 现金));
		model.addAttribute("amount", account.getAmount());
		model.addAttribute("date", DateFormatUtils.format(DateUtils.addMinutes(new Date(), 30), "HH:mm"));
		return "ucenter/currency/moneyWithdraw";
	}
	
	@RequestMapping(value = "/withdraw", method = RequestMethod.POST)
	public String withdraw(Principal principal, Model model, BigDecimal amount, RedirectAttributes redirectAttributes) {
		try {
			BankCardQueryModel bankCardQueryModel = new BankCardQueryModel();
			bankCardQueryModel.setUserIdEQ(principal.getUserId());
			Long count = bankCardService.count(bankCardQueryModel);
			if(count.compareTo(0L) <= 0){
				redirectAttributes.addFlashAttribute(MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("您尚未绑定银行卡，请先绑定银行卡"));
				return "redirect:/u/money/withdraw";
			}
			Withdraw withdraw = withdrawService.create(principal.getUserId(), null, 现金, amount);
			model.addAttribute("withdraw", withdraw);
			return "ucenter/currency/moneyWithdrawSuccess";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(现金.getAlias() + "提现申请失败, 原因" + e.getMessage()));
			return "redirect:/u/money/withdraw";
		}
	}
	
}
