package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.act.ReportVisitedLog;
import com.zy.model.query.ReportVisitedLogQueryModel;

import java.util.List;

public interface ReportVisitedLogService {

	ReportVisitedLog findOne(Long id);

	void create(ReportVisitedLog reportVisitedLog, Long userId);

	void modify(ReportVisitedLog reportVisitedLog, Long userId);

	ReportVisitedLog findByReportId(Long reportId);

	Page<ReportVisitedLog> findPage(ReportVisitedLogQueryModel reportVisitLogedQueryModel);

	List<ReportVisitedLog> findAll(ReportVisitedLogQueryModel reportVisitLogQueryModel);
}
