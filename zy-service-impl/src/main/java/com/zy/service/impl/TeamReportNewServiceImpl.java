package com.zy.service.impl;

import com.zy.common.model.query.Page;
import com.zy.entity.report.TeamReportNew;
import com.zy.entity.report.UserSpread;
import com.zy.entity.sys.Area;
import com.zy.entity.usr.UserInfo;
import com.zy.mapper.*;
import com.zy.model.query.AreaQueryModel;
import com.zy.model.query.TeamReportNewQueryModel;
import com.zy.model.query.UserSpreadQueryModel;
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

    @Autowired
    private UserSpreadMapper userSpreadMapper;

    @Autowired
    private UserInfoMapper  userInfoMappe;

    @Autowired
    private AreaMapper areaMapper;

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

    /**
     * 查询地区的  id
     * @param
     * @return
     */
    @Override
    public List<Area> findAreaAll( AreaQueryModel areaQueryModel) {
     return  areaMapper.findAll(areaQueryModel);
    }

    @Override
    public void insertUserSpread(UserSpread userSpread) {
        userSpreadMapper.insert(userSpread);
    }

    @Override
    public List<Area> findParentAll() {
        AreaQueryModel areaQueryModel = new AreaQueryModel();
        areaQueryModel.setAreaTypeEQ(Area.AreaType.省);
        return areaMapper.findAll(areaQueryModel);
    }

    @Override
    public TeamReportNew findOne(Long teamReportNewId) {
        return teamReportNewMapper.findOne(teamReportNewId);
    }

    @Override
    public List<UserSpread> findUserSpread(UserSpreadQueryModel userSpreadQueryModel) {
        return userSpreadMapper.findAll(userSpreadQueryModel);
    }


}
