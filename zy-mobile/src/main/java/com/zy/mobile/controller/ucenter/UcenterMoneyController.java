package com.zy.mobile.controller.ucenter;

import static com.zy.entity.fnc.CurrencyType.现金;
import static com.zy.entity.usr.User.UserType.代理;
import static com.zy.model.Constants.MODEL_ATTRIBUTE_RESULT;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.Config;
import com.zy.common.model.result.ResultBuilder;
import com.zy.component.AccountLogComponent;
import com.zy.component.BankCardComponent;
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
	
	Logger logger = LoggerFactory.getLogger(UcenterMoneyController.class);
	
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
	private BankCardComponent bankCardComponent;

	@Autowired
	private Config config;
	
	@RequestMapping(method = RequestMethod.GET)
	public String index(Principal principal, Model model) {
		Long userId = principal.getUserId();
		Account account = accountService.findByUserIdAndCurrencyType(userId, 现金);
		model.addAttribute("amount", account.getAmount());
		BankCardQueryModel bankCardQueryModel = new BankCardQueryModel();
		bankCardQueryModel.setUserIdEQ(principal.getUserId());
		bankCardQueryModel.setIsDeletedEQ(false);
		Long count = bankCardService.count(bankCardQueryModel);
		Boolean isBoundBankCard = true;
		if(count.compareTo(0L) <= 0){
			isBoundBankCard = false;
		}
		model.addAttribute("isBoundBankCard", isBoundBankCard);
		return "ucenter/currency/money";
	}
	
	@RequestMapping(value = "/log", method = RequestMethod.GET)
	public String log(Principal principal, Model model) {
		Date date = new Date();
		Date beginDate = DateUtils.setSeconds(DateUtils.setMinutes(DateUtils.setHours(date, 0), 0), 0);
		AccountLogQueryModel accountLogQueryModel = new AccountLogQueryModel();
		accountLogQueryModel.setCurrencyTypeEQ(现金);
		accountLogQueryModel.setUserIdEQ(principal.getUserId());
		accountLogQueryModel.setTransTimeGTE(beginDate);
		accountLogQueryModel.setTransTimeLT(date);
		List<AccountLog> accountLogs  = accountLogService.findAll(accountLogQueryModel);
		model.addAttribute("accountLogs", accountLogs.stream().map(accountLogComponent::buildAdminVo).collect(Collectors.toList()));
		return "ucenter/currency/moneyLog";
	}
	
	@RequestMapping(value = "/withdraw", method = RequestMethod.GET)
	public String withdraw(Principal principal, Model model, RedirectAttributes redirectAttributes) {
		BankCardQueryModel bankCardQueryModel = new BankCardQueryModel();
		bankCardQueryModel.setUserIdEQ(principal.getUserId());
		bankCardQueryModel.setIsDeletedEQ(false);
		long bankCardCount = bankCardService.count(bankCardQueryModel);
		if(bankCardCount <= 0l){
			redirectAttributes.addFlashAttribute(MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("您尚未绑定银行卡，请先绑定银行卡"));
			return "redirect:/u/money";
		}
		List<BankCard> bankCardList = bankCardService.findByUserId(principal.getUserId());
		BankCard defaultBankCard = bankCardList.stream().filter(v -> v.getIsDefault()).findFirst().orElse(null);
		defaultBankCard = defaultBankCard == null ? bankCardList.get(0) : defaultBankCard;
		
		Account account = accountService.findByUserIdAndCurrencyType(principal.getUserId(), 现金);
		model.addAttribute("withdrawFeeRate", config.getWithdrawFeeRate(代理, 现金));
		model.addAttribute("amount", account.getAmount());
		model.addAttribute("date", DateFormatUtils.format(DateUtils.addMinutes(new Date(), 30), "HH:mm"));
		model.addAttribute("bankCardCount", bankCardCount);
		model.addAttribute("defaultBankCard", bankCardComponent.buildVo(defaultBankCard));
		model.addAttribute("bankCards", bankCardList.stream().map(bankCardComponent::buildVo).collect(Collectors.toList()));
		return "ucenter/currency/moneyWithdraw";
	}
	
	@RequestMapping(value = "/withdraw", method = RequestMethod.POST)
	public String withdraw(Principal principal, Model model, BigDecimal amount,Long bankCardId, RedirectAttributes redirectAttributes) {
		try {
			Withdraw withdraw = withdrawService.create(principal.getUserId(), bankCardId, 现金, amount);
			model.addAttribute("withdraw", withdraw);
			return "ucenter/currency/moneyWithdrawSuccess";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			redirectAttributes.addFlashAttribute(MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(现金.getAlias() + "提现申请失败, 原因" + e.getMessage()));
			return "redirect:/u/money/withdraw";
		}
	}
	
}
