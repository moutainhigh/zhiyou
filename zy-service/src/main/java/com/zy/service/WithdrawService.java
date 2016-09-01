package com.gc.service;

import java.math.BigDecimal;
import java.util.List;

import com.zy.common.model.query.Page;
import com.gc.entity.fnc.CurrencyType;
import com.gc.entity.fnc.Withdraw;
import com.gc.model.query.WithdrawQueryModel;

public interface WithdrawService {

	Withdraw create(Long userId, CurrencyType currencyType, BigDecimal amount);
	
	void success(Long id, Long operatorUserId, String remark);
	
	void cancel(Long id, Long operatorUserId, String remark);
	
	Withdraw findOne(Long id);
	
	Page<Withdraw> findPage(WithdrawQueryModel withdrawQueryModel);

	List<Withdraw> findAll(WithdrawQueryModel withdrawQueryModel);
	
	long count(WithdrawQueryModel withdrawQueryModel);
}
