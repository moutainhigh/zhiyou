package com.zy.service.impl;

import static com.zy.common.util.ValidateUtils.NOT_BLANK;
import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.zy.Config;
import com.zy.ServiceUtils;
import com.zy.common.exception.BizException;
import com.zy.common.exception.ConcurrentException;
import com.zy.common.model.query.Page;
import com.zy.component.FncComponent;
import com.zy.entity.fnc.Account;
import com.zy.entity.fnc.AccountLog.InOut;
import com.zy.entity.fnc.BankCard;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.fnc.Withdraw;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserType;
import com.zy.model.BizCode;
import com.zy.model.query.WithdrawQueryModel;
import com.zy.service.WithdrawService;

@Service
@Validated
public class WithdrawServiceImpl implements WithdrawService {

	@Autowired
	private com.zy.mapper.AccountMapper accountMapper;

	@Autowired
	private com.zy.mapper.UserMapper userMapper;

	@Autowired
	private com.zy.mapper.BankCardMapper bankCardMapper;

	@Autowired
	private com.zy.mapper.WithdrawMapper withdrawMapper;

	@Autowired
	private FncComponent fncComponent;

	@Autowired
	private Config config;

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

		if (userType != UserType.代理 && currencyType != CurrencyType.现金) {
			throw new BizException(BizCode.ERROR, "暂只支持代理现金提现");

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
		withdraw.setTitle("余额提现");
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

//		final BigDecimal zero = new BigDecimal("0.00");
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

		/*BigDecimal fee = withdraw.getFee();
		if (fee.compareTo(zero) > 0) {
			String title = "提现单" + withdraw.getSn() + "收益";
			Profit profit = fncComponent.createProfit(BIZ_NAME_WITHDRAW, currencyType, title, sysUserId, fee, null);
		}*/
		
		// TODO 暂不处理提现手续费收益
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

		if (withdraw.getWithdrawStatus() == Withdraw.WithdrawStatus.已取消) {
			return; // 幂等处理
		} else if (withdraw.getWithdrawStatus() != Withdraw.WithdrawStatus.已申请) {
			throw new BizException(BizCode.ERROR, "只有已申请状态的提现单可以取消");
		}

		withdraw.setRemark(remark);
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

}
