package com.gc.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.gc.entity.fnc.Profit;
import com.gc.model.query.ProfitQueryModel;


public interface ProfitMapper {

	int insert(Profit profit);

	int update(Profit profit);

	int merge(@Param("profit") Profit profit, @Param("fields")String... fields);

	int delete(Long id);

	Profit findOne(Long id);

	List<Profit> findAll(ProfitQueryModel profitQueryModel);

	long count(ProfitQueryModel profitQueryModel);

}