package com.zy.service.impl;

import com.zy.common.exception.ConcurrentException;
import com.zy.common.model.query.Page;
import com.zy.entity.act.Report;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.usr.User;
import com.zy.mapper.ReportMapper;
import com.zy.mapper.UserMapper;
import com.zy.model.query.ReportQueryModel;
import com.zy.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@Service
@Validated
public class ReportServiceImpl implements ReportService {

	@Autowired
	private ReportMapper reportMapper;

	@Autowired
	private UserMapper userMapper;

	@Override
	public Report create(@NotNull Report report) {
		report.setCreatedTime(new Date());
		report.setVersion(0);
		Long userId = report.getUserId();
		validate(userId, NOT_NULL, "user id is null");
		User user = userMapper.findOne(userId);
		validate(user, NOT_NULL, "user id " + userId + "  is not found");
		validate(report);
		reportMapper.insert(report);
		return report;
	}

	@Override
	public Page<Report> findPage(@NotNull ReportQueryModel reportQueryModel) {
		if (reportQueryModel.getPageNumber() == null)
			reportQueryModel.setPageNumber(0);
		if (reportQueryModel.getPageSize() == null)
			reportQueryModel.setPageSize(20);
		long total = reportMapper.count(reportQueryModel);
		List<Report> data = reportMapper.findAll(reportQueryModel);
		Page<Report> page = new Page<>();
		page.setPageNumber(reportQueryModel.getPageNumber());
		page.setPageSize(reportQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public void confirm(@NotNull Long id, boolean isSuccess, String confirmRemark) {
		Report report = reportMapper.findOne(id);
		validate(report, NOT_NULL, "report id " + id + " is not found");
		if (isSuccess) {
			report.setConfirmStatus(ConfirmStatus.审核通过);
			report.setConfirmRemark(confirmRemark);
		} else {
			report.setConfirmStatus(ConfirmStatus.审核未通过);
			report.setConfirmRemark(confirmRemark);
		}
		if (reportMapper.update(report) == 0) {
			throw new ConcurrentException();
		}
	}

}
