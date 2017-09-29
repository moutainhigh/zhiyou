package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.fnc.Profit;
import com.zy.entity.usr.User;
import com.zy.model.query.ProfitQueryModel;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ProfitService {

	Page<Profit> findPage(ProfitQueryModel profitQueryModel);
	
	List<Profit> findAll(ProfitQueryModel profitQueryModel);
	
	Profit createAndGrant(Long userId, String title, CurrencyType currencyType, BigDecimal amount, Date createdTime);

	void grant(Long id);
	
	void cancel(Long id);
	
	Profit findOne(Long id);

	Map<String, Object> queryRevenue(ProfitQueryModel profitQueryModel);

	Map<String,Object> countIncomeDataByUser(Long userId, Map<String, Object> map);

	List<Profit> orderRevenueDetail(ProfitQueryModel profitQueryModel);

	void insert(List<User> v4Users);
}
