package com.zy.mapper;


import java.math.BigDecimal;
import java.util.List;

import com.zy.model.dto.ProfitSumDto;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.fnc.Profit;
import com.zy.model.query.ProfitQueryModel;


public interface ProfitMapper {

	int insert(Profit profit);

	int update(Profit profit);

	int merge(@Param("profit") Profit profit, @Param("fields")String... fields);

	int delete(Long id);

	Profit findOne(Long id);

	List<Profit> findAll(ProfitQueryModel profitQueryModel);

	long count(ProfitQueryModel profitQueryModel);

	Double sum(ProfitQueryModel profitQueryModel);

	List<ProfitSumDto> sumGroupBy(ProfitQueryModel profitQueryModel);

	List<BigDecimal> queryRevenue(ProfitQueryModel profitQueryModel);
}