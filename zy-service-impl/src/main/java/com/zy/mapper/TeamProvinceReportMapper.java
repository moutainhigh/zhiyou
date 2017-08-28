package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.report.TeamProvinceReport;
import com.zy.model.query.TeamProvinceReportQueryModel;


public interface TeamProvinceReportMapper {

	int insert(TeamProvinceReport teamProvinceReport);

	int update(TeamProvinceReport teamProvinceReport);

	int merge(@Param("teamProvinceReport") TeamProvinceReport teamProvinceReport, @Param("fields")String... fields);

	int delete(Long id);

	TeamProvinceReport findOne(Long id);

	List<TeamProvinceReport> findAll(TeamProvinceReportQueryModel teamProvinceReportQueryModel);

	long count(TeamProvinceReportQueryModel teamProvinceReportQueryModel);

}