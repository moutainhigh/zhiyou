package com.gc.service.impl;

import com.zy.common.model.query.Page;
import com.gc.entity.fnc.Account;
import com.gc.entity.fnc.CurrencyType;
import com.gc.mapper.AccountMapper;
import com.gc.model.query.AccountQueryModel;
import com.gc.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.List;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@Service
@Validated
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountMapper accountMapper;

	@Override
	public Account update(@NotNull Account account) {
		accountMapper.update(account);
		return account;
	}
	
	@Override
	public List<Account> findByUserId(@NotNull @Min(1) Long userId) {
		return accountMapper.findByUserId(userId);
	}

	@Override
	public Account findByUserIdAndCurrencyType(@NotNull @Min(1) Long userId, @NotNull CurrencyType currencyType) {
		return accountMapper.findByUserIdAndCurrencyType(userId, currencyType);
	}

	@Override
	public Page<Account> findPage(AccountQueryModel accountQueryModel) {
		validate(accountQueryModel, NOT_NULL, "account query model is null");
		if (accountQueryModel.getPageNumber() == null)
			accountQueryModel.setPageNumber(0);
		if (accountQueryModel.getPageSize() == null)
			accountQueryModel.setPageSize(20);
		long total = accountMapper.count(accountQueryModel);
		List<Account> data = accountMapper.findAll(accountQueryModel);
		Page<Account> page = new Page<>();
		page.setPageNumber(accountQueryModel.getPageNumber());
		page.setPageSize(accountQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public Account findOne(@NotNull @Min(1) Long id) {
		return accountMapper.findOne(id);
	}

	@Override
	public List<Account> findAll(@NotNull AccountQueryModel accountQueryModel) {
		return accountMapper.findAll(accountQueryModel);
	}

}
