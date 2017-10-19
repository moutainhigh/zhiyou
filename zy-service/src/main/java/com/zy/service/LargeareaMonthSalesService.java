package com.zy.service;


import com.zy.entity.report.LargeareaMonthSales;
import com.zy.model.query.LargeareaMonthSalesQueryModel;

import java.util.List;

public interface LargeareaMonthSalesService {

     List<LargeareaMonthSales> findAll(LargeareaMonthSalesQueryModel largeareaMonthSalesQueryModel);

     void insert(LargeareaMonthSales largeareaMonthSales);

}
