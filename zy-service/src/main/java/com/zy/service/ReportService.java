package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.act.Report;
import com.zy.model.query.ReportQueryModel;

public interface ReportService {

	Report create(Report report);

	Page<Report> findPage(ReportQueryModel reportQueryModel);
}
