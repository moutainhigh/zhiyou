package com.zy.mapper;


import com.zy.entity.mal.OrderMonthlySettlement;
import com.zy.model.query.OrderMonthlySettlementQueryModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface OrderMonthlySettlementMapper {

	int insert(OrderMonthlySettlement orderMonthlySettlement);

	int update(OrderMonthlySettlement orderMonthlySettlement);

	int merge(@Param("orderMonthlySettlement") OrderMonthlySettlement orderMonthlySettlement, @Param("fields")String... fields);

	int delete(Long id);

	OrderMonthlySettlement findOne(Long id);

	List<OrderMonthlySettlement> findAll(OrderMonthlySettlementQueryModel orderMonthlySettlementQueryModel);

	long count(OrderMonthlySettlementQueryModel orderMonthlySettlementQueryModel);

	OrderMonthlySettlement findByYearAndMonth(String yearAndMonth);
}