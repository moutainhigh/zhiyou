package com.zy.service.impl;

import com.zy.common.exception.BizException;
import com.zy.common.exception.ValidationException;
import com.zy.common.model.query.Page;
import com.zy.entity.act.Report;
import com.zy.entity.act.ReportVisitedLog;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.usr.User;
import com.zy.mapper.ReportMapper;
import com.zy.mapper.ReportVisitedLogMapper;
import com.zy.mapper.UserMapper;
import com.zy.model.BizCode;
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

	@Autowired
	private UserMapper userMapper;

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
			throw new ValidationException("preConfirm status error: " + report.getPreConfirmStatus());
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
		Long visitUserId = report.getVisitUserId();
		User user = userMapper.findOne(visitUserId);
		validate(user, NOT_NULL, "user id not " + visitUserId + "found");

		reportVisitedLog.setCustomerServiceName1(user.getNickname());
		validate(reportVisitedLog);
		reportVisitedLogMapper.insert(reportVisitedLog);
	}

	@Override
	public void modify(@NotNull ReportVisitedLog reportVisitedLog) {

		Long id = reportVisitedLog.getId();
		ReportVisitedLog persistence = reportVisitedLogMapper.findOne(id);
		validate(persistence, NOT_NULL, "report visited log id " + id + " not found");
		if(persistence.getConfirmStatus() == 已通过) {
			return ;
		}
		if(persistence.getConfirmStatus() != 待审核) {
			throw new ValidationException("report visited log confirm status error");
		}

		Long reportId = reportVisitedLog.getReportId();
		validate(reportId, NOT_NULL, "report id " + reportId + " is null");
		Report report = reportMapper.findOne(reportId);
		validate(report, NOT_NULL, "report id " + reportId + "not found");
		if(report.getPreConfirmStatus() != 已通过) {
			throw new ValidationException("preConfirm status error: " + report.getPreConfirmStatus());
		}
		if(report.getConfirmStatus() != 待审核) {
			return ;
		}
		Long visitUserId = report.getVisitUserId();
		User user = userMapper.findOne(visitUserId);
		validate(user, NOT_NULL, "user id not " + visitUserId + "found");

		String visitedStatus3 = reportVisitedLog.getVisitedStatus3();
		String visitedStatus2 = reportVisitedLog.getVisitedStatus2();
		if (persistence.getVisitedStatus2() == null) {
			if (StringUtils.isBlank(visitedStatus2)) {
				throw new BizException(BizCode.ERROR, "回访记录已进行到二访,二访回访结果为空");
			}
			if (reportVisitedLog.getVisitedTime2() == null) {
				throw new BizException(BizCode.ERROR, "回访记录已进行到二访,二访回访时间为空");
			}
			persistence.setCustomerServiceName2(user.getNickname());
			persistence.setVisitedStatus2(visitedStatus2);
			persistence.setVisitedTime2(reportVisitedLog.getVisitedTime2());
			if (!visitContinueCheckList.contains(visitedStatus2)) {  //判断是否需要再次回访

				ConfirmStatus confirmStatus = checkSuccessAndReturnStatus(visitedStatus2);  //是否回访成功
				persistence.setVisitedStatus(visitedStatus2);  //无需回访 设置最终回访状态
				persistence.setConfirmStatus(confirmStatus);

				report.setConfirmStatus(confirmStatus);
				report.setConfirmRemark(visitedStatus2);
				if(confirmStatus == 已通过) {
					report.setConfirmedTime(new Date());
				}

			} else {
				reportVisitedLog.setConfirmStatus(待审核);
			}
		} else {
			if (StringUtils.isBlank(visitedStatus3)) {
				throw new BizException(BizCode.ERROR, "回访记录已进行到三访,三访回访结果为空");
			}
			if (reportVisitedLog.getVisitedTime3() == null) {
				throw new BizException(BizCode.ERROR, "回访记录已进行到三访,三访回访时间为空");
			}

			ConfirmStatus confirmStatus = checkSuccessAndReturnStatus(visitedStatus3);
			String confirmRemark = visitedStatus3;

			persistence.setCustomerServiceName3(user.getNickname());
			persistence.setConfirmStatus(confirmStatus);
			persistence.setVisitedStatus(confirmRemark);
			persistence.setVisitedStatus3(visitedStatus3);
			persistence.setVisitedTime3(reportVisitedLog.getVisitedTime3());

			report.setConfirmStatus(confirmStatus);
			report.setConfirmRemark(confirmRemark);
			if(confirmStatus == 已通过) {
				report.setConfirmedTime(new Date());
			}
		}

		persistence.setRemark(reportVisitedLog.getRemark());
		persistence.setCause(reportVisitedLog.getCause());
		persistence.setCauseText(reportVisitedLog.getCauseText());
		persistence.setContactWay(reportVisitedLog.getContactWay());
		persistence.setContactWayText(reportVisitedLog.getContactWayText());
		persistence.setDrink(reportVisitedLog.getDrink());
		persistence.setExercise(reportVisitedLog.getExercise());
		persistence.setExerciseText(reportVisitedLog.getExerciseText());
		persistence.setFamilyHistory(reportVisitedLog.getFamilyHistory());
		persistence.setHealth(reportVisitedLog.getHealth());
		persistence.setHealthProduct(reportVisitedLog.getHealthProduct());
		persistence.setHealthProductText(reportVisitedLog.getHealthProductText());
		persistence.setHobby(reportVisitedLog.getHobby());
		persistence.setHobbyText(reportVisitedLog.getHobbyText());
		persistence.setInterferingFactors(reportVisitedLog.getInterferingFactors());
		persistence.setMonthlyCost(reportVisitedLog.getMonthlyCost());
		persistence.setMonthlyCostText(reportVisitedLog.getMonthlyCostText());
		persistence.setProductName(reportVisitedLog.getProductName());
		persistence.setProductSharing(reportVisitedLog.getProductSharing());
		persistence.setProductSharingText(reportVisitedLog.getProductSharingText());
		persistence.setRelationship(reportVisitedLog.getRelationship());
		persistence.setRelationshipText(reportVisitedLog.getRelationshipText());
		persistence.setRemark1(reportVisitedLog.getRemark1());
		persistence.setRemark2(reportVisitedLog.getRemark2());
		persistence.setRemark3(reportVisitedLog.getRemark3());
		persistence.setRestTimeLabel(reportVisitedLog.getRestTimeLabel());
		persistence.setRestTimeText(reportVisitedLog.getRestTimeText());
		persistence.setSickness(reportVisitedLog.getSickness());
		persistence.setSleepQuality(reportVisitedLog.getSleepQuality());
		persistence.setSleepQualityText(reportVisitedLog.getSleepQualityText());
		persistence.setSmoke(reportVisitedLog.getSmoke());
		persistence.setToAgent(reportVisitedLog.getToAgent());
		persistence.setVisitedInfo(reportVisitedLog.getVisitedInfo());
		validate(persistence);
		reportVisitedLogMapper.update(persistence);
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
