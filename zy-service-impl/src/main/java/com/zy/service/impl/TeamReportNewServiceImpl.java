package com.zy.service.impl;

import com.zy.common.model.query.Page;
import com.zy.entity.report.TeamReportNew;
import com.zy.mapper.TeamReportNewMapper;
import com.zy.model.query.TeamReportNewQueryModel;
import com.zy.service.TeamReportNewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Created by it001 on 2017/8/25.
 */
@Service
@Validated
public class TeamReportNewServiceImpl implements TeamReportNewService {

    @Autowired
    private TeamReportNewMapper teamReportNewMapper;

    @Override
    public Page<TeamReportNew> findPage(TeamReportNewQueryModel teamReportNewQueryModel) {
        if (teamReportNewQueryModel.getPageNumber() == null)
            teamReportNewQueryModel.setPageNumber(0);
        if (teamReportNewQueryModel.getPageSize() == null)
            teamReportNewQueryModel.setPageSize(20);
        long total = teamReportNewMapper.count(teamReportNewQueryModel);
        List<TeamReportNew> data = teamReportNewMapper.findAll(teamReportNewQueryModel);
        Page<TeamReportNew> page = new Page<>();
        page.setPageNumber(teamReportNewQueryModel.getPageNumber());
        page.setPageSize(teamReportNewQueryModel.getPageSize());
        page.setData(data);
        page.setTotal(total);
        return page;
    }

    @Override
    public List<TeamReportNew> findExReport(TeamReportNewQueryModel teamReportNewQueryModel) {
       return teamReportNewMapper.findAll(teamReportNewQueryModel);
    }

    @Override
    public void insert(TeamReportNew teamReportNew) {
        teamReportNewMapper.insert(teamReportNew);
    }
}
