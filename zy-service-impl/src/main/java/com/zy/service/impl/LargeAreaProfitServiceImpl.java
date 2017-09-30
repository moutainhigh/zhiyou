package com.zy.service.impl;

import com.zy.common.util.DateUtil;
import com.zy.entity.report.LargeAreaProfit;
import com.zy.entity.sys.SystemCode;
import com.zy.mapper.LargeAreaProfitMapper;
import com.zy.model.query.LargeAreaProfitQueryModel;
import com.zy.service.LargeAreaProfitService;
import com.zy.service.SystemCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Author: Xuwq
 * Date: 2017/9/28.
 */
@Service
@Validated
public class LargeAreaProfitServiceImpl implements LargeAreaProfitService{

    @Autowired
    private LargeAreaProfitMapper largeAreaProfitMapper;

    @Autowired
    private SystemCodeService systemCodeService;

    @Override
    public void insert(LargeAreaProfit largeAreaProfit) {
        largeAreaProfitMapper.insert(largeAreaProfit);
    }

    @Override
    public LargeAreaProfit findLargeAreaProfit(LargeAreaProfitQueryModel largeAreaProfitQueryModel) {
        return largeAreaProfitMapper.findLargeAreaProfit(largeAreaProfitQueryModel);
    }

    /**
     * 公司收益
     * @param largeAreaProfitQueryModel
     * @return
     */
    @Override
    public Map<String, Object> findAll(LargeAreaProfitQueryModel largeAreaProfitQueryModel) {
        Map<String ,Object> returnMap = new HashMap<>();
        Map<String ,Object> rMap = new HashMap<>();
        Map<String ,Object> map = new HashMap<>();
        double profit [] = new double[12];
        double relativeRate [] = new double[12];
        double sameRate [] = new double[12];
        //查询所有存在的大区
        List<SystemCode> systemCodeList = systemCodeService.findByType("LargeAreaType");
        for (SystemCode systemCode: systemCodeList) {
            //查询每个大区每个月的数据
                for (int i = 12; i >= 1;i--){
                    double data = 0d;
                    //过滤出每个月的数据
                    LargeAreaProfitQueryModel la = new LargeAreaProfitQueryModel();
                    la.setMonthEQ(i);
                    la.setLargeAreaValueEQ(Integer.parseInt(systemCode.getSystemValue()));
                    Double num = largeAreaProfitMapper.queryProfitByMonth(la);
                    if (num == null){
                        num = 0d;
                    }
                    data = num;
                    profit[i-1] = data;
                }
            returnMap.put(systemCode.getSystemName(), DateUtil.arryToString(profit,false));

            //循环这个区的所有数据计算环比
            for (int i = profit.length; i >= 1;i--){
                double data = 0d;
                double r = 0.00d;
                if (i-2 >= 0 && profit [i-1] != 0 && profit [i-2] != 0){
                    r = DateUtil.formatDouble((profit [i-2] - profit [i-1]) / profit [i-1] * 100) ;
                }else if (i-2 >= 0 && profit [i-1] == 0 && profit [i-2] > 0){
                    r = 100;
                }else if (i-2 >= 0 && profit [i-1] > 0 && profit [i-2] == 0){
                    r = -100;
                }
                data = r;
                if (i-2 >= 0){
                    relativeRate[i-2] = data;
                }
            }
            rMap.put(systemCode.getSystemName(),relativeRate);

        }
        map.put("profit",returnMap);
        map.put("relativeRate",rMap);
        return map;

    }

}
