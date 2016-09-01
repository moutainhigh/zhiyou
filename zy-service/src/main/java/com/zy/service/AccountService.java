package com.gc.service;

import java.util.List;

import com.zy.common.model.query.Page;
import com.gc.entity.fnc.Account;
import com.gc.entity.fnc.CurrencyType;
import com.gc.model.query.AccountQueryModel;

public interface AccountService {

	Account update(Account account);
	
	List<Account> findAll(AccountQueryModel accountQueryModel);
	
	Page<Account> findPage(AccountQueryModel accountQueryModel);
	
	Account findOne(Long id);
	
	Account findByUserIdAndCurrencyType(Long userId, CurrencyType currencyType);

	List<Account> findByUserId(Long userId);
	
}
