package com.zy.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.zy.common.model.query.Page;
import com.zy.component.FncComponent;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.fnc.Profit;
import com.zy.mapper.ProfitMapper;
import com.zy.model.query.ProfitQueryModel;
import com.zy.service.ProfitService;

@Service
@Validated
public class ProfitServiceImpl implements ProfitService {

	@Autowired
	private ProfitMapper profitMapper;

	@Autowired
	private FncComponent fncComponent;

	@Override
	public Page<Profit> findPage(@NotNull ProfitQueryModel profitQueryModel) {
		if (profitQueryModel.getPageNumber() == null)
			profitQueryModel.setPageNumber(0);
		if (profitQueryModel.getPageSize() == null)
			profitQueryModel.setPageSize(20);
		long total = profitMapper.count(profitQueryModel);
		List<Profit> data = profitMapper.findAll(profitQueryModel);
		Page<Profit> page = new Page<>();
		page.setPageNumber(profitQueryModel.getPageNumber());
		page.setPageSize(profitQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public List<Profit> findAll(@NotNull ProfitQueryModel profitQueryModel) {
		return profitMapper.findAll(profitQueryModel);
	}

	@Override
	public Profit grant(Long userId, String bizName, String title, CurrencyType currencyType, BigDecimal amount, String remark) {
		return fncComponent.grantProfit(userId, bizName, title, currencyType, amount, remark);
	}

}
