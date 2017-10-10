package com.zy.service;

import com.zy.entity.report.LargeAreaProfit;
import com.zy.model.query.LargeAreaProfitQueryModel;

import java.util.Map;

/**
 * Author: Xuwq
 * Date: 2017/9/28.
 */
public interface LargeAreaProfitService {

    void insert(LargeAreaProfit largeAreaProfit);

    LargeAreaProfit findLargeAreaProfit(LargeAreaProfitQueryModel largeAreaProfitQueryModel);

    Map<String, Object> findAll(LargeAreaProfitQueryModel largeAreaProfitQueryModel);

    Map<String,Object> findByLargeAreaName(LargeAreaProfitQueryModel largeAreaProfitQueryModel);
}
