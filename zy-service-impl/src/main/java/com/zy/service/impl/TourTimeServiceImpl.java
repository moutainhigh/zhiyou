package com.zy.service.impl;

import com.zy.entity.tour.TourTime;
import com.zy.mapper.TourTimeMapper;
import com.zy.service.TourTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * Author: Xuwq
 * Date: 2017/7/4.
 */
@Service
@Validated
public class TourTimeServiceImpl implements TourTimeService{

    @Autowired
    private TourTimeMapper timeMapperfoMapper;

    @Override
    public TourTime findOne(Long timeId) {
        return timeMapperfoMapper.findOne(timeId);
    }
}
