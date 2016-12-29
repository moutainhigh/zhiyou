package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.fnc.Withdraw;
import com.zy.model.query.WithdrawQueryModel;

import java.math.BigDecimal;
import java.util.List;

public interface WithdrawService {

	Withdraw create(Long userId, Long bankCardId, CurrencyType currencyType, BigDecimal amount);
	
	void success(Long id, Long operatorId, String remark); // 手动成功
	
	void cancel(Long id, Long operatorId, String remark); // 手动取消
	
	Withdraw findOne(Long id);
	
	Page<Withdraw> findPage(WithdrawQueryModel withdrawQueryModel);

	List<Withdraw> findAll(WithdrawQueryModel withdrawQueryModel);
	
	long count(WithdrawQueryModel withdrawQueryModel);

	Withdraw findBySn(String sn);

	void push(Long id); // 推送

	void autoSuccess(Long id); // 自动成功

	void autoFailure(Long id, String remark); // 自动失败
}
