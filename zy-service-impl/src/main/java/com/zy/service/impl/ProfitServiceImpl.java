package com.zy.service.impl;

import com.zy.Config;
import com.zy.common.exception.BizException;
import com.zy.common.exception.ConcurrentException;
import com.zy.common.model.query.Page;
import com.zy.component.FncComponent;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.fnc.Profit;
import com.zy.entity.fnc.Profit.ProfitStatus;
import com.zy.entity.fnc.Profit.ProfitType;
import com.zy.mapper.ProfitMapper;
import com.zy.model.BizCode;
import com.zy.model.query.ProfitQueryModel;
import com.zy.service.ProfitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;
import static com.zy.entity.fnc.AccountLog.InOut.支出;
import static com.zy.entity.fnc.AccountLog.InOut.收入;

@Service
@Validated
public class ProfitServiceImpl implements ProfitService {

	@Autowired
	private ProfitMapper profitMapper;

	@Autowired
	private FncComponent fncComponent;

	@Autowired
	private Config config;

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
	public Profit createAndGrant(Long userId, String title, CurrencyType currencyType, BigDecimal amount, Date createdTime) {
		return fncComponent.createAndGrantProfit(userId, ProfitType.补偿, null, title, currencyType, amount, createdTime);
	}

	@Override
	public void grant(@NotNull Long id) {
		Profit profit = profitMapper.findOne(id);
		Profit.ProfitStatus profitStatus = profit.getProfitStatus();
		if (profitStatus == Profit.ProfitStatus.已发放) {
			return; // 幂等操作
		}

		String title = profit.getTitle();
		CurrencyType currencyType = profit.getCurrencyType();
		BigDecimal amount = profit.getAmount();
		Long userId = profit.getUserId();
		Long sysUserId = config.getSysUserId();
		if (!sysUserId.equals(userId)) {
			fncComponent.recordAccountLog(sysUserId, title, currencyType, amount, 支出, profit, userId);
			fncComponent.recordAccountLog(userId, title, currencyType, amount, 收入, profit, sysUserId);
		}
		profit.setGrantedTime(new Date());
		profit.setProfitStatus(Profit.ProfitStatus.已发放);
		if (profitMapper.update(profit) == 0) {
			throw new ConcurrentException();
		}

	}

	@Override
	public Profit findOne(@NotNull Long id) {
		return profitMapper.findOne(id);
	}

	@Override
	public List<BigDecimal> queryRevenue(@NotNull ProfitQueryModel profitQueryModel) {
		return profitMapper.queryRevenue(profitQueryModel);
	}

	@Override
	public void cancel(@NotNull Long id) {
		Profit profit = profitMapper.findOne(id);
		validate(profit, NOT_NULL, "profit id " + id + " not found");

		if (profit.getProfitStatus() == ProfitStatus.已取消) {
			return;
		} else if (profit.getProfitStatus() != ProfitStatus.待发放) {
			throw new BizException(BizCode.ERROR, "只有待发放收益才能取消");
		}

		profit.setProfitStatus(ProfitStatus.已取消);
		if (profitMapper.update(profit) == 0) {
			throw new ConcurrentException();
		}
	}

}
