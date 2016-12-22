package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.act.ReportVisitedLog;
import com.zy.model.query.ReportVisitedLogQueryModel;


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