package com.zy.service;


import com.zy.common.model.query.Page;
import com.zy.entity.report.TeamReportNew;
import com.zy.model.query.TeamReportNewQueryModel;

import java.util.List;

public interface TeamReportNewService {

     Page<TeamReportNew> findPage(TeamReportNewQueryModel teamReportNewQueryModel);

     List<TeamReportNew> findExReport(TeamReportNewQueryModel teamReportNewQueryModel);

     void insert(TeamReportNew teamReportNew);

}
