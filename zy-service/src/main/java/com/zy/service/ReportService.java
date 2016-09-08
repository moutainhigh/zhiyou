package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.act.Report;
import com.zy.model.query.ReportQueryModel;

public interface ReportService {

	Report findOne(Long id);
	
	Report create(Report report);

	Report modify(Report report);
	
	void settleUp(Long id);
	
	Page<Report> findPage(ReportQueryModel reportQueryModel);

	void preConfirm(Long id, boolean isSuccess, String confirmRemark);
	
	void confirm(Long id, boolean isSuccess, String confirmRemark);
}
