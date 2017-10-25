package com.zy.service.impl;

import com.zy.common.util.DateUtil;
import com.zy.entity.report.LargeAreaProfit;
import com.zy.entity.sys.SystemCode;
import com.zy.entity.usr.User;
import com.zy.mapper.LargeAreaProfitMapper;
import com.zy.model.query.LargeAreaProfitQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.LargeAreaProfitService;
import com.zy.service.SystemCodeService;
import com.zy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private UserService userService;

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
     *
     * @param type
     * @param largeAreaProfitQueryModel
     * @return
     */
    @Override
    public Map<String, Object> findAll(String type, LargeAreaProfitQueryModel largeAreaProfitQueryModel) {
        Map<String ,Object> returnMap = new HashMap<>();
        Map<String ,Object> rMap = new HashMap<>();
        Map<String ,Object> sMap = new HashMap<>();
        Map<String ,Object> map = new HashMap<>();
        double profit [] = new double[12];
        double yProfit [] = new double[12];
        double relativeRate [] = new double[12];
        double sameiveRate [] = new double[12];
        if (type.equals("0")){
            //查询所有存在的大区
            List<SystemCode> systemCodeList = systemCodeService.findByType("LargeAreaType");
            for (SystemCode systemCode: systemCodeList) {
                //查询每个大区每个月的数据
                for (int i = 12; i >= 1;i--){
                    double pdata = 0d;
                    //过滤出每个月的数据
                    LargeAreaProfitQueryModel la = new LargeAreaProfitQueryModel();
                    la.setYearEQ(DateUtil.getYear(new Date()));
                    la.setMonthEQ(i);
                    la.setLargeAreaValueEQ(Integer.parseInt(systemCode.getSystemValue()));
                    Double num = largeAreaProfitMapper.queryProfitByMonth(la);
                    if (num == null){
                        num = 0d;
                    }
                    pdata = num;
                    profit[i-1] = pdata;

                    //同月去年数据
                    double yData = 0d;
                    la.setYearEQ(DateUtil.getYear(new Date())-1);
                    Double count = largeAreaProfitMapper.queryProfitByMonth(la);
                    if (count == null){
                        count = 0d;
                    }
                    yData = count;
                    yProfit[i-1] = yData;
                }
                returnMap.put(systemCode.getSystemName(), DateUtil.arryToString(profit,false));

                //循环这个区的所有数据计算环比
                for (int i = profit.length; i >= 1;i--){
                    double data = 0d;
                    double r = 0.00d;
                    if (i-2 >= 0 && profit [i-1] >= 0 && profit [i-2] > 0){
                        r = DateUtil.formatDouble((profit [i-1] - profit [i-2]) / profit [i-2] * 100) ;
                    }else if (i-2 >= 0 && profit [i-1] > 0 && profit [i-2] == 0){
                        r = 100;
                    }else if (i-2 >= 0 && profit [i-1] == 0 && profit [i-2] == 0){
                        r = 0;
                    }
                    data = r;
                    if (i-2 >= 0){
                        relativeRate[i-1] = data;
                    }
                }
                rMap.put(systemCode.getSystemName(),DateUtil.arryToString(relativeRate,false));

                //循环这个区的所有数据计算同比
                for (int i = profit.length; i >= 1;i--){
                    double data = 0d;
                    double s = 0.00d;
                    if (profit [i-1] >= 0 && yProfit [i-1] > 0){
                        s = DateUtil.formatDouble((profit [i-1] - yProfit [i-1]) / yProfit [i-1] * 100) ;
                    }else if (profit [i-1] > 0 && yProfit [i-1] == 0){
                        s = 100;
                    }else if (profit [i-1] == 0 && yProfit [i-1] == 0){
                        s = 0;
                    }
                    data = s;
                    sameiveRate[i-1] = data;
                }
                sMap.put(systemCode.getSystemName(),DateUtil.arryToString(sameiveRate,false));
            }
        }else {
            //根据大区查询所有总裁
            List<User> v4Users = userService.findAll(UserQueryModel.builder().largeArea(Integer.parseInt(type)).build()).stream()
                    .filter(v -> v.getIsPresident() == true)
                    .collect(Collectors.toList());
            for (User user: v4Users) {
                List<Long> ids = new ArrayList<>();
                ids.add(user.getId());
                //查询所有大区总裁的特级团队
                List<User> users = userService.findAll(UserQueryModel.builder().parentIdEQ(user.getId()).build()).stream()
                        .filter(v -> v.getUserRank() == User.UserRank.V4).collect(Collectors.toList());
                for (User u: users) {
                    ids.add(u.getId());
                }

                //查询每个总裁每个月的数据
                for (int i = 12; i >= 1;i--){
                    double pdata = 0d;
                    //过滤出每个月的数据
                    LargeAreaProfitQueryModel la = new LargeAreaProfitQueryModel();
                    la.setYearEQ(DateUtil.getYear(new Date()));
                    la.setMonthEQ(i);
                    la.setIds(ids);
                    Double num = largeAreaProfitMapper.queryProfitByMonth(la);
                    if (num == null){
                        num = 0d;
                    }
                    pdata = num;
                    profit[i-1] = pdata;

                    //同月去年数据
                    double yData = 0d;
                    la.setYearEQ(DateUtil.getYear(new Date())-1);
                    Double count = largeAreaProfitMapper.queryProfitByMonth(la);
                    if (count == null){
                        count = 0d;
                    }
                    yData = count;
                    yProfit[i-1] = yData;
                }
                returnMap.put(user.getNickname(),DateUtil.arryToString(profit,false));

                //循环这个区的所有数据计算环比
                for (int i = profit.length; i >= 1;i--){
                    double data = 0d;
                    double r = 0.00d;
                    if (i-2 >= 0 && profit [i-1] >= 0 && profit [i-2] > 0){
                        r = DateUtil.formatDouble((profit [i-1] - profit [i-2]) / profit [i-2] * 100) ;
                    }else if (i-2 >= 0 && profit [i-1] > 0 && profit [i-2] == 0){
                        r = 100;
                    }else if (i-2 >= 0 && profit [i-1] == 0 && profit [i-2] == 0){
                        r = 0;
                    }
                    data = r;
                    if (i-2 >= 0){
                        relativeRate[i-1] = data;
                    }
                }
                rMap.put(user.getNickname(),DateUtil.arryToString(relativeRate,false));

                //循环这个区的所有数据计算同比
                for (int i = profit.length; i >= 1;i--){
                    double data = 0d;
                    double s = 0.00d;
                    if (profit [i-1] >= 0 && yProfit [i-1] > 0){
                        s = DateUtil.formatDouble((profit [i-1] - yProfit [i-1]) / yProfit [i-1] * 100) ;
                    }else if (profit [i-1] > 0 && yProfit [i-1] == 0){
                        s = 100;
                    }else if (profit [i-1] == 0 && yProfit [i-1] == 0){
                        s = 0;
                    }
                    data = s;
                    sameiveRate[i-1] = data;
                }
                sMap.put(user.getNickname(),DateUtil.arryToString(sameiveRate,false));
            }
        }
        map.put("profit",returnMap);
        map.put("relativeRate",rMap);
        map.put("sameiveRate",sMap);
        return map;

    }
}
