package com.zy.mobile.controller.ucenter;

import com.zy.common.exception.BizException;
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
import com.zy.entity.usr.User;
import com.zy.model.BizCode;
import com.zy.model.Constants;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/u/account")
@Controller
public class UcenterAccountController {

	Logger logger = LoggerFactory.getLogger(UcenterAccountController.class);

	private final int pageSize = 20;

	@Autowired
	private AccountService accountService;

	@Autowired
	private UserService userService;

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
		List<Account> accounts = accountService.findByUserId(userId);
		accounts.stream().forEach(v -> {
			switch (v.getCurrencyType()) {
				case 现金:
					model.addAttribute("amount1", v.getAmount());
					break;
				case 积分:
					model.addAttribute("amount2", v.getAmount());
					break;
				case 货币期权:
					model.addAttribute("amount3", v.getAmount());
					break;
				case 货币股份:
					model.addAttribute("amount4", v.getAmount());
					break;
				default:
					break;
			}
		});


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
	public Result<?> transferAjax(Long id, String remark, Principal principal) {
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

	@RequestMapping(value = "/transfer/create", method = RequestMethod.GET)
	public String transferGet(Principal principal , Model model) {

		User user = userService.findOne(principal.getUserId());
		if (user.getUserRank() != User.UserRank.V4) {
			throw new BizException(BizCode.ERROR, "只有特技服务商才可以转账");
		}

		Account account = accountService.findByUserIdAndCurrencyType(principal.getUserId(), CurrencyType.现金);
		model.addAttribute("amount", account.getAmount());
		return "ucenter/account/transfer";
	}

	@RequestMapping(value = "/transfer/create", method = RequestMethod.POST)
	public String transferPost(Principal principal, @RequestParam String toUserPhone, @RequestParam BigDecimal amount
			, String remark, RedirectAttributes redirectAttributes) {

		User user = userService.findOne(principal.getUserId());
		if (user.getUserRank() != User.UserRank.V4) {
			throw new BizException(BizCode.ERROR, "只有特技服务商才可以转账");
		}

		User toUser = userService.findByPhone(toUserPhone);
		if (toUser == null) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("用户手机号不存在"));
			return "redirect:/u/account/transfer/create";
		}
		if (toUser.getUserRank() != User.UserRank.V4) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("转入用户必须是特级服务商"));
			return "redirect:/u/account/transfer/create";
		}

		try {
			transferService.createAndTransfer(principal.getUserId(), toUser.getId(), amount, remark);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
			return "redirect:/u/account/transfer/create";
		}
		redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("转账成功"));
		return "redirect:/u/account/transferOut?status=1";
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
