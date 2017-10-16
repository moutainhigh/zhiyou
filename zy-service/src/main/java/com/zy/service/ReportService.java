package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.act.Report;
import com.zy.model.query.ReportQueryModel;

import java.util.List;

public interface ReportService {

	Report findOne(Long id);
	
	Report create(Report report);
	
	Report adminCreate(Report report);

	Report adminModify(Report report, Long userId);
	
	Report modify(Report report, Long userId);

	Page<Report> findPage(ReportQueryModel reportQueryModel);

	void preConfirm(Long id, boolean isSuccess, String confirmRemark,Long userId);

	List<Report> findAll(ReportQueryModel reportQueryModel);

	long count(ReportQueryModel reportQueryModel);
	
	void confirm(Long id, boolean isSuccess, Long userId, String confirmRemark);

	void settleUp(Long id);

	void checkReportResult(Long id, Long userId, Report.ReportResult reportResult);

	void visitUser(Long id, Long userId, Long aLong);

	Report findReport(ReportQueryModel reportQueryModel);
}
