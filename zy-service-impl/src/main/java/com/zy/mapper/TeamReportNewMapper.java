package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.report.TeamReportNew;
import com.zy.model.query.TeamReportNewQueryModel;


public interface TeamReportNewMapper {

	int insert(TeamReportNew teamReportNew);

	int update(TeamReportNew teamReportNew);

	int merge(@Param("teamReportNew") TeamReportNew teamReportNew, @Param("fields")String... fields);

	int delete(Long id);

	TeamReportNew findOne(Long id);

	List<TeamReportNew> findAll(TeamReportNewQueryModel teamReportNewQueryModel);

	long count(TeamReportNewQueryModel teamReportNewQueryModel);

}