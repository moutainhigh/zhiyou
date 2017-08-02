package com.zy.service.impl;

import com.shengpay.mcl.btc.response.DirectApplyResponse;
import com.zy.Config;
import com.zy.ServiceUtils;
import com.zy.common.exception.BizException;
import com.zy.common.exception.ConcurrentException;
import com.zy.common.model.query.Page;
import com.zy.common.support.shengpay.BatchPaymentClient;
import com.zy.component.FncComponent;
import com.zy.entity.fnc.Account;
import com.zy.entity.fnc.AccountLog.InOut;
import com.zy.entity.fnc.BankCard;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.fnc.Withdraw;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserType;
import com.zy.mapper.AccountMapper;
import com.zy.mapper.BankCardMapper;
import com.zy.mapper.UserMapper;
import com.zy.mapper.WithdrawMapper;
import com.zy.model.BizCode;
import com.zy.model.Constants;
import com.zy.model.query.WithdrawQueryModel;
import com.zy.service.WithdrawService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import static com.zy.common.util.ValidateUtils.*;

@Service
@Validated
@Slf4j
public class WithdrawServiceImpl implements WithdrawService {

	@Autowired
	private AccountMapper accountMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private BankCardMapper bankCardMapper;

	@Autowired
	private WithdrawMapper withdrawMapper;

	@Autowired
	private FncComponent fncComponent;

	@Autowired
	private Config config;

	@Autowired
	private BatchPaymentClient batchPaymentClient;

	@Override
	public Withdraw create(@NotNull Long userId, @NotNull Long bankCardId, @NotNull CurrencyType currencyType, @NotNull @DecimalMin("0.01") BigDecimal amount) {
		final BigDecimal zero = new BigDecimal("0.00");
		validate(userId, NOT_NULL, "user id is null");
		validate(currencyType, NOT_NULL, "currency type is null");
		validate(amount, NOT_NULL, "amount is null");
		validate(amount, v -> v.compareTo(zero) >= 0, currencyType + "amount must not be less than 0.00");

		User user = userMapper.findOne(userId);
		validate(user, NOT_NULL, "user id " + userId + " is not found");
		Account account = accountMapper.findByUserIdAndCurrencyType(userId, currencyType);
		validate(account, NOT_NULL, "account user id " + userId + " currency type " + currencyType + " is not found");
 
		/* check 提现币种 */
		UserType userType = user.getUserType();

		if (userType != UserType.代理 && (currencyType != CurrencyType.现金 || currencyType != CurrencyType.积分)) {
			throw new BizException(BizCode.ERROR, "暂只支持代理现金和积分提现");

		}

	    /* check bank card */
		BankCard bankCard = bankCardMapper.findOne(bankCardId);
		validate(bankCard, NOT_NULL, "bank card id " + bankCardId + " is not found");
		validate(bankCard, v -> v.getUserId().equals(userId), "bank card " + bankCardId + " must be own");

		/* check 余额 */
		BigDecimal beforeAmount = account.getAmount();
		BigDecimal afterAmount = beforeAmount.subtract(amount);
		if (afterAmount.compareTo(zero) < 0) {
			throw new BizException(BizCode.ERROR, "提现申请失败,余额不足");
		}
		Date date = new Date();
		BigDecimal feeRate = config.getWithdrawFeeRate(userType, currencyType);

		BigDecimal fee = amount.multiply(feeRate).setScale(2, RoundingMode.HALF_UP);
		BigDecimal realAmount = amount.subtract(fee);

		Withdraw withdraw = new Withdraw();
		withdraw.setCreatedTime(date);
		withdraw.setCurrencyType(currencyType);
		withdraw.setAmount(amount);
		withdraw.setFeeRate(feeRate);
		withdraw.setFee(fee);
		withdraw.setRealAmount(realAmount);
		withdraw.setAmount(amount);
		withdraw.setSn(ServiceUtils.generateWithdrawSn());
		withdraw.setUserId(userId);
		withdraw.setVersion(0);
		withdraw.setIsToBankCard(true);
		withdraw.setBankCardId(bankCardId);
		withdraw.setOpenId(null);
		withdraw.setTitle(currencyType.getAlias() + "余额提现");
		withdraw.setWithdrawStatus(Withdraw.WithdrawStatus.已申请);
		validate(withdraw);
		withdrawMapper.insert(withdraw);

		Long sysUserId = config.getSysUserId();
		fncComponent.recordAccountLog(userId, "提现申请", currencyType, amount, InOut.支出, withdraw, sysUserId);
		fncComponent.recordAccountLog(sysUserId, "提现申请", currencyType, amount, InOut.收入, withdraw, userId);
		return withdraw;
	}

	@Override
	public void success(@NotNull Long id, @NotNull Long operatorId, @NotBlank String remark) {

		Withdraw withdraw = withdrawMapper.findOne(id);
		validate(withdraw, NOT_NULL, "withdraw id " + id + " is not found null");
		User operator = userMapper.findOne(operatorId);
		validate(operator, NOT_NULL, "operator id" + operatorId + " is not found null");

		if (withdraw.getWithdrawStatus() == Withdraw.WithdrawStatus.提现成功) {
			return; // 幂等处理
		} else if (withdraw.getWithdrawStatus() != Withdraw.WithdrawStatus.已申请) {
			throw new BizException(BizCode.ERROR, "只有已申请状态的提现单可以确认成功");
		}
		if (!withdraw.getIsToBankCard()) {
			throw new BizException(BizCode.ERROR, "暂不支持微信提现");
		}

		withdraw.setRemark(remark);
		withdraw.setOperatorId(operatorId);
		withdraw.setWithdrawStatus(Withdraw.WithdrawStatus.提现成功);
		withdraw.setWithdrawedTime(new Date());
		if (withdrawMapper.update(withdraw) == 0) {
			throw new ConcurrentException();
		}
		CurrencyType currencyType = withdraw.getCurrencyType();
		Long sysUserId = config.getSysUserId();

		fncComponent.recordAccountLog(sysUserId, "提现出账", currencyType, withdraw.getRealAmount(), InOut.支出, withdraw, withdraw.getUserId());
	}

	@Override
	public void cancel(Long id, Long operatorId, String remark) {
		validate(id, NOT_NULL, "withdraw is null");
		validate(operatorId, NOT_NULL, "operator user id is null");
		validate(remark, NOT_BLANK, "remark is blank");

		Withdraw withdraw = withdrawMapper.findOne(id);
		validate(withdraw, NOT_NULL, "withdraw id " + id + " is not found null");
		User operator = userMapper.findOne(operatorId);
		validate(operator, NOT_NULL, "operator id" + operatorId + " is not found null");

		Withdraw.WithdrawStatus withdrawStatus = withdraw.getWithdrawStatus();
		if (withdrawStatus == Withdraw.WithdrawStatus.已取消) {
			return; // 幂等处理
		} else if (withdrawStatus != Withdraw.WithdrawStatus.已申请 && withdrawStatus != Withdraw.WithdrawStatus.处理失败) {
			throw new BizException(BizCode.ERROR, "只有已申请状态的提现单可以取消");
		}

		withdraw.setRemark(withdraw.getRemark() + ";" + remark);
		withdraw.setOperatorId(operatorId);
		withdraw.setWithdrawStatus(Withdraw.WithdrawStatus.已取消);
		if (withdrawMapper.update(withdraw) == 0) {
			throw new ConcurrentException();
		}
		CurrencyType currencyType = withdraw.getCurrencyType();

		BigDecimal amount = withdraw.getAmount();

		Long userId = withdraw.getUserId();
		Long sysUserId = config.getSysUserId();

		fncComponent.recordAccountLog(userId, "提现取消退款", currencyType, amount, InOut.收入, withdraw, sysUserId);
		fncComponent.recordAccountLog(sysUserId, "提现取消退款", currencyType, amount, InOut.支出, withdraw, userId);
	}

	@Override
	public Page<Withdraw> findPage(@NotNull WithdrawQueryModel withdrawQueryModel) {
		if (withdrawQueryModel.getPageNumber() == null)
			withdrawQueryModel.setPageNumber(0);
		if (withdrawQueryModel.getPageSize() == null)
			withdrawQueryModel.setPageSize(20);
		long total = withdrawMapper.count(withdrawQueryModel);
		List<Withdraw> data = withdrawMapper.findAll(withdrawQueryModel);
		Page<Withdraw> page = new Page<>();
		page.setPageNumber(withdrawQueryModel.getPageNumber());
		page.setPageSize(withdrawQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public Withdraw findOne(@NotNull Long id) {
		return withdrawMapper.findOne(id);
	}

	@Override
	public List<Withdraw> findAll(@NotNull WithdrawQueryModel withdrawQueryModel) {
		return withdrawMapper.findAll(withdrawQueryModel);
	}

	@Override
	public long count(@NotNull WithdrawQueryModel withdrawQueryModel) {
		return withdrawMapper.count(withdrawQueryModel);
	}

	@Override
	public Withdraw findBySn(@NotBlank String sn) {
		return withdrawMapper.findBySn(sn);
	}

	@Override
	public void push(@NotNull Long id) {
		Withdraw withdraw = withdrawMapper.findOne(id);
		validate(withdraw, NOT_NULL, "withdraw id " + id + " is not found null");

		if (withdraw.getWithdrawStatus() == Withdraw.WithdrawStatus.已推送) {
			return; // 幂等处理
		} else if (withdraw.getWithdrawStatus() != Withdraw.WithdrawStatus.已申请) {
			throw new BizException(BizCode.ERROR, "只有已申请状态的提现单可以推送");
		}

		BankCard bankCard = bankCardMapper.findOne(withdraw.getBankCardId());
		DirectApplyResponse directApplyResponse = batchPaymentClient.directApply(withdraw.getSn(), withdraw.getAmount(), null, null, bankCard.getBankName(), null, false, bankCard.getRealname(), bankCard.getCardNumber(), Constants.SHENGPAY_BATCH_PAYMENT_NOTIFY);
		if (!batchPaymentClient.isDirectApplyResponseSuccess(directApplyResponse)) {
			throw new BizException(BizCode.ERROR, "推送失败, 错误码" + directApplyResponse.getResultCode() + ", 错误信息" + directApplyResponse.getResultMessage());
		}

		withdraw.setWithdrawStatus(Withdraw.WithdrawStatus.已推送);
		if (withdrawMapper.update(withdraw) == 0) {
			throw new ConcurrentException();
		}

	}

	@Override
	public void autoSuccess(@NotNull Long id) {
		Withdraw withdraw = withdrawMapper.findOne(id);
		validate(withdraw, NOT_NULL, "withdraw id " + id + " is not found null");

		if (withdraw.getWithdrawStatus() == Withdraw.WithdrawStatus.提现成功) {
			return; // 幂等处理
		} else if (withdraw.getWithdrawStatus() != Withdraw.WithdrawStatus.已推送) {
			throw new BizException(BizCode.ERROR, "只有已推送状态的提现单可以确认成功");
		}
		if (!withdraw.getIsToBankCard()) {
			throw new BizException(BizCode.ERROR, "暂不支持微信提现");
		}

		withdraw.setWithdrawStatus(Withdraw.WithdrawStatus.提现成功);
		withdraw.setWithdrawedTime(new Date());
		if (withdrawMapper.update(withdraw) == 0) {
			throw new ConcurrentException();
		}
		CurrencyType currencyType = withdraw.getCurrencyType();
		Long sysUserId = config.getSysUserId();

		fncComponent.recordAccountLog(sysUserId, "提现出账", currencyType, withdraw.getRealAmount(), InOut.支出, withdraw, withdraw.getUserId());
	}

	@Override
	public void autoFailure(@NotNull Long id, String remark) {
		Withdraw withdraw = withdrawMapper.findOne(id);
		validate(withdraw, NOT_NULL, "withdraw id " + id + " is not found null");

		if (withdraw.getWithdrawStatus() == Withdraw.WithdrawStatus.处理失败) {
			return; // 幂等处理
		} else if (withdraw.getWithdrawStatus() != Withdraw.WithdrawStatus.已推送) {
			throw new BizException(BizCode.ERROR, "只有已申请状态的提现单可以确认失败");
		}
		if (!withdraw.getIsToBankCard()) {
			throw new BizException(BizCode.ERROR, "暂不支持微信提现");
		}

		withdraw.setRemark(remark);
		withdraw.setWithdrawStatus(Withdraw.WithdrawStatus.处理失败);
		if (withdrawMapper.update(withdraw) == 0) {
			throw new ConcurrentException();
		}
	}

}
