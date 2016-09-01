package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.fnc.Account;
import com.zy.model.query.AccountQueryModel;

import com.zy.entity.fnc.CurrencyType;

public interface AccountMapper {

	int insert(Account account);

	int update(Account account);

	int merge(@Param("account") Account account, @Param("fields")String... fields);

	int delete(Long id);

	Account findOne(Long id);

	List<Account> findAll(AccountQueryModel accountQueryModel);

	long count(AccountQueryModel accountQueryModel);

	Account findByUserIdAndCurrencyType(@Param("userId") Long userId, @Param("currencyType") CurrencyType currencyType);

	List<Account> findByUserId(Long userId);

}