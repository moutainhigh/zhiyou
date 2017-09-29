package com.zy.mapper;

import com.zy.entity.report.LargeAreaProfit;
import com.zy.model.query.LargeAreaProfitQueryModel;

/**
 * Author: Xuwq
 * Date: 2017/9/28.
 */
public interface LargeAreaProfitMapper {
    void insert(LargeAreaProfit largeAreaProfit);

    LargeAreaProfit findLargeAreaProfit(LargeAreaProfitQueryModel largeAreaProfitQueryModel);
}
