package com.zy.service.impl;

import com.zy.common.model.query.Page;
import com.zy.entity.act.PolicyCode;
import com.zy.entity.act.Report;
import com.zy.entity.mal.Order;
import com.zy.entity.mal.OrderStore;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.tour.Sequence;
import com.zy.entity.tour.Tour;
import com.zy.entity.tour.TourTime;
import com.zy.entity.tour.TourUser;
import com.zy.entity.usr.UserInfo;
import com.zy.mapper.*;
import com.zy.model.query.OrderStoreQueryModel;
import com.zy.model.query.TourQueryModel;
import com.zy.model.query.TourTimeQueryModel;
import com.zy.model.query.TourUserQueryModel;
import com.zy.service.StoreService;
import com.zy.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

/**
 * Created by it001 on 2017/6/27.
 */
@Service
@Validated
public class StoreServiceImpl implements StoreService {

    @Autowired
    private OrderStoreMapper orderStoreMapper;

    @Override
    public Page<OrderStore> findPage(OrderStoreQueryModel orderStoreQueryModel) {
        if (orderStoreQueryModel.getPageNumber() == null)
            orderStoreQueryModel.setPageNumber(0);
        if (orderStoreQueryModel.getPageSize() == null)
            orderStoreQueryModel.setPageSize(20);
        long total = orderStoreMapper.count(orderStoreQueryModel);
        List<OrderStore> data = orderStoreMapper.findAll(orderStoreQueryModel);
        Page<OrderStore> page = new Page<>();
        page.setPageNumber(orderStoreQueryModel.getPageNumber());
        page.setPageSize(orderStoreQueryModel.getPageSize());
        page.setData(data);
        page.setTotal(total);
        return page;
    }
}
