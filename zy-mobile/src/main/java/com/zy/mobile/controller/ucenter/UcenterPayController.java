package com.zy.mobile.controller.ucenter;

import com.zy.common.exception.BizException;
import com.zy.common.extend.BigDecimalBinder;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.support.shengpay.ShengPayClient;
import com.zy.common.support.shengpay.ShengPayMobileClient;
import com.zy.entity.act.Activity;
import com.zy.entity.act.ActivityApply;
import com.zy.entity.fnc.*;
import com.zy.entity.fnc.Deposit.DepositStatus;
import com.zy.entity.fnc.Payment.PaymentType;
import com.zy.entity.mal.Order;
import com.zy.entity.mal.Order.OrderStatus;
import com.zy.entity.usr.User;
import com.zy.model.BizCode;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.model.query.DepositQueryModel;
import com.zy.model.query.PaymentQueryModel;
import com.zy.service.*;
import com.zy.util.GcUtils;
import io.gd.generator.api.query.Direction;
import org.apache.commons.lang3.StringUtils;
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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.zy.common.util.ValidateUtils.*;

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

	@Autowired
	private UserService userService;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private ActivityApplyService activityApplyService;

	@Autowired
	private ShengPayClient shengPayClient;

	@Autowired
	private ShengPayMobileClient shengPayMobileClient;

	@RequestMapping(method = RequestMethod.POST)
	public String depositPay(@BigDecimalBinder BigDecimal money, @RequestParam PayType payType, Model model,
			Principal principal) {

		final BigDecimal zero = new BigDecimal("0.00");
		String title = "积分余额充值";
		Long userId = principal.getUserId();
		validate(money, v -> v.compareTo(zero) > 0, "money must be more than 0.00");

		DepositQueryModel depositQueryModel = new DepositQueryModel();
		depositQueryModel.setUserIdEQ(principal.getUserId());
		depositQueryModel.setDepositStatusIN(new DepositStatus[] {DepositStatus.待充值});
		depositQueryModel.setOrderBy("createdTime");
		depositQueryModel.setDirection(Direction.DESC);
		List<Deposit> deposits = depositService.findAll(depositQueryModel);
		Deposit deposit = deposits.stream().filter(v -> v.getPayType() == payType)
				.filter(v -> (v.getDepositStatus() == DepositStatus.待充值))
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

		if (payType == PayType.银行汇款) {
			model.addAttribute("title", deposit.getTitle());
			model.addAttribute("sn", deposit.getSn());
			model.addAttribute("amount", deposit.getAmount1());
			model.addAttribute("refId", deposit.getId());
			model.addAttribute("offlineImage", deposit.getOfflineImage());
			model.addAttribute("offlineMemo", deposit.getOfflineMemo());
			return "ucenter/account/moneyDepositOffline";
		} else if(payType == PayType.盛付通) {
			String payUrl = shengPayClient.getPayCreateUrl(deposit.getTitle(), deposit.getSn(), deposit.getAmount1(), new Date(), GcUtils.getHost(), Constants.SHENGPAY_RETURN, Constants.SHENGPAY_NOTIFY, Constants.URL_MOBILE);
			return "redirect:" + payUrl;
		} else {
			return null;
		}
	}

	@RequestMapping(path = "/deposit", method = RequestMethod.GET)
	public String depositPay(Model model, Principal principal) {
		User user = userService.findOne(principal.getUserId());
		User.UserRank userRank = user.getUserRank();
		boolean useShengPay = false;
		if(userRank == User.UserRank.V3 || userRank == User.UserRank.V4) {
			useShengPay = true;
		}
		model.addAttribute("useShengPay", useShengPay);
		return "ucenter/account/moneyDeposit";
	}

	@RequestMapping(path = "/order/{orderId}", method = RequestMethod.GET)
	public String orderPay(@PathVariable Long orderId, @RequestParam(required = true) PayType payType, Model model,
			Principal principal) {
		validate(payType, NOT_NULL, "order payType" + payType + " is null");
		Order order = orderService.findOne(orderId);
		validate(order, NOT_NULL, "order id" + orderId + " not found");
		if(!order.getUserId().equals(principal.getUserId())){
			throw new BizException(BizCode.ERROR, "非自己的订单不能操作");
		}
		if(order.getOrderStatus() != OrderStatus.待支付){
			return "redirect:/u/order/" + orderId;
		}


		model.addAttribute("title", order.getTitle());
		model.addAttribute("sn", order.getSn());
		model.addAttribute("amount", order.getAmount());
		model.addAttribute("payType", payType);
		model.addAttribute("orderId", orderId);
		Account account = accountService.findByUserIdAndCurrencyType(principal.getUserId(), CurrencyType.现金);
		model.addAttribute("amount1", account.getAmount());
		Account account1 = accountService.findByUserIdAndCurrencyType(principal.getUserId(), CurrencyType.积分);
		model.addAttribute("amount2", account1.getAmount());
		BigDecimal balance = account.getAmount().add(account1.getAmount());
		model.addAttribute("balance", balance);
		return "ucenter/pay/payBalance";

	}

	@RequestMapping(path = "/payment", method = RequestMethod.POST)
	public String paymentPay(Long orderId, boolean useCurrency2, BigDecimal amount2, RedirectAttributes redirectAttributes,
			Principal principal) {
		validate(orderId, NOT_NULL, "order id " + orderId + " is null");
		Order order = orderService.findOne(orderId);
		validate(order, NOT_NULL, "order id" + orderId + " not found");
		if(!order.getUserId().equals(principal.getUserId())){
			throw new BizException(BizCode.ERROR, "非自己的订单不能操作");
		}
		if(order.getOrderStatus() != OrderStatus.待支付){
			return "redirect:/u/order/" + orderId;
		}

		if(useCurrency2) {
			if(amount2 == null) {
				throw new BizException(BizCode.INSUFFICIENT_BALANCE, "使用积分余额支付, 积分余额为空");
			}
			Account account = accountService.findByUserIdAndCurrencyType(principal.getUserId(), CurrencyType.积分);
			if(account.getAmount().compareTo(amount2) < 0) {
				throw new BizException(BizCode.INSUFFICIENT_BALANCE, "支付失败, " + CurrencyType.积分.getAlias() + "余额不足");
			}
		} else {
			amount2 = null;
		}
		Payment payment =createPayment(order, amount2);

		paymentService.balancePay(payment.getId(), true);
		redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("积分余额支付成功"));
		return "redirect:/u/order/" + orderId;
	}

	private Payment createPayment(Order order, BigDecimal amount2) {
		PayType payType = PayType.余额;
		BigDecimal orderAmount = order.getAmount();
		BigDecimal amount1 = orderAmount;
		if(amount2 != null) {
			amount1 =  orderAmount.subtract(amount2);
		}
//		List<Payment> payments = paymentService.findAll(PaymentQueryModel.builder().refIdEQ(order.getId()).paymentTypeEQ(PaymentType.订单支付).build());
//		Payment payment = payments.stream().filter(v -> v.getPayType() == payType)
//				.filter(v -> (v.getPaymentStatus() == PaymentStatus.待支付 || v.getPaymentStatus() == PaymentStatus.待确认))
//				.filter(v -> v.getExpiredTime() == null || v.getExpiredTime().after(new Date()))
//				.filter(v -> v.getAmount1().equals(amount1))
//				.filter(v -> v.getAmount2().equals(amount2))
//				.filter(v -> v.getCurrencyType2() == null)
//				.findFirst().orElse(null);

		Payment payment = new Payment();
		payment.setExpiredTime(DateUtils.addMinutes(new Date(), Constants.SETTING_PAYMENT_EXPIRE_IN_MINUTES));
		payment.setCurrencyType1(CurrencyType.现金);
		payment.setAmount1(amount1);
		if(amount2 != null) {
			payment.setCurrencyType2(CurrencyType.积分);
			payment.setAmount2(amount2);
		}
		payment.setPaymentType(PaymentType.订单支付);
		payment.setRefId(order.getId());
		payment.setUserId(order.getUserId());
		payment.setTitle(order.getTitle());
		payment.setPayType(payType);
		payment = paymentService.create(payment);
		return payment;
	}
	
	@RequestMapping(path = "/deposit", method = RequestMethod.POST)
	public String depositPay(Long depositId, String offlineImage, String offlineMemo, RedirectAttributes redirectAttributes,
			Principal principal) {
		validate(depositId, NOT_NULL, "deposit id " + depositId + " is null");
		Deposit deposit = depositService.findOne(depositId);
		validate(deposit, NOT_NULL, "deposit id" + depositId + " not found");
		if(!deposit.getUserId().equals(principal.getUserId())){
			throw new BizException(BizCode.ERROR, "非自己的充值单不能操作");
		}
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

	@RequestMapping(path = "/activityApply/{activityId}", method = RequestMethod.GET)
	public String orderPay(@PathVariable Long activityId, Model model, Principal principal) {

		Activity activity = activityService.findOne(activityId);
		validate(activity, NOT_NULL, "activity id " + activityId + " not found");

		ActivityApply byActivityIdAndUserId = activityApplyService.findByActivityIdAndUserId(activityId, principal.getUserId());
		validate(byActivityIdAndUserId, NOT_NULL, "activity apply id" + activityId + " not found");
		if(!byActivityIdAndUserId.getUserId().equals(principal.getUserId())){
			throw new BizException(BizCode.ERROR, "非自己的订单不能操作");
		}
		if(byActivityIdAndUserId.getActivityApplyStatus() != ActivityApply.ActivityApplyStatus.已报名){
			return "redirect:/activity/" + activityId;
		}

		model.addAttribute("title", activity.getTitle());
		model.addAttribute("activityId", activityId);
		model.addAttribute("amount", byActivityIdAndUserId.getAmount());
		return "ucenter/pay/activityPay";
	}

	@RequestMapping(path = "/activityApply/payment", method = RequestMethod.POST)
	public String paymentPay(@RequestParam Long activityId, String payerPhone, RedirectAttributes redirectAttributes,
	                         Principal principal) {

		Activity activity = activityService.findOne(activityId);
		validate(activity, NOT_NULL, "activity id " + activityId + " not found");

		ActivityApply activityApply = activityApplyService.findByActivityIdAndUserId(activityId, principal.getUserId());
		validate(activityApply, NOT_NULL, "activity apply id" + activityId + " not found");
		Long userId = activityApply.getUserId();
		if(!userId.equals(principal.getUserId())){
			throw new BizException(BizCode.ERROR, "非自己的订单不能操作");
		}
		if(activityApply.getActivityApplyStatus() != ActivityApply.ActivityApplyStatus.已报名){
			return "redirect:/activity/" + activityId;
		}

		List<Payment> payments = paymentService.findAll(PaymentQueryModel.builder().refIdEQ(activityApply.getId()).paymentTypeEQ(PaymentType.活动报名).build());
		Payment payment = payments.stream()
				.filter(v -> (v.getPaymentStatus() == Payment.PaymentStatus.待支付 || v.getPaymentStatus() == Payment.PaymentStatus.待确认))
				.filter(v -> v.getExpiredTime() == null || v.getExpiredTime().after(new Date()))
				.filter(v -> v.getAmount1().equals(activityApply.getAmount()))
				.filter(v -> v.getCurrencyType1() == CurrencyType.人民币)
				.filter(v -> v.getAmount2() == null)
				.filter(v -> v.getCurrencyType2() == null)
				.findFirst()
				.orElse(null);

		if (payment == null) {
			payment = new Payment();
			payment.setExpiredTime(DateUtils.addMinutes(new Date(), Constants.SETTING_PAYMENT_EXPIRE_IN_MINUTES));
			payment.setAmount1(activityApply.getAmount());
			payment.setCurrencyType1(CurrencyType.人民币);
			payment.setPaymentType(PaymentType.活动报名);
			payment.setRefId(activityApply.getId());
			payment.setUserId(userId);
			payment.setTitle(activity.getTitle());
			payment.setPayType(PayType.盛付通);
			paymentService.create(payment);
		}

		User user = userService.findOne(userId);
		String registerIp = StringUtils.isBlank(user.getRegisterIp())? "127.0.0.1" : user.getRegisterIp();

		String payUrl = shengPayMobileClient.getPayCreateUrl(new Date(), userId, user.getRegisterTime(), registerIp, "0"
				, user.getNickname(), user.getPhone(), activityApply.getId(), activity.getTitle(), activity.getTitle()
				, activityApply.getAmount(), Constants.SHENGPAY_RETURN_MOBILE, Constants.SHENGPAY_NOTIFY_MOBILE, "127.0.0.1");
		logger.error(payUrl);
		return "redirect:" + payUrl;
	}
}
