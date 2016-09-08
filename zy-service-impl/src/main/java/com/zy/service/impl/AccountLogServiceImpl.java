package com.zy.service.impl;

import com.zy.common.exception.ConcurrentException;
import com.zy.common.model.query.Page;
import com.zy.entity.fnc.Account;
import com.zy.entity.fnc.AccountLog;
import com.zy.entity.fnc.CurrencyType;
import com.zy.mapper.AccountLogMapper;
import com.zy.mapper.AccountMapper;
import com.zy.model.query.AccountLogQueryModel;
import com.zy.service.AccountLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;
import static com.zy.entity.fnc.AccountLog.InOut.收入;

@Service
@Validated
public class AccountLogServiceImpl implements AccountLogService {

	Logger logger = LoggerFactory.getLogger(AccountLogServiceImpl.class);
	
	@Autowired
	private AccountLogMapper accountLogMapper;

	@Autowired
	private AccountMapper accountMapper;

	@Override
	public Page<AccountLog> findPage(@NotNull AccountLogQueryModel accountLogQueryModel) {
		if(accountLogQueryModel.getPageNumber() == null)
			accountLogQueryModel.setPageNumber(0);
		if(accountLogQueryModel.getPageSize() == null)
			accountLogQueryModel.setPageSize(20);
		long total = accountLogMapper.count(accountLogQueryModel);
		List<AccountLog> data = accountLogMapper.findAll(accountLogQueryModel);
		Page<AccountLog> page = new Page<>();
		page.setPageNumber(accountLogQueryModel.getPageNumber());
		page.setPageSize(accountLogQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public void acknowledge(@NotNull Long id) {
		AccountLog accountLog = accountLogMapper.findOne(id);
		validate(accountLog, NOT_NULL, "account log id " + id + " is not found");

		if (accountLog.getIsAcknowledged()) {
			return; // 幂等操作
		}

		Long userId = accountLog.getUserId();
		CurrencyType currencyType = accountLog.getCurrencyType();
		AccountLog.InOut inOut = accountLog.getInOut();
		BigDecimal transAmount = accountLog.getTransAmount();
		Account account = accountMapper.findByUserIdAndCurrencyType(userId, currencyType);

		BigDecimal beforeAmount = account.getAmount();
		BigDecimal afterAmount;
		if (inOut == 收入) {
			afterAmount = beforeAmount.add(transAmount);
		} else {
			afterAmount = beforeAmount.subtract(transAmount);
		}

		accountLog.setIsAcknowledged(true);
		accountLog.setAfterAmount(afterAmount);
		accountLog.setBeforeAmount(beforeAmount);
		validate(accountLog);
		if (accountLogMapper.update(accountLog) == 0) {
			throw new ConcurrentException();
		}
		account.setAmount(afterAmount);
		if (accountMapper.update(account) == 0) {
			throw new ConcurrentException();
		}


	}

	@Override
	public List<AccountLog> findAll(@NotNull AccountLogQueryModel accountLogQueryModel) {
		return accountLogMapper.findAll(accountLogQueryModel);
	}

}
