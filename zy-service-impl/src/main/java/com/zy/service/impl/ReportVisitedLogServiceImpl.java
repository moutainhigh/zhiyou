package com.zy.service.impl;

import com.zy.common.exception.ValidationException;
import com.zy.common.model.query.Page;
import com.zy.entity.act.Report;
import com.zy.entity.act.ReportVisitedLog;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.mapper.ReportMapper;
import com.zy.mapper.ReportVisitedLogMapper;
import com.zy.model.query.ReportVisitedLogQueryModel;
import com.zy.service.ReportVisitedLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

import static com.zy.common.util.ValidateUtils.*;
import static com.zy.entity.sys.ConfirmStatus.*;
import static com.zy.model.Constants.visitContinueCheckList;
import static com.zy.model.Constants.visitedSuccessList;

@Service
@Validated
public class ReportVisitedLogServiceImpl implements ReportVisitedLogService{

	@Autowired
	private ReportVisitedLogMapper reportVisitedLogMapper;

	@Autowired
	private ReportMapper reportMapper;

	@Override
	public ReportVisitedLog findOne(@NotNull Long id) {
		return reportVisitedLogMapper.findOne(id);
	}

	@Override
	public ReportVisitedLog findByReportId(@NotNull Long reportId) {
		return reportVisitedLogMapper.findByReportId(reportId);
	}

	@Override
	public void create(@NotNull ReportVisitedLog reportVisitedLog) {

		Long reportId = reportVisitedLog.getReportId();
		validate(reportId, NOT_NULL, "report id " + reportId + " is null");
		Report report = reportMapper.findOne(reportId);
		validate(report, NOT_NULL, "report id " + reportId + "not found");
		if(report.getPreConfirmStatus() != 已通过) {
			throw new ValidationException("pre confirm status error: " + report.getPreConfirmStatus());
		}
		if(report.getConfirmStatus() != 待审核) {
			return ;
		}
		ReportVisitedLog persistence = reportVisitedLogMapper.findByReportId(reportId);
		if (persistence != null) {
			return ;
		}

		String visitedStatus1 = reportVisitedLog.getVisitedStatus1();
		validate(visitedStatus1, NOT_BLANK, "visited status1 is blank");
		if(!visitContinueCheckList.contains(visitedStatus1)) {  //判断是否需要再次回访
			reportVisitedLog.setVisitedStatus(visitedStatus1);      //无需回访 设置最终回访状态
			if(StringUtils.isBlank(reportVisitedLog.getRemark())) {
				reportVisitedLog.setRemark(reportVisitedLog.getRemark());
			}
			ConfirmStatus confirmStatus = checkSuccessAndReturnStatus(visitedStatus1);  //是否回访成功
			reportVisitedLog.setConfirmStatus(confirmStatus);
			report.setConfirmStatus(confirmStatus);
			report.setConfirmRemark(visitedStatus1);
			if(confirmStatus == 已通过) {
				report.setConfirmedTime(new Date());
			}
			reportMapper.update(report);

		} else {
			reportVisitedLog.setConfirmStatus(待审核);
		}
		reportVisitedLogMapper.insert(reportVisitedLog);
	}

	@Override
	public void modify(@NotNull ReportVisitedLog reportVisitedLog) {

		Long id = reportVisitedLog.getId();
		ReportVisitedLog persistence = reportVisitedLogMapper.findOne(id);
		validate(persistence, NOT_NULL, "report visited log id " + id + " not found");
		if(persistence.getConfirmStatus() != 待审核) {
			return ;
		}

		Long reportId = reportVisitedLog.getReportId();
		validate(reportId, NOT_NULL, "report id " + reportId + " is null");
		Report report = reportMapper.findOne(reportId);
		validate(report, NOT_NULL, "report id " + reportId + "not found");
		if(report.getConfirmStatus() != 待审核) {
			return ;
		}

		String visitedStatus3 = reportVisitedLog.getVisitedStatus3();
		String visitedStatus2 = reportVisitedLog.getVisitedStatus2();
		if(StringUtils.isNotBlank(visitedStatus3)) {
			ConfirmStatus confirmStatus = checkSuccessAndReturnStatus(visitedStatus3);
			String confirmRemark = visitedStatus3;

			reportVisitedLog.setConfirmStatus(confirmStatus);
			reportVisitedLog.setVisitedStatus(confirmRemark);
			report.setConfirmStatus(confirmStatus);
			report.setConfirmRemark(confirmRemark);
			if(confirmStatus == 已通过) {
				report.setConfirmedTime(new Date());
			}

		} else if(StringUtils.isNotBlank(visitedStatus2)) {
			if(!visitContinueCheckList.contains(visitedStatus2)) {  //判断是否需要再次回访
				reportVisitedLog.setVisitedStatus(visitedStatus2);      //无需回访 设置最终回访状态
				if(StringUtils.isBlank(reportVisitedLog.getRemark())) {
					reportVisitedLog.setRemark(reportVisitedLog.getRemark());
				}
				ConfirmStatus confirmStatus = checkSuccessAndReturnStatus(visitedStatus2);  //是否回访成功
				reportVisitedLog.setConfirmStatus(confirmStatus);
				report.setConfirmStatus(confirmStatus);
				report.setConfirmRemark(visitedStatus2);
				if(confirmStatus == 已通过) {
					report.setConfirmedTime(new Date());
				}
				reportMapper.update(report);
			} else {
				reportVisitedLog.setConfirmStatus(待审核);
			}
		} else {
			throw new ValidationException("visited status3 and visited status2 all are blank");
		}

		reportVisitedLog.setCustomerServiceName1(persistence.getCustomerServiceName1());
		reportVisitedLog.setVisitedStatus1(persistence.getVisitedStatus1());
		reportVisitedLog.setVisitedTime1(persistence.getVisitedTime1());
		if(StringUtils.isNotBlank(persistence.getCustomerServiceName2())) {
			reportVisitedLog.setCustomerServiceName2(persistence.getCustomerServiceName2());
			reportVisitedLog.setVisitedStatus2(persistence.getVisitedStatus2());
			reportVisitedLog.setVisitedTime2(persistence.getVisitedTime2());
		}
		if(StringUtils.isNotBlank(persistence.getCustomerServiceName3())) {
			reportVisitedLog.setCustomerServiceName3(persistence.getCustomerServiceName3());
			reportVisitedLog.setVisitedStatus3(persistence.getVisitedStatus3());
			reportVisitedLog.setVisitedTime3(persistence.getVisitedTime3());
		}
		reportVisitedLogMapper.update(reportVisitedLog);
		reportMapper.update(report);
	}

	private ConfirmStatus checkSuccessAndReturnStatus(String visitedStatus) {
		if(visitedSuccessList.contains(visitedStatus)) {
			return 已通过;
		}
		return 未通过;
	}

	@Override
	public Page<ReportVisitedLog> findPage(ReportVisitedLogQueryModel reportVisitLogQueryModel) {
		if (reportVisitLogQueryModel.getPageNumber() == null)
			reportVisitLogQueryModel.setPageNumber(0);
		if (reportVisitLogQueryModel.getPageSize() == null)
			reportVisitLogQueryModel.setPageSize(20);
		long total = reportVisitedLogMapper.count(reportVisitLogQueryModel);
		List<ReportVisitedLog> data = reportVisitedLogMapper.findAll(reportVisitLogQueryModel);
		Page<ReportVisitedLog> page = new Page<>();
		page.setPageNumber(reportVisitLogQueryModel.getPageNumber());
		page.setPageSize(reportVisitLogQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public List<ReportVisitedLog> findAll(ReportVisitedLogQueryModel reportVisitLogQueryModel) {
		return reportVisitedLogMapper.findAll(reportVisitLogQueryModel);
	}
}
