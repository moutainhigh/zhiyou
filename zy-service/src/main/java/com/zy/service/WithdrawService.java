package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.fnc.Withdraw;
import com.zy.model.query.WithdrawQueryModel;

import java.math.BigDecimal;
import java.util.List;

public interface WithdrawService {

	Withdraw create(Long userId, Long bankCardId, CurrencyType currencyType, BigDecimal amount);
	
	void success(Long id, Long operatorUserId, String remark);
	
	void cancel(Long id, Long operatorUserId, String remark);
	
	Withdraw findOne(Long id);
	
	Page<Withdraw> findPage(WithdrawQueryModel withdrawQueryModel);

	List<Withdraw> findAll(WithdrawQueryModel withdrawQueryModel);
	
	long count(WithdrawQueryModel withdrawQueryModel);
}
