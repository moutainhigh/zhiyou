package com.zy.service.impl;

import com.zy.common.model.query.Page;
import com.zy.entity.usr.UserTargetSales;
import com.zy.mapper.UserTargetSalesMapper;
import com.zy.model.query.UserTargetSalesQueryModel;
import com.zy.service.UserTargetSalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@Service
@Validated
public class UserTargetSalesServiceImpl implements UserTargetSalesService {


    @Autowired
    private UserTargetSalesMapper userTargetSalesMapper;

    @Override
    public UserTargetSales create(UserTargetSales userTargetSales) {
        userTargetSalesMapper.insert(userTargetSales);
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

    private UserTargetSales findAndValidate(Long id) {
        UserTargetSales userTargetSales = userTargetSalesMapper.findOne(id);
        validate(userTargetSales, NOT_NULL, "userTargetSales id " + id + "not found");
        return userTargetSales;
    }
}
