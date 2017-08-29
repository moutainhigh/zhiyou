package com.zy.service;


import com.zy.common.model.query.Page;
import com.zy.entity.report.TeamProvinceReport;
import com.zy.model.query.TeamProvinceReportQueryModel;

import java.util.List;

public interface TeamProvinceReportService {

     Page<TeamProvinceReport> findPage(TeamProvinceReportQueryModel teamProvinceReportQueryModel);

     List<TeamProvinceReport> findExReport(TeamProvinceReportQueryModel teamProvinceReportQueryModel);

     void insert(TeamProvinceReport teamProvinceReport);

}
