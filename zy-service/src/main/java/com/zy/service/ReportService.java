package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.act.Report;
import com.zy.model.query.ReportQueryModel;

import java.util.List;

public interface ReportService {

	Report findOne(Long id);
	
	Report create(Report report);
	
	Report adminCreate(Report report);

	Report adminModify(Report report);
	
	Report modify(Report report);

	Page<Report> findPage(ReportQueryModel reportQueryModel);

	void preConfirm(Long id, boolean isSuccess, String confirmRemark);

	List<Report> findAll(ReportQueryModel reportQueryModel);

	long count(ReportQueryModel reportQueryModel);
	
	void confirm(Long id, boolean isSuccess, String confirmRemark);

	void settleUp(Long id);

	void checkReportResult(Long id, Report.ReportResult reportResult);

	void visitUser(Long id, Long userId);

	Report findReport(ReportQueryModel reportQueryModel);
}
