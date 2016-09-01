package com.zy.component;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.NULL;
import static com.zy.common.util.ValidateUtils.validate;
import static com.zy.entity.fnc.AccountLog.AccountLogType.充值单;
import static com.zy.entity.fnc.AccountLog.AccountLogType.提现单;
import static com.zy.entity.fnc.AccountLog.AccountLogType.支付单;
import static com.zy.entity.fnc.AccountLog.AccountLogType.收益单;
import static com.zy.entity.fnc.AccountLog.InOut.支出;
import static com.zy.entity.fnc.AccountLog.InOut.收入;
import static com.zy.entity.fnc.Payment.PaymentStatus.已支付;
import static com.zy.entity.fnc.Payment.PaymentStatus.待支付;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.zy.Config;
import com.zy.ServiceUtils;
import com.zy.common.exception.BizException;
import com.zy.common.exception.ConcurrentException;
import com.zy.common.exception.ValidationException;
import com.zy.entity.fnc.Account;
import com.zy.entity.fnc.AccountLog;
import com.zy.entity.fnc.AccountLog.InOut;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.fnc.Deposit;
import com.zy.entity.fnc.PayType;
import com.zy.entity.fnc.Payment;
import com.zy.entity.fnc.Payment.PaymentStatus;
import com.zy.entity.fnc.Profit;
import com.zy.entity.fnc.Withdraw;
import com.zy.entity.usr.User;
import com.zy.mapper.AccountLogMapper;
import com.zy.mapper.AccountMapper;
import com.zy.mapper.PaymentMapper;
import com.zy.mapper.ProfitMapper;
import com.zy.mapper.UserMapper;
import com.zy.model.BizCode;

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

	public void recordAccountLog(Long userId, String title, CurrencyType currencyType, BigDecimal transAmount, InOut inOut, Object ref) {
		Account account = accountMapper.findByUserIdAndCurrencyType(userId, currencyType);
		final BigDecimal zero = new BigDecimal("0.00");
		if (transAmount.compareTo(zero) <= 0) {
			throw new ValidationException(currencyType + " trans amount " + transAmount + " is wrong");
		}
		if (account.getCurrencyType() != currencyType) {
			throw new ValidationException(currencyType + " does not match account currency type " + account.getCurrencyType());
		}
		BigDecimal beforeAmount = account.getAmount();
		BigDecimal afterAmount = null;
		if (inOut == 收入) {
			afterAmount = beforeAmount.add(transAmount);
		} else {
			afterAmount = beforeAmount.subtract(transAmount);
		}

		AccountLog accountLog = new AccountLog();
		accountLog.setTitle(title);
		accountLog.setBeforeAmount(beforeAmount);
		accountLog.setTransAmount(transAmount);
		accountLog.setAfterAmount(afterAmount);
		accountLog.setTransTime(new Date());
		accountLog.setCurrencyType(currencyType);

		accountLog.setUserId(account.getUserId());
		accountLog.setInOut(inOut);

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

		validate(accountLog);
		accountLogMapper.insert(accountLog);

		account.setAmount(afterAmount);
		if (accountMapper.update(account) == 0) {
			throw new ConcurrentException();
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

	/**
	 * 使用系统账户付款
	 *
	 * @param userId
	 * @param bizName
	 *            {@link com.zy.model.Constants.ProfitBizName}
	 * @param title
	 * @param currencyType
	 * @param amount
	 * @return
	 */
	public Profit grantProfit(Long userId, String bizName, String title, @NotNull CurrencyType currencyType, BigDecimal amount, String remark) {
		Profit profit = this.createProfit(bizName, currencyType, title, userId, amount, remark);
		this.recordAccountLog(config.getGrantUserId(), title, currencyType, amount, 支出, profit);
		this.recordAccountLog(userId, title, currencyType, amount, 收入, profit);
		return profit;
	}

	public Payment createPayment(@NotBlank String title, @NotNull Long userId, @NotNull CurrencyType currencyType1,
			@NotNull @DecimalMin("0.01") BigDecimal amount1, CurrencyType currencyType2, BigDecimal amount2, @NotBlank String bizName, @NotBlank String bizSn) {

		User user = userMapper.findOne(userId);
		validate(user, NOT_NULL, "user id " + userId + " is not found");

		if (currencyType2 != null) {
			validate(currencyType2, v -> v != currencyType1, "curency type 1 equals curency type 2");
			validate(amount2, NOT_NULL, "amount 2 is null");
			validate(amount2, v -> v.compareTo(new BigDecimal("0.00")) > 0, "amount 2 must be greater than 0.00");
		}

		Payment persistence = paymentMapper.findByBizNameAndBizSn(bizName, bizSn);
		if (persistence != null) {
			return persistence;
		}

		Payment payment = new Payment();
		payment.setSn(ServiceUtils.generatePaymentSn());
		payment.setAmount1(amount1);
		payment.setCurrencyType1(currencyType1);
		payment.setAmount2(amount2);
		payment.setCurrencyType2(currencyType2);
		payment.setCreatedTime(new Date());
		payment.setExpiredTime(null);
		payment.setTitle(title);
		payment.setPaymentStatus(待支付);
		payment.setPayType(PayType.余额);
		payment.setBizName(bizName);
		payment.setBizSn(bizSn);
		payment.setVersion(0);
		payment.setUserId(userId);
		validate(payment);
		paymentMapper.insert(payment);
		return payment;
	}

	public void payPayment(@NotNull Long paymentId) {
		final BigDecimal zero = new BigDecimal("0.00");
		Payment payment = paymentMapper.findOne(paymentId);
		validate(payment, NOT_NULL, "payment id " + paymentId + " is not found");

		Payment.PaymentStatus paymentStatus = payment.getPaymentStatus();
		if (paymentStatus == 已支付) {
			return; // 幂等操作
		} else if (paymentStatus != 待支付) {
			throw new BizException(BizCode.ERROR, "只有待支付订单才能支付");
		}

		Long userId = payment.getUserId();
		CurrencyType currencyType1 = payment.getCurrencyType1();
		CurrencyType currencyType2 = payment.getCurrencyType2();
		BigDecimal amount1 = payment.getAmount1();
		BigDecimal amount2 = payment.getAmount2();
		Account account1 = accountMapper.findByUserIdAndCurrencyType(userId, currencyType1);

		BigDecimal beforeAmount1 = account1.getAmount();
		BigDecimal afterAmount1 = beforeAmount1.subtract(amount1);
		if (afterAmount1.compareTo(zero) < 0) {
			throw new BizException(BizCode.INSUFFICIENT_BALANCE, "支付失败, " + currencyType1.getAlias() + "余额不足");
		}

		String title = payment.getTitle();
		recordAccountLog(userId, title, currencyType1, amount1, InOut.支出, payment);
		recordAccountLog(config.getSysUserId(), title, currencyType1, amount1, InOut.收入, payment);

		if (currencyType2 != null) {
			Account account2 = accountMapper.findByUserIdAndCurrencyType(userId, currencyType2);
			BigDecimal beforeAmount2 = account2.getAmount();
			BigDecimal afterAmount2 = beforeAmount2.subtract(amount2);
			if (afterAmount2.compareTo(zero) < 0) {
				throw new BizException(BizCode.INSUFFICIENT_BALANCE, "支付失败, " + currencyType2.getAlias() + "余额不足");
			}

			title = payment.getTitle();
			recordAccountLog(userId, title, currencyType2, amount2, InOut.支出, payment);
			recordAccountLog(config.getSysUserId(), title, currencyType2, amount2, InOut.收入, payment);
		}

		payment.setPaidTime(new Date());
		payment.setPaymentStatus(已支付);
		if (paymentMapper.update(payment) == 0) {
			throw new ConcurrentException();
		}

	}

	public void refundPayment(@NotNull Long paymentId, @NotNull CurrencyType refundCurrencyType1, @NotNull @DecimalMin("0.00") BigDecimal refund1,
			CurrencyType refundCurrencyType2, @DecimalMin("0.00") BigDecimal refund2) {

		Payment payment = paymentMapper.findOne(paymentId);
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
		recordAccountLog(config.getSysUserId(), title, currencyType1, refund1, InOut.支出, payment);
		recordAccountLog(payment.getUserId(), title, currencyType1, refund1, InOut.收入, payment);

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
			recordAccountLog(config.getSysUserId(), title, currencyType2, refund2, InOut.支出, payment);
			recordAccountLog(payment.getUserId(), title, currencyType2, refund2, InOut.收入, payment);
		} else {
			validate(refund2, NULL, "refund 2 must be null");
		}
		payment.setRefundedTime(new Date());
		payment.setRefund1(refund1);
		payment.setRefund2(refund2);
		payment.setPaymentStatus(PaymentStatus.已退款);

		if (paymentMapper.update(payment) == 0) {
			throw new ConcurrentException();
		}
	}

	public void cancelPayment(@NotNull Long paymentId) {
		Payment payment = paymentMapper.findOne(paymentId);
		validate(payment, NOT_NULL, "payment id " + paymentId + " is not found");
		PaymentStatus paymentStatus = payment.getPaymentStatus();
		if (paymentStatus == PaymentStatus.已取消) {
			return; // 幂等处理
		} else if (paymentStatus != PaymentStatus.待支付) {
			throw new BizException(BizCode.ERROR, "只有待支付支付订单才能取消");
		}

		payment.setPaymentStatus(PaymentStatus.已取消);
		if (paymentMapper.update(payment) == 0) {
			throw new ConcurrentException();
		}
	}

}
