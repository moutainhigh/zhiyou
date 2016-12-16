package com.zy.mapper;


import com.zy.entity.act.ReportVisitedLog;
import com.zy.model.query.ReportVisitedLogQueryModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ReportVisitedLogMapper {

	int insert(ReportVisitedLog reportVisitedLog);

	int update(ReportVisitedLog reportVisitedLog);

	int merge(@Param("reportVisitedLog") ReportVisitedLog reportVisitedLog, @Param("fields")String... fields);

	int delete(Long id);

	ReportVisitedLog findOne(Long id);

	List<ReportVisitedLog> findAll(ReportVisitedLogQueryModel reportVisitedLogQueryModel);

	long count(ReportVisitedLogQueryModel reportVisitedLogQueryModel);

	ReportVisitedLog findByReportId(Long reportId);

}