package com.zy.service.impl;

import com.zy.entity.report.LargeAreaProfit;
import com.zy.mapper.LargeAreaProfitMapper;
import com.zy.model.query.LargeAreaProfitQueryModel;
import com.zy.service.LargeAreaProfitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * Author: Xuwq
 * Date: 2017/9/28.
 */
@Service
@Validated
public class LargeAreaProfitServiceImpl implements LargeAreaProfitService{

    @Autowired
    private LargeAreaProfitMapper largeAreaProfitMapper;

    @Override
    public void insert(LargeAreaProfit largeAreaProfit) {
        largeAreaProfitMapper.insert(largeAreaProfit);
    }

    @Override
    public LargeAreaProfit findLargeAreaProfit(LargeAreaProfitQueryModel largeAreaProfitQueryModel) {
        return largeAreaProfitMapper.findLargeAreaProfit(largeAreaProfitQueryModel);
    }
}
