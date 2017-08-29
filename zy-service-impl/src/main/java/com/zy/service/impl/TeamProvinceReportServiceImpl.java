package com.zy.service.impl;

import com.zy.common.model.query.Page;
import com.zy.entity.report.TeamProvinceReport;
import com.zy.mapper.TeamProvinceReportMapper;
import com.zy.model.query.TeamProvinceReportQueryModel;
import com.zy.service.TeamProvinceReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Created by liang on 2017/8/28.
 */
@Service
@Validated
public class TeamProvinceReportServiceImpl implements TeamProvinceReportService {

    @Autowired
    private TeamProvinceReportMapper teamProvinceReportMapper;

    @Override
    public Page<TeamProvinceReport> findPage(TeamProvinceReportQueryModel teamProvinceReportQueryModel) {
        if (teamProvinceReportQueryModel.getPageNumber() == null)
            teamProvinceReportQueryModel.setPageNumber(0);
        if (teamProvinceReportQueryModel.getPageSize() == null)
            teamProvinceReportQueryModel.setPageSize(20);
        long total = teamProvinceReportMapper.count(teamProvinceReportQueryModel);
        List<TeamProvinceReport> data = teamProvinceReportMapper.findAll(teamProvinceReportQueryModel);
        Page<TeamProvinceReport> page = new Page<>();
        page.setPageNumber(teamProvinceReportQueryModel.getPageNumber());
        page.setPageSize(teamProvinceReportQueryModel.getPageSize());
        page.setData(data);
        page.setTotal(total);
        return page;
    }

    @Override
    public List<TeamProvinceReport> findExReport(TeamProvinceReportQueryModel teamProvinceReportQueryModel) {
        return teamProvinceReportMapper.findAll(teamProvinceReportQueryModel);
    }

    @Override
    public void insert(TeamProvinceReport teamProvinceReport) {
        teamProvinceReportMapper.insert(teamProvinceReport);
    }
}
