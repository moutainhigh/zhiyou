package com.zy.component;

import com.zy.Config;
import com.zy.ServiceUtils;
import com.zy.common.exception.ConcurrentException;
import com.zy.common.exception.ValidationException;
import com.zy.entity.fnc.*;
import com.zy.entity.fnc.AccountLog.InOut;
import com.zy.entity.fnc.Profit.ProfitType;
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
	private TransferMapper transferMapper;

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
		accountLog.setRefUserId(refUserId);

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
		} else if (ref instanceof Transfer) {
			Transfer transfer = (Transfer) ref;
			accountLog.setRefSn(transfer.getSn());
			accountLog.setRefId(transfer.getId());
			accountLog.setAccountLogType(转账单);
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
			BigDecimal afterAmount;
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

	public Profit createAndGrantProfit(@NotNull Long userId, @NotNull ProfitType profitType, Long refId, @NotBlank String title,
	                           @NotNull CurrencyType currencyType, @NotBlank @DecimalMin("0.01") BigDecimal amount) {
		final BigDecimal zero = new BigDecimal("0.00");
		if (amount.compareTo(zero) <= 0) {
			throw new ValidationException("profit amount " + amount + " is wrong");
		}
		Date date = new Date();
		Profit profit = new Profit();
		profit.setProfitType(profitType);
		profit.setTitle(title);
		profit.setUserId(userId);
		profit.setAmount(amount);
		profit.setSn(ServiceUtils.generateProfitSn());
		profit.setCurrencyType(currencyType);
		profit.setCreatedTime(date);
		profit.setRefId(refId);
		profit.setProfitStatus(Profit.ProfitStatus.已发放);
		profit.setGrantedTime(date);
		validate(profit);
		profitMapper.insert(profit);
		Long sysUserId = config.getSysUserId();
		this.recordAccountLog(sysUserId, title, currencyType, amount, 支出, profit, userId);
		this.recordAccountLog(userId, title, currencyType, amount, 收入, profit, sysUserId);
		return profit;
	}

	public Profit createProfit(@NotNull Long userId, @NotNull ProfitType profitType, Long refId, @NotBlank String title,
	                                   @NotNull CurrencyType currencyType, @NotBlank @DecimalMin("0.01") BigDecimal amount) {
		final BigDecimal zero = new BigDecimal("0.00");
		if (amount.compareTo(zero) <= 0) {
			throw new ValidationException("profit amount " + amount + " is wrong");
		}
		Date date = new Date();
		Profit profit = new Profit();
		profit.setProfitType(profitType);
		profit.setTitle(title);
		profit.setUserId(userId);
		profit.setAmount(amount);
		profit.setSn(ServiceUtils.generateProfitSn());
		profit.setCurrencyType(currencyType);
		profit.setCreatedTime(date);
		profit.setRefId(refId);
		profit.setProfitStatus(Profit.ProfitStatus.待发放);
		profit.setGrantedTime(null);
		validate(profit);
		profitMapper.insert(profit);
		return profit;
	}

	public Transfer createTransfer(@NotNull Long fromUserId, @NotNull Long toUserId, @NotNull Transfer.TransferType transferType, Long refId,
	                               @NotBlank String title, @NotNull CurrencyType currencyType,
	                               @NotNull @DecimalMin("0.01") BigDecimal amount) {
		Transfer transfer = new Transfer();
		if (fromUserId.equals(toUserId)) {
			throw new ValidationException("from user id must not be same with to user id");
		}
		
		transfer.setRefId(refId);
		transfer.setTransferStatus(Transfer.TransferStatus.待转账);
		transfer.setTransferType(transferType);
		transfer.setAmount(amount);
		transfer.setFromUserId(fromUserId);
		transfer.setToUserId(toUserId);
		transfer.setCurrencyType(currencyType);
		transfer.setSn(ServiceUtils.generateTransferSn());
		transfer.setCreatedTime(new Date());
		transfer.setTitle(title);
		transfer.setVersion(0);
		transferMapper.insert(transfer);
		validate(transfer);
		return transfer;
	}

}
