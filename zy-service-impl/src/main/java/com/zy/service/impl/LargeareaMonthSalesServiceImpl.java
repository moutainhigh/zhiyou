package com.zy.service.impl;

import com.zy.entity.report.LargeareaMonthSales;
import com.zy.mapper.LargeareaMonthSalesMapper;
import com.zy.model.query.LargeareaMonthSalesQueryModel;
import com.zy.service.LargeareaMonthSalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Created by liang on 2017/9/26.
 */
@Service
@Validated
public class LargeareaMonthSalesServiceImpl implements LargeareaMonthSalesService {


    @Autowired
    private LargeareaMonthSalesMapper largeareaMonthSalesMapper;

    @Override
    public List<LargeareaMonthSales> findAll(LargeareaMonthSalesQueryModel largeareaMonthSalesQueryModel) {
        return largeareaMonthSalesMapper.findAll(largeareaMonthSalesQueryModel);
    }

    @Override
    public void insert(LargeareaMonthSales largeareaMonthSales) {
        largeareaMonthSalesMapper.insert(largeareaMonthSales);
    }
}
