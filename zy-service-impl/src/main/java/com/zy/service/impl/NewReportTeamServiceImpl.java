package com.zy.service.impl;

import com.zy.entity.report.NewReportTeam;
import com.zy.mapper.NewReportTeamMapper;
import com.zy.model.query.NewReportTeamQueryModel;
import com.zy.service.NewReportTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Created by it001 on 2017-09-27.
 */
@Service
@Validated
public class NewReportTeamServiceImpl implements NewReportTeamService {

    @Autowired
    private NewReportTeamMapper newReportTeamMapper;
    @Override
    public List<NewReportTeam> findAll(NewReportTeamQueryModel newReportTeamQueryModel) {
        return newReportTeamMapper.findAll(newReportTeamQueryModel);
    }

    @Override
    public void insert(NewReportTeam newReportTeam) {
        newReportTeamMapper.insert(newReportTeam);
    }
}
