package com.zy.mobile.controller.ucenter;

import com.zy.Config;
import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
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
import com.zy.service.*;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.zy.entity.fnc.CurrencyType.现金;
import static com.zy.entity.usr.User.UserType.代理;
import static com.zy.model.Constants.MODEL_ATTRIBUTE_RESULT;

@RequestMapping("/u/money")
@Controller
public class UcenterMoneyController {
	
	Logger logger = LoggerFactory.getLogger(UcenterMoneyController.class);
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private UserService userService;
	
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
		Long bankCardCount = bankCardService.count(bankCardQueryModel);
		model.addAttribute("bankCardCount", bankCardCount);
		model.addAttribute("userRank", userService.findOne(userId).getUserRank());
		
		Calendar calendar = Calendar.getInstance();
		Date now = new Date();
		calendar.setTime(now);
		calendar.set(Calendar.DAY_OF_MONTH, 7);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date beginTime = calendar.getTime();
		calendar.set(Calendar.DAY_OF_MONTH, 12);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		Date endTime = calendar.getTime();
		
		boolean withdraw = false;
		if(now.after(beginTime) && now.before(endTime)) {
			withdraw = true;
		}
		model.addAttribute("withdraw", withdraw);
		return "ucenter/account/money";
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
		return "ucenter/account/moneyLog";
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
		return "ucenter/account/moneyWithdraw";
	}
	
	@RequestMapping(value = "/withdraw", method = RequestMethod.POST)
	public String withdraw(Principal principal, Model model, BigDecimal amount,Long bankCardId, RedirectAttributes redirectAttributes) {
		if (true) {
			redirectAttributes.addFlashAttribute(MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("提现暂时关闭, 如有疑问请联系平台客服"));
			return "redirect:/u/money";
		}

		
		Calendar calendar = Calendar.getInstance();
		Date now = new Date();
		calendar.setTime(now);
		calendar.set(Calendar.DAY_OF_MONTH, 7);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date beginTime = calendar.getTime();
		calendar.set(Calendar.DAY_OF_MONTH, 12);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		Date endTime = calendar.getTime();
		
		if(now.before(beginTime) && now.after(endTime)) {
			redirectAttributes.addFlashAttribute(MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("提现操作时间每月7号-15号之间"));
			return "redirect:/u/money";
		}
		
		try {
			Long userId = principal.getUserId();
			Withdraw withdraw = withdrawService.create(userId, bankCardId, 现金, amount);
			model.addAttribute("withdraw", withdraw);
			return "ucenter/account/moneyWithdrawSuccess";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			redirectAttributes.addFlashAttribute(MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(现金.getAlias() + "提现申请失败, 原因" + e.getMessage()));
			return "redirect:/u/money/withdraw";
		}
	}
	
}
