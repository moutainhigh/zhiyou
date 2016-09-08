package com.zy.service.impl;

import com.zy.common.exception.BizException;
import com.zy.common.exception.ConcurrentException;
import com.zy.common.model.query.Page;
import com.zy.entity.act.Report;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.usr.User;
import com.zy.mapper.ReportMapper;
import com.zy.mapper.UserMapper;
import com.zy.model.BizCode;
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
		Long userId = report.getUserId();
		validate(userId, NOT_NULL, "user id is null");
		User user = userMapper.findOne(userId);
		validate(user, NOT_NULL, "user id " + userId + "  is not found");
		
		Date now = new Date();
		report.setCreatedTime(new Date());
		report.setVersion(0);
		report.setPreConfirmStatus(ConfirmStatus.待审核);
		report.setConfirmStatus(ConfirmStatus.待审核);
		report.setConfirmRemark(null);
		report.setConfirmedTime(null);
		report.setAppliedTime(now);
		report.setCreatedTime(now);
		report.setIsSettledUp(false);
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
			report.setConfirmStatus(ConfirmStatus.已通过);
			report.setConfirmRemark(confirmRemark);
		} else {
			report.setConfirmStatus(ConfirmStatus.未通过);
			report.setConfirmRemark(confirmRemark);
		}
		if (reportMapper.update(report) == 0) {
			throw new ConcurrentException();
		}
	}

	@Override
	public void settleUp(Long id) {
		// TODO
	}

	@Override
	public Report findOne(@NotNull Long id) {
		return reportMapper.findOne(id);
	}

	@Override
	public Report modify(@NotNull Report report) {
		Long id = report.getId();
		validate(id, NOT_NULL, "id is null");

		Report persistence = reportMapper.findOne(id);
		validate(persistence, NOT_NULL, "report id" + id + " not found");
		if(persistence.getConfirmStatus() == ConfirmStatus.已通过) {
			throw new BizException(BizCode.ERROR, "状态不匹配");
		}
		
		persistence.setAppliedTime(new Date());
		persistence.setConfirmedTime(null);
		persistence.setConfirmRemark(null);
		persistence.setConfirmStatus(ConfirmStatus.待审核);
		persistence.setPreConfirmStatus(ConfirmStatus.待审核);
		persistence.setDate(report.getDate());
		persistence.setGender(report.getGender());
		persistence.setImage1(report.getImage1());
		persistence.setImage2(report.getImage2());
		persistence.setImage3(report.getImage3());
		persistence.setImage4(report.getImage4());
		persistence.setImage5(report.getImage5());
		persistence.setImage6(report.getImage6());
		persistence.setRealname(report.getRealname());
		persistence.setAge(report.getAge());
		persistence.setText(report.getText());
		persistence.setReportResult(report.getReportResult());
		reportMapper.update(persistence);
		return persistence;
	}

}
