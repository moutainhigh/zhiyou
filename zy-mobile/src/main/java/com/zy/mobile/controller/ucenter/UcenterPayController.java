package com.zy.mobile.controller.ucenter;

import static com.zy.common.util.ValidateUtils.NOT_BLANK;
import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.exception.BizException;
import com.zy.common.extend.BigDecimalBinder;
import com.zy.common.model.result.ResultBuilder;
import com.zy.entity.fnc.Account;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.fnc.Deposit;
import com.zy.entity.fnc.Deposit.DepositStatus;
import com.zy.entity.fnc.PayType;
import com.zy.entity.fnc.Payment;
import com.zy.entity.fnc.Payment.PaymentStatus;
import com.zy.entity.fnc.Payment.PaymentType;
import com.zy.entity.mal.Order;
import com.zy.model.BizCode;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.model.query.DepositQueryModel;
import com.zy.model.query.PaymentQueryModel;
import com.zy.service.AccountService;
import com.zy.service.DepositService;
import com.zy.service.OrderService;
import com.zy.service.PaymentService;

import io.gd.generator.api.query.Direction;

@Controller
@RequestMapping("/u/pay")
public class UcenterPayController {

	final Logger logger = LoggerFactory.getLogger(UcenterPayController.class);

	@Autowired
	private DepositService depositService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private AccountService accountService;

	@RequestMapping(method = RequestMethod.POST)
	public String depositPay(@BigDecimalBinder BigDecimal money, @RequestParam PayType payType, Model model,
			Principal principal) {

		if (payType != PayType.银行汇款) {
			throw new BizException(BizCode.ERROR, "暂只支持银行汇款");
		}

		final BigDecimal zero = new BigDecimal("0.00");
		String title = "余额充值";
		Long userId = principal.getUserId();
		validate(money, v -> v.compareTo(zero) > 0, "money must be more than 0.00");

		DepositQueryModel depositQueryModel = new DepositQueryModel();
		depositQueryModel.setUserIdEQ(principal.getUserId());
		depositQueryModel.setDepositStatusIN(new DepositStatus[] {DepositStatus.待充值, DepositStatus.待确认});
		depositQueryModel.setOrderBy("createdTime");
		depositQueryModel.setDirection(Direction.DESC);
		List<Deposit> deposits = depositService.findAll(depositQueryModel);
		Deposit deposit = deposits.stream().filter(v -> v.getPayType() == payType)
				.filter(v -> (v.getDepositStatus() == DepositStatus.待充值 || v.getDepositStatus() == DepositStatus.待确认))
				.filter(v -> v.getExpiredTime() == null || v.getExpiredTime().after(new Date()))
				.filter(v -> v.getAmount1().equals(money) && v.getCurrencyType1() == CurrencyType.现金
						&& v.getCurrencyType2() == null)
				.findFirst().orElse(null);

		if (deposit == null) {
			deposit = new Deposit();
			deposit.setPayType(payType);
			deposit.setCurrencyType1(CurrencyType.现金);
			deposit.setTitle(title);
			deposit.setAmount1(money);
			deposit.setUserId(userId);
			deposit.setExpiredTime(DateUtils.addMinutes(new Date(), Constants.SETTING_DEPOSIT_OFFLINE_EXPIRE_IN_MINUTES));
			deposit = depositService.create(deposit);
		}

		model.addAttribute("title", deposit.getTitle());
		model.addAttribute("sn", deposit.getSn());
		model.addAttribute("amount", deposit.getAmount1());
		model.addAttribute("refId", deposit.getId());
		if (payType == PayType.银行汇款) {
			model.addAttribute("offlineImage", deposit.getOfflineImage());
			model.addAttribute("offlineMemo", deposit.getOfflineMemo());
			return "ucenter/currency/moneyDepositOffline";
		} else {
			return null;
		}
	}

	@RequestMapping(path = "/deposit", method = RequestMethod.GET)
	public String depositPay(@RequestParam(required = true) PayType payType, Model model, Principal principal) {
		validate(payType, NOT_NULL, "order payType" + payType + " is null");
		if (payType == PayType.银行汇款) {
			return "ucenter/currency/moneyDeposit";
		} else {
			throw new BizException(BizCode.ERROR, "不支持的付款方式");
		}
	}

	@RequestMapping(path = "/order/{orderId}", method = RequestMethod.GET)
	public String orderPay(@PathVariable Long orderId, @RequestParam(required = true) PayType payType, Model model,
			Principal principal) {
		validate(payType, NOT_NULL, "order payType" + payType + " is null");
		Order order = orderService.findOne(orderId);
		validate(order, NOT_NULL, "order id" + orderId + " not found");
		if(!order.getUserId().equals(principal.getUserId())){
			throw new BizException(BizCode.ERROR, "非自己的订单不能支付");
		}
		List<Payment> payments = paymentService.findAll(PaymentQueryModel.builder().refIdEQ(order.getId()).build());
		Payment payment = payments.stream().filter(v -> v.getPayType() == payType)
				.filter(v -> (v.getPaymentStatus() == PaymentStatus.待支付 || v.getPaymentStatus() == PaymentStatus.待确认))
				.filter(v -> v.getExpiredTime() == null || v.getExpiredTime().after(new Date()))
				.filter(v -> v.getRefId() == orderId).filter(v -> v.getAmount1().equals(order.getAmount()))
				.filter(v -> v.getCurrencyType1() == order.getCurrencyType()).filter(v -> v.getAmount2() == null)
				.filter(v -> v.getCurrencyType2() == null).findFirst().orElse(null);

		if (payment == null) {
			payment = new Payment();
			if(payType == PayType.银行汇款){
				payment.setExpiredTime(DateUtils.addMinutes(new Date(), Constants.SETTING_PAYMENT_OFFLINE_EXPIRE_IN_MINUTES));
			} else {
				payment.setExpiredTime(DateUtils.addMinutes(new Date(), Constants.SETTING_PAYMENT_EXPIRE_IN_MINUTES));
			}
			payment.setAmount1(order.getAmount());
			payment.setCurrencyType1(order.getCurrencyType());
			payment.setPaymentType(PaymentType.订单支付);
			payment.setRefId(orderId);
			payment.setUserId(order.getUserId());
			payment.setTitle(order.getTitle());
			payment.setPayType(payType);
			payment = paymentService.create(payment);
		}

		model.addAttribute("title", order.getTitle());
		model.addAttribute("sn", order.getSn());
		model.addAttribute("amount", order.getAmount());
		model.addAttribute("refId", payment.getId());
		if (payType == PayType.银行汇款) {
			model.addAttribute("offlineImage", payment.getOfflineImage());
			model.addAttribute("offlineMemo", payment.getOfflineMemo());
			return "ucenter/pay/payOffline";
		} else if (payment.getPayType() == PayType.余额) {
			Account account = accountService.findByUserIdAndCurrencyType(principal.getUserId(), CurrencyType.现金);
			model.addAttribute("balance", account.getAmount());
			return "ucenter/pay/payBalance";
		} else {
			throw new BizException(BizCode.ERROR, "不支持的付款方式");
		}
	}

	@RequestMapping(path = "/payment", method = RequestMethod.POST)
	public String paymentPay(Long paymentId, String offlineImage, String offlineMemo, RedirectAttributes redirectAttributes,
			Principal principal) {
		validate(paymentId, NOT_NULL, "payment id " + paymentId + " is null");
		Payment payment = paymentService.findOne(paymentId);
		validate(payment, NOT_NULL, "payment id" + paymentId + " not found");
		Long orderId = payment.getRefId();
		if (payment.getPayType() == PayType.余额) {
			paymentService.balancePay(paymentId, true);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("余额支付成功"));
			return "redirect:/u/order/" + orderId;
		} else if (payment.getPayType() == PayType.银行汇款) {
			validate(offlineImage, NOT_BLANK, "payment offlineImage is blank");
			validate(offlineMemo, NOT_BLANK, "payment offlineMemo is blank");
			payment.setOfflineImage(offlineImage);
			payment.setOfflineMemo(offlineMemo);
			paymentService.modifyOffline(paymentId, offlineImage, offlineMemo);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT,
					ResultBuilder.ok("转账汇款信息提交成功，请等待工作人员确认"));
			return "redirect:/u/order/" + orderId;
		} else {
			throw new BizException(BizCode.ERROR, "不支持的付款方式");
		}
	}
	
	@RequestMapping(path = "/deposit", method = RequestMethod.POST)
	public String depositPay(Long depositId, String offlineImage, String offlineMemo, RedirectAttributes redirectAttributes,
			Principal principal) {
		validate(depositId, NOT_NULL, "deposit id " + depositId + " is null");
		Deposit deposit = depositService.findOne(depositId);
		validate(deposit, NOT_NULL, "deposit id" + depositId + " not found");
		if (deposit.getPayType() == PayType.银行汇款) {
			validate(offlineImage, NOT_BLANK, "deposit offlineImage is blank");
			validate(offlineMemo, NOT_BLANK, "deposit offlineMemo is blank");
			deposit.setOfflineImage(offlineImage);
			deposit.setOfflineMemo(offlineMemo);
			depositService.modifyOffline(depositId, offlineImage, offlineMemo);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT,
					ResultBuilder.ok("转账汇款信息提交成功，请等待工作人员确认"));
			return "redirect:/u";
		} else {
			throw new BizException(BizCode.ERROR, "不支持的付款方式");
		}
	}
}
