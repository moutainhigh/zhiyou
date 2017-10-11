package com.zy.service.impl;

import com.zy.common.model.query.Page;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserTargetSales;
import com.zy.mapper.UserTargetSalesMapper;
import com.zy.model.query.UserQueryModel;
import com.zy.model.query.UserTargetSalesQueryModel;
import com.zy.service.SystemCodeService;
import com.zy.service.UserService;
import com.zy.service.UserTargetSalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@Service
@Validated
public class UserTargetSalesServiceImpl implements UserTargetSalesService {


    @Autowired
    private UserTargetSalesMapper userTargetSalesMapper;

    @Autowired
    private SystemCodeService systemCodeService;

    @Autowired
    private UserService userService;

    @Override
    public UserTargetSales create(UserTargetSales userTargetSales) {
        validate(userTargetSales);
        userTargetSalesMapper.insert(userTargetSales);
        return userTargetSales;
    }

    @Override
    public UserTargetSales findOne(Long id) {
        return userTargetSalesMapper.findOne(id);
    }

    @Override
    public List<UserTargetSales> findByUserId(Long userId) {
        return userTargetSalesMapper.findAll(UserTargetSalesQueryModel.builder().userIdEQ(userId).build());
    }

    @Override
    public Page<UserTargetSales> findPage(UserTargetSalesQueryModel userTargetSalesQueryModel) {
        if (userTargetSalesQueryModel.getPageNumber() == null)
            userTargetSalesQueryModel.setPageNumber(0);
        if (userTargetSalesQueryModel.getPageSize() == null)
            userTargetSalesQueryModel.setPageSize(20);
        long total = userTargetSalesMapper.count(userTargetSalesQueryModel);
        List<UserTargetSales> data = userTargetSalesMapper.findAll(userTargetSalesQueryModel);
        Page<UserTargetSales> page = new Page<>();
        page.setPageNumber(userTargetSalesQueryModel.getPageNumber());
        page.setPageSize(userTargetSalesQueryModel.getPageSize());
        page.setData(data);
        page.setTotal(total);
        return page;
    }

    @Override
    public List<UserTargetSales> findAll(UserTargetSalesQueryModel userTargetSalesQueryModel) {
        return userTargetSalesMapper.findAll(userTargetSalesQueryModel);
    }

    /**
     * 修改目标销量
     * @param id
     * @param targetSales
     */
    @Override
    public void modifySales(Long id, Integer targetSales) {
        UserTargetSales userTargetSales = findAndValidate(id);
        userTargetSales.setTargetCount(targetSales);
        userTargetSalesMapper.merge(userTargetSales,"targetCount");
    }

    @Override
    public long totalTargetSales(UserTargetSalesQueryModel userTargetSalesQueryModel) {
        return userTargetSalesMapper.totalTargetSales(userTargetSalesQueryModel);
    }

    @Override
    public long count(UserTargetSalesQueryModel userTargetSalesQueryModel) {
        return userTargetSalesMapper.count(userTargetSalesQueryModel);
    }

    /**
     * 未设置目标销量的大区总裁，按平均值设置当月目标销量
     * @param year
     * @param month
     */
    @Override
    public void avgUserTargetSales(Integer year, Integer month) {
        Date now = new Date();
        UserTargetSalesQueryModel userTargetSalesQueryModel = new UserTargetSalesQueryModel();
        userTargetSalesQueryModel.setMonthEQ(month);
        userTargetSalesQueryModel.setYearEQ(year);
        Long total = userTargetSalesMapper.totalTargetSales(userTargetSalesQueryModel);
        total = total == null ? 0 : total;
        List<UserTargetSales> all = userTargetSalesMapper.findAll(userTargetSalesQueryModel);
        List<User> users = userService.findAll(UserQueryModel.builder().userRankEQ(User.UserRank.V4).isPresident(true).build());
        Integer avg = null;
        if(total == 0){
            avg = Integer.parseInt(systemCodeService.findByTypeAndName("DEFAULTTARGETSALES","PRESIDENT").getSystemValue());
        }else {
            avg = (int)(total/all.size());
        }
       // Map<Long, User> userMap = users.stream().collect(Collectors.toMap(v -> v.getId(), v -> v));
        if(all != null && all.size() == 0){
            for (User u: users) {
                UserTargetSales userTargetSales = new UserTargetSales();
                userTargetSales.setCreateTime(now);
                userTargetSales.setMonth(month);
                userTargetSales.setYear(year);
                userTargetSales.setUserId(u.getId());
                userTargetSales.setTargetCount(avg);
                userTargetSalesMapper.insert(userTargetSales);
            }
        }else if(all != null && all.size() > 0){
            Map<Long, UserTargetSales> userTargetSalesMap = all.stream().collect(Collectors.toMap(v -> v.getUserId(), v -> v));
            for (User u: users) {
                if(userTargetSalesMap.get(u.getId()) == null){
                    UserTargetSales userTargetSales = new UserTargetSales();
                    userTargetSales.setCreateTime(now);
                    userTargetSales.setMonth(month);
                    userTargetSales.setYear(year);
                    userTargetSales.setUserId(u.getId());
                    userTargetSales.setTargetCount(avg);
                    userTargetSalesMapper.insert(userTargetSales);
                }
            }
        }
    }

    @Override
    public void delete(Long id) {
        userTargetSalesMapper.delete(id);
    }

    private UserTargetSales findAndValidate(Long id) {
        UserTargetSales userTargetSales = userTargetSalesMapper.findOne(id);
        validate(userTargetSales, NOT_NULL, "userTargetSales id " + id + "not found");
        return userTargetSales;
    }
}
