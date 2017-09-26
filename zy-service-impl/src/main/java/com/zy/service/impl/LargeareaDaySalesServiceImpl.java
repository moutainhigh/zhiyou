package com.zy.service.impl;

import com.zy.common.model.query.Page;
import com.zy.common.util.DateUtil;
import com.zy.entity.mal.Order;
import com.zy.entity.report.LargeareaDaySales;
import com.zy.entity.report.TeamProvinceReport;
import com.zy.entity.sys.Area;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserInfo;
import com.zy.mapper.TeamProvinceReportMapper;
import com.zy.model.query.*;
import com.zy.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by liang on 2017/9/26.
 */
@Service
@Validated
public class LargeareaDaySalesServiceImpl implements LargeareaDaySalesService {


    @Override
    public List<LargeareaDaySales> findAll(LargeareaDaySalesQueryModel largeareaDaySalesQueryModel) {
        return null;
    }

    @Override
    public void insert(LargeareaDaySales largeareaDaySales) {

    }
}
