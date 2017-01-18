package com.zy.mobile.controller.ucenter;

import com.zy.common.exception.UnauthorizedException;
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
import static com.zy.entity.fnc.CurrencyType.积分;

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
		Account account1 = accountService.findByUserIdAndCurrencyType(userId, 现金);
		model.addAttribute("amount1", account1.getAmount());
		Account account2 = accountService.findByUserIdAndCurrencyType(userId, 积分);
		model.addAttribute("amount2", account2.getAmount());

		BankCardQueryModel bankCardQueryModel = new BankCardQueryModel();
		bankCardQueryModel.setUserIdEQ(principal.getUserId());
		bankCardQueryModel.setIsDeletedEQ(false);
		Long bankCardCount = bankCardService.count(bankCardQueryModel);
		model.addAttribute("bankCardCount", bankCardCount);

		TransferQueryModel transferQueryModel = new TransferQueryModel();
		transferQueryModel.setFromUserIdEQ(principal.getUserId());
		transferQueryModel.setTransferStatusEQ(Transfer.TransferStatus.待转账);
		Long transferOutCount = transferService.count(transferQueryModel);
		model.addAttribute("transferOutCount", transferOutCount);

		transferQueryModel = new TransferQueryModel();
		transferQueryModel.setToUserIdEQ(principal.getUserId());
		transferQueryModel.setTransferStatusEQ(Transfer.TransferStatus.待转账);
		Long transferInCount = transferService.count(transferQueryModel);
		model.addAttribute("transferInCount", transferInCount);

		return "ucenter/account/account";
	}

	@RequestMapping(value = "/profit", method = RequestMethod.GET)
	public String profit(@RequestParam Profit.ProfitType type, Principal principal, Model model) {
		model.addAttribute("type", type);
		return "ucenter/account/profit";
	}

	@RequestMapping(value = "/profit", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> profit(@RequestParam Profit.ProfitType type, Principal principal, Model model, Date timeLT, @RequestParam(required = true) Integer pageNumber) {
		if (timeLT == null) {
			timeLT = new Date();
		}
		ProfitQueryModel profitQueryModel = new ProfitQueryModel();
		profitQueryModel.setProfitTypeEQ(type);
		profitQueryModel.setUserIdEQ(principal.getUserId());
		profitQueryModel.setCreatedTimeLT(timeLT);
		profitQueryModel.setPageNumber(pageNumber);
		profitQueryModel.setPageSize(pageSize);

		Page<Profit> page = profitService.findPage(profitQueryModel);
		Map<String, Object> map = new HashMap<>();
		map.put("page", PageBuilder.copyAndConvert(page, profitComponent::buildListVo));
		map.put("timeLT", DateFormatUtils.format(timeLT, "yyyy-MM-dd HH:mm:ss"));
		model.addAttribute("type", type);
		return ResultBuilder.result(map);
	}

	@RequestMapping(value = "/transferOut", method = RequestMethod.GET)
	public String transferOut(Transfer.TransferType type, String status, Model model) {
		model.addAttribute("type", type);
		model.addAttribute("status", status);
		return "ucenter/account/transferOut";
	}

	@RequestMapping(value = "/transferOut", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> transferOut(Transfer.TransferType type, String status, Principal principal, Model model, Date timeLT, @RequestParam(required = true) Integer pageNumber) {
		if (timeLT == null) {
			timeLT = new Date();
		}
		TransferQueryModel transferQueryModel = new TransferQueryModel();
		transferQueryModel.setFromUserIdEQ(principal.getUserId());
		transferQueryModel.setCreatedTimeLT(timeLT);
		transferQueryModel.setPageSize(pageSize);
		transferQueryModel.setPageNumber(pageNumber);

		if (type != null) {
			transferQueryModel.setTransferTypeEQ(type);
		}
		if ("0".equals(status)) {
			transferQueryModel.setTransferStatusEQ(Transfer.TransferStatus.待转账);
		} else if ("1".equals(status)) {
			transferQueryModel.setTransferStatusIN(new Transfer.TransferStatus[]{Transfer.TransferStatus.已转账, Transfer.TransferStatus.已线下转账, Transfer.TransferStatus.已取消});
		}
		Page<Transfer> page = transferService.findPage(transferQueryModel);
		Map<String, Object> map = new HashMap<>();
		map.put("page", PageBuilder.copyAndConvert(page, transferComponent::buildListVo));
		map.put("timeLT", DateFormatUtils.format(timeLT, "yyyy-MM-dd HH:mm:ss"));
		model.addAttribute("type", type);
		model.addAttribute("status", status);
		return ResultBuilder.result(map);
	}

	@RequestMapping(value = "/transferIn", method = RequestMethod.GET)
	public String transferIn(Transfer.TransferType type, String status, Model model) {
		model.addAttribute("type", type);
		model.addAttribute("status", status);
		return "ucenter/account/transferIn";
	}

	@RequestMapping(value = "/transferIn", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> transferIn(Transfer.TransferType type, String status, Principal principal, Model model, Date timeLT, @RequestParam(required = true) Integer pageNumber) {
		if (timeLT == null) {
			timeLT = new Date();
		}
		TransferQueryModel transferQueryModel = new TransferQueryModel();
		transferQueryModel.setToUserIdEQ(principal.getUserId());
		transferQueryModel.setCreatedTimeLT(timeLT);
		transferQueryModel.setPageNumber(pageNumber);
		transferQueryModel.setPageSize(pageSize);

		if (type != null) {
			transferQueryModel.setTransferTypeEQ(type);
		}
		if ("0".equals(status)) {
			transferQueryModel.setTransferStatusEQ(Transfer.TransferStatus.待转账);
		} else if ("1".equals(status)) {
			transferQueryModel.setTransferStatusIN(new Transfer.TransferStatus[]{Transfer.TransferStatus.已转账, Transfer.TransferStatus.已线下转账, Transfer.TransferStatus.已取消});
		}

		Page<Transfer> page = transferService.findPage(transferQueryModel);
		Map<String, Object> map = new HashMap<>();
		map.put("page", PageBuilder.copyAndConvert(page, transferComponent::buildListVo));
		map.put("timeLT", DateFormatUtils.format(timeLT, "yyyy-MM-dd HH:mm:ss"));
		model.addAttribute("type", type);
		model.addAttribute("status", status);
		return ResultBuilder.result(map);
	}

	@RequestMapping(value = "/transfer", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> transfer(Long id, String remark, Principal principal) {
		Transfer transfer = transferService.findOne(id);
		if(!transfer.getFromUserId().equals(principal.getUserId())) {
			throw new UnauthorizedException();
		}
		try {
			transferService.offlineTransfer(id, remark);
			return ResultBuilder.ok("已转账成功");
		} catch (Exception e) {
			return ResultBuilder.error(e.getMessage());
		}
	}


	@RequestMapping(value = "/withdraw", method = RequestMethod.GET)
	public String withdraw(Principal principal, Model model) {
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
		Page<Withdraw> page = withdrawService.findPage(withdrawQueryModel);

		Map<String, Object> map = new HashMap<>();
		map.put("page", PageBuilder.copyAndConvert(page, withdrawComponent::buildListVo));
		map.put("timeLT", DateFormatUtils.format(timeLT, "yyyy-MM-dd HH:mm:ss"));
		return ResultBuilder.result(map);
	}

	@RequestMapping(value = "/deposit", method = RequestMethod.GET)
	public String deposit(Principal principal, Model model) {
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
		Page<Deposit> page = depositService.findPage(depositQueryModel);

		Map<String, Object> map = new HashMap<>();
		map.put("page", PageBuilder.copyAndConvert(page, depositComponent::buildListVo));
		map.put("timeLT", DateFormatUtils.format(timeLT, "yyyy-MM-dd HH:mm:ss"));
		return ResultBuilder.result(map);
	}

}
