package com.zy.service;

import com.zy.entity.report.NewReportTeam;
import com.zy.model.query.NewReportTeamQueryModel;

import java.util.List;

/**
 * Created by it001 on 2017-09-27.
 */
public interface NewReportTeamService {

    public List<NewReportTeam> findAll(NewReportTeamQueryModel newReportTeamQueryModel);

    void insert(NewReportTeam newReportTeam);
}
