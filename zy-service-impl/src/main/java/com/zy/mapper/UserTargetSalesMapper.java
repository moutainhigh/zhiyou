package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.usr.UserTargetSales;
import com.zy.model.query.UserTargetSalesQueryModel;


public interface UserTargetSalesMapper {

	int insert(UserTargetSales userTargetSales);

	int update(UserTargetSales userTargetSales);

	int merge(@Param("userTargetSales") UserTargetSales userTargetSales, @Param("fields")String... fields);

	int delete(UserTargetSales userTargetSales);

	UserTargetSales findOne(Long id);

	List<UserTargetSales> findAll(UserTargetSalesQueryModel userTargetSalesQueryModel);

	long count(UserTargetSalesQueryModel userTargetSalesQueryModel);

	Long totalTargetSales(UserTargetSalesQueryModel userTargetSalesQueryModel);

}