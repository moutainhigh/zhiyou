package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.report.LargeareaMonthSales;
import com.zy.model.query.LargeareaMonthSalesQueryModel;


public interface LargeareaMonthSalesMapper {

	int insert(LargeareaMonthSales largeareaMonthSales);

	int update(LargeareaMonthSales largeareaMonthSales);

	int merge(@Param("largeareaMonthSales") LargeareaMonthSales largeareaMonthSales, @Param("fields")String... fields);

	int delete(Long id);

	LargeareaMonthSales findOne(Long id);

	List<LargeareaMonthSales> findAll(LargeareaMonthSalesQueryModel largeareaMonthSalesQueryModel);

	long count(LargeareaMonthSalesQueryModel largeareaMonthSalesQueryModel);

}