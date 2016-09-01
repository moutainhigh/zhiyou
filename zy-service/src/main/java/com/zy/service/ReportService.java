package com.gc.service;

import com.zy.common.model.query.Page;
import com.gc.entity.act.Report;
import com.gc.model.query.ReportQueryModel;

public interface ReportService {

	Report create(Report report);

	Page<Report> findPage(ReportQueryModel reportQueryModel);
}
