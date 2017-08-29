package com.zy.service;


import com.zy.common.model.query.Page;
import com.zy.entity.report.TeamReportNew;
import com.zy.entity.report.UserSpread;
import com.zy.entity.sys.Area;
import com.zy.model.query.AreaQueryModel;
import com.zy.model.query.TeamReportNewQueryModel;
import com.zy.model.query.UserSpreadQueryModel;

import java.util.List;

public interface TeamReportNewService {

     Page<TeamReportNew> findPage(TeamReportNewQueryModel teamReportNewQueryModel);

     List<TeamReportNew> findExReport(TeamReportNewQueryModel teamReportNewQueryModel);

     void insert(TeamReportNew teamReportNew);

     List<Area> findAreaAll( AreaQueryModel areaQueryModel);

     void insertUserSpread(UserSpread userSpread);

     List<Area> findParentAll();

     TeamReportNew findOne(Long teamReportNewId);

     List<UserSpread> findUserSpread(UserSpreadQueryModel userSpreadQueryModel);
}
