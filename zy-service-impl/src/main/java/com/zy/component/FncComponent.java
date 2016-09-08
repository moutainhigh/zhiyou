package com.zy.component;

import com.zy.Config;
import com.zy.ServiceUtils;
import com.zy.common.exception.ConcurrentException;
import com.zy.common.exception.ValidationException;
import com.zy.entity.fnc.*;
import com.zy.entity.fnc.AccountLog.InOut;
import com.zy.entity.usr.User;
import com.zy.mapper.*;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;
import static com.zy.entity.fnc.AccountLog.AccountLogType.*;
import static com.zy.entity.fnc.AccountLog.InOut.支出;
import static com.zy.entity.fnc.AccountLog.InOut.收入;

@Component
@Validated
public class FncComponent {

	@Autowired
	private AccountMapper accountMapper;

	@Autowired
	private AccountLogMapper accountLogMapper;

	@Autowired
	private ProfitMapper profitMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private PaymentMapper paymentMapper;

	@Autowired
	private Config config;

	public void recordAccountLog(@NotNull Long userId, @NotBlank String title, @NotNull CurrencyType currencyType,
	                             @NotNull @DecimalMin("0.01") BigDecimal transAmount,  @NotNull InOut inOut,  @NotNull Object ref,  @NotNull Long refUserId) {

		validate(refUserId, v -> !v.equals(userId), "ref user id must not be same with user id");

		User user = userMapper.findOne(userId);
		validate(user, NOT_NULL, "user id " + userId + " is not found");
		Account account = accountMapper.findByUserIdAndCurrencyType(userId, currencyType);
		final BigDecimal zero = new BigDecimal("0.00");
		if (transAmount.compareTo(zero) <= 0) {
			throw new ValidationException(currencyType + " trans amount " + transAmount + " is wrong");
		}
		if (account.getCurrencyType() != currencyType) {
			throw new ValidationException(currencyType + " does not match account currency type " + account.getCurrencyType());
		}

		User refUser = userMapper.findOne(userId);
		validate(refUser, NOT_NULL, "ref user id " + refUserId + " is not found");

		AccountLog accountLog = new AccountLog();
		accountLog.setTitle(title);
		accountLog.setTransAmount(transAmount);
		accountLog.setTransTime(new Date());
		accountLog.setCurrencyType(currencyType);
		accountLog.setUserId(account.getUserId());
		accountLog.setInOut(inOut);
		accountLog.setRefId(refUserId);

		if (ref instanceof Payment) {
			Payment payment = (Payment) ref;
			accountLog.setRefSn(payment.getSn());
			accountLog.setRefId(payment.getId());
			accountLog.setAccountLogType(支付单);
		} else if (ref instanceof Profit) {
			Profit profit = (Profit) ref;
			accountLog.setRefSn(profit.getSn());
			accountLog.setRefId(profit.getId());
			accountLog.setAccountLogType(收益单);

		} else if (ref instanceof Withdraw) {
			Withdraw withdraw = (Withdraw) ref;
			accountLog.setRefSn(withdraw.getSn());
			accountLog.setRefId(withdraw.getId());
			accountLog.setAccountLogType(提现单);

		} else if (ref instanceof Deposit) {
			Deposit deposit = (Deposit) ref;
			accountLog.setRefSn(deposit.getSn());
			accountLog.setRefId(deposit.getId());
			accountLog.setAccountLogType(充值单);
		} else {
			throw new ValidationException("error ref class " + ref.getClass().getName());
		}

		if (user.getUserType() == User.UserType.平台) {
			accountLog.setIsAcknowledged(false);
			validate(accountLog);
			accountLogMapper.insert(accountLog);
		} else {
			accountLog.setIsAcknowledged(true);
			BigDecimal beforeAmount = account.getAmount();
			BigDecimal afterAmount = null;
			if (inOut == 收入) {
				afterAmount = beforeAmount.add(transAmount);
			} else {
				afterAmount = beforeAmount.subtract(transAmount);
			}

			accountLog.setAfterAmount(afterAmount);
			accountLog.setBeforeAmount(beforeAmount);
			validate(accountLog);
			accountLogMapper.insert(accountLog);
			account.setAmount(afterAmount);
			if (accountMapper.update(account) == 0) {
				throw new ConcurrentException();
			}
		}
	}

	public Profit createProfit(String bizName, CurrencyType currencyType, String title, Long userId, BigDecimal amount, String remark) {
		final BigDecimal zero = new BigDecimal("0.00");
		if (amount.compareTo(zero) <= 0) {
			throw new ValidationException("profit amount " + amount + " is wrong");
		}
		Profit profit = new Profit();
		profit.setBizName(bizName);
		profit.setTitle(title);
		profit.setUserId(userId);
		profit.setAmount(amount);
		profit.setSn(ServiceUtils.generateProfitSn());
		profit.setCurrencyType(currencyType);
		profit.setCreatedTime(new Date());
		profit.setRemark(remark);
		validate(profit);
		profitMapper.insert(profit);
		return profit;
	}

	public Profit grantProfit(Long userId, String bizName, String title, @NotNull CurrencyType currencyType, BigDecimal amount, String remark) {
		Profit profit = this.createProfit(bizName, currencyType, title, userId, amount, remark);
		Long sysUserId = config.getSysUserId();
		this.recordAccountLog(sysUserId, title, currencyType, amount, 支出, profit, userId);
		this.recordAccountLog(userId, title, currencyType, amount, 收入, profit, sysUserId);
		return profit;
	}

	@Deprecated
	public void refundPayment(@NotNull Long paymentId, @NotNull CurrencyType refundCurrencyType1, @NotNull @DecimalMin("0.00") BigDecimal refund1,
			CurrencyType refundCurrencyType2, @DecimalMin("0.00") BigDecimal refund2) {

		/*Payment payment = paymentMapper.findOne(paymentId);
		validate(payment, NOT_NULL, "payment id " + paymentId + " is not found");

		PaymentStatus paymentStatus = payment.getPaymentStatus();
		if (paymentStatus == PaymentStatus.已退款) {
			return; // 幂等处理
		} else if (paymentStatus != PaymentStatus.已支付) {
			throw new BizException(BizCode.ERROR, "只有已支付支付订单才能退款");
		}

		CurrencyType currencyType1 = payment.getCurrencyType1();
		validate(refundCurrencyType1, v -> v == currencyType1, "currency type 1 is not the same");
		BigDecimal amount1 = payment.getAmount1();
		if (refund1.compareTo(amount1) > 0) {
			throw new BizException(BizCode.ERROR, "退款金额1超过支付金额");
		}

		String title = payment.getTitle() + currencyType1.getAlias() + "退款";
		Long sysUserId = config.getSysUserId();
		Long userId = payment.getUserId();
		recordAccountLog(sysUserId, title, currencyType1, refund1, InOut.支出, payment, userId);
		recordAccountLog(userId, title, currencyType1, refund1, InOut.收入, payment, sysUserId);

		CurrencyType currencyType2 = payment.getCurrencyType2();
		validate(refundCurrencyType2, v -> v == currencyType2, "currency type 2 is not the same");
		if (currencyType2 != null) {
			validate(refund2, NOT_NULL, "refund 2 must not be null");
			BigDecimal amount2 = payment.getAmount2();
			if (refund2.compareTo(amount2) > 0) {
				throw new BizException(BizCode.ERROR, "退款金额2超过支付金额");
			}
			validate(refundCurrencyType2, v -> v == currencyType2, "currency type 2 is not the same");

			title = payment.getTitle() + currencyType2.getAlias() + "退款";
			recordAccountLog(sysUserId, title, currencyType2, refund2, InOut.支出, payment, userId);
			recordAccountLog(userId, title, currencyType2, refund2, InOut.收入, payment, sysUserId);
		} else {
			validate(refund2, NULL, "refund 2 must be null");
		}
		payment.setRefundedTime(new Date());
		payment.setRefund1(refund1);
		payment.setRefund2(refund2);
		payment.setPaymentStatus(PaymentStatus.已退款);

		if (paymentMapper.update(payment) == 0) {
			throw new ConcurrentException();
		}*/
	}

}
