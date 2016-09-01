package com.zy.service;

import java.math.BigDecimal;
import java.util.List;

import com.zy.common.model.query.Page;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.fnc.Deposit;
import com.zy.entity.fnc.PayType;
import com.zy.model.query.DepositQueryModel;

public interface DepositService {

	Deposit create(String title, CurrencyType currencyType1, BigDecimal amount1, CurrencyType currencyType2, BigDecimal amount2, PayType payType, Long userId, Long paymentId);
	
	void update(Deposit deposit);
	
	void success(Long id, String outerSn);
	
	void cancel(Long id);
	
	Deposit findOne(Long id);
	
	Deposit findBySn(String sn);

	Page<Deposit> findPage(DepositQueryModel depositQueryModel);
	
	List<Deposit> findAll(DepositQueryModel depositQueryModel);
}
