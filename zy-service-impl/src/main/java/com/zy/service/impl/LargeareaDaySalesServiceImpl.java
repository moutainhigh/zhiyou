package com.zy.service.impl;

import com.zy.entity.report.LargeareaDaySales;
import com.zy.mapper.LargeareaDaySalesMapper;
import com.zy.model.query.LargeareaDaySalesQueryModel;
import com.zy.service.LargeareaDaySalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Created by liang on 2017/9/26.
 */
@Service
@Validated
public class LargeareaDaySalesServiceImpl implements LargeareaDaySalesService {


    @Autowired
    private LargeareaDaySalesMapper largeareaDaySalesMapper;

    @Override
    public List<LargeareaDaySales> findAll(LargeareaDaySalesQueryModel largeareaDaySalesQueryModel) {
        return largeareaDaySalesMapper.findAll(largeareaDaySalesQueryModel);
    }

    @Override
    public void insert(LargeareaDaySales largeareaDaySales) {
        largeareaDaySalesMapper.insert(largeareaDaySales);
    }
}
