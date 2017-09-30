package com.zy.mapper;

import com.zy.entity.report.LargeAreaProfit;
import com.zy.model.query.LargeAreaProfitQueryModel;

import java.util.List;

/**
 * Author: Xuwq
 * Date: 2017/9/28.
 */
public interface LargeAreaProfitMapper {
    void insert(LargeAreaProfit largeAreaProfit);

    LargeAreaProfit findLargeAreaProfit(LargeAreaProfitQueryModel largeAreaProfitQueryModel);

    List<LargeAreaProfit> findList(LargeAreaProfitQueryModel largeAreaProfitQueryModel);

    Double queryProfitByMonth(LargeAreaProfitQueryModel largeAreaProfitQueryModel);
}
