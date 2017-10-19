package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.usr.UserTargetSales;
import com.zy.model.query.UserTargetSalesQueryModel;

import java.util.List;

public interface UserTargetSalesService {

	UserTargetSales create(UserTargetSales userTargetSales);

	UserTargetSales findOne(Long id);

	List<UserTargetSales> findByUserId(Long userId);

	Page<UserTargetSales> findPage(UserTargetSalesQueryModel userTargetSalesQueryModel);

	List<UserTargetSales> findAll(UserTargetSalesQueryModel userTargetSalesQueryModel);

	void modifySales(Long id, Long userId, Integer targetSales);

	long totalTargetSales(UserTargetSalesQueryModel userTargetSalesQueryModel);

	long count(UserTargetSalesQueryModel userTargetSalesQueryModel);

	void avgUserTargetSales(Integer year,Integer month);

	void delete(Long id, Long userId);

}
