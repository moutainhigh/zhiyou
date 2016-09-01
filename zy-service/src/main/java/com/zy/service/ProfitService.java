package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.fnc.Profit;
import com.zy.model.query.ProfitQueryModel;

import java.math.BigDecimal;
import java.util.List;

public interface ProfitService {

	Page<Profit> findPage(ProfitQueryModel profitQueryModel);
	
	List<Profit> findAll(ProfitQueryModel profitQueryModel);
	
	Profit grant(Long userId, String bizName, String title, CurrencyType currencyType, BigDecimal amount, String remark);
	
}
