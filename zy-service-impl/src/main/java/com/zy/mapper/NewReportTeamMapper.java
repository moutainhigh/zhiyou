package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.report.NewReportTeam;
import com.zy.model.query.NewReportTeamQueryModel;


public interface NewReportTeamMapper {

	int insert(NewReportTeam newReportTeam);

	int update(NewReportTeam newReportTeam);

	int merge(@Param("newReportTeam") NewReportTeam newReportTeam, @Param("fields")String... fields);

	int delete(Long id);

	NewReportTeam findOne(Long id);

	List<NewReportTeam> findAll(NewReportTeamQueryModel newReportTeamQueryModel);

	long count(NewReportTeamQueryModel newReportTeamQueryModel);

}