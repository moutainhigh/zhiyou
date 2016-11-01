package com.zy.service.impl;

import com.zy.common.exception.BizException;
import com.zy.common.exception.ConcurrentException;
import com.zy.common.model.query.Page;
import com.zy.component.FncComponent;
import com.zy.entity.act.Report;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.fnc.Profit.ProfitType;
import com.zy.entity.fnc.Transfer;
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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.zy.common.support.weixinpay.WeixinPayUtils.logger;
import static com.zy.common.util.ValidateUtils.*;
import static com.zy.entity.usr.User.UserRank.V0;

@Service
@Validated
public class ReportServiceImpl implements ReportService {

	@Autowired
	private ReportMapper reportMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private FncComponent fncComponent;

	@Override
	public Report create(@NotNull Report report) {
		Long userId = report.getUserId();
		validate(userId, NOT_NULL, "user id is null");
		User user = userMapper.findOne(userId);
		validate(user, NOT_NULL, "user id " + userId + "  is not found");
		if (user.getUserType() != User.UserType.代理) {
			throw new BizException(BizCode.ERROR, "只有代理才能提交检测报告");
		}
		if (user.getUserRank() == null || user.getUserRank() == V0) {
			throw new BizException(BizCode.ERROR, "不具有提交检测报告的权限, 请升级代理等级");
		}

		Date now = new Date();
		report.setVersion(0);
		report.setPreConfirmStatus(ConfirmStatus.待审核);
		report.setConfirmStatus(ConfirmStatus.待审核);
		report.setConfirmRemark(null);
		report.setConfirmedTime(null);
		report.setAppliedTime(now);
		report.setCreatedTime(now);
		report.setIsSettledUp(false);
		report.setIsHot(false);
		validate(report);
		
		String realname = report.getRealname();
		Integer times = report.getTimes();
		ReportQueryModel reportQueryModel = new ReportQueryModel();
		reportQueryModel.setRealnameEQ(realname);
		reportQueryModel.setPhoneEQ(report.getPhone());
		reportQueryModel.setTimesEQ(times);
		List<Report> reports = reportMapper.findAll(reportQueryModel);
		validate(reports, v -> v.isEmpty(), realname + "，第" + times + "次检测结果已经提交，请勿重复提交，谢谢！");
		
		reportMapper.insert(report);
		return report;
	}

	@Override
	public Report adminCreate(@NotNull Report report) {
		Long userId = report.getUserId();
		validate(userId, NOT_NULL, "user id is null");
		User user = userMapper.findOne(userId);
		validate(user, NOT_NULL, "user id " + userId + "  is not found");

		Date now = new Date();
		report.setVersion(0);
		report.setPreConfirmStatus(ConfirmStatus.已通过);
		report.setConfirmStatus(ConfirmStatus.已通过);
		report.setConfirmRemark("管理后台新增");
		report.setTimes(report.getTimes());
		report.setConfirmedTime(now);
		report.setPreConfirmedTime(now);
		report.setAppliedTime(now);
		report.setCreatedTime(now);
		report.setIsSettledUp(true);
		report.setIsHot(false);
		validate(report);
		
		String realname = report.getRealname();
		Integer times = report.getTimes();
		ReportQueryModel reportQueryModel = new ReportQueryModel();
		reportQueryModel.setRealnameEQ(realname);
		reportQueryModel.setPhoneEQ(report.getPhone());
		reportQueryModel.setTimesEQ(times);
		List<Report> reports = reportMapper.findAll(reportQueryModel);
		validate(reports, v -> v.isEmpty(), realname + "，第" + times + "次检测结果已经提交，请勿重复提交，谢谢！");
		
		reportMapper.insert(report);
		return report;
	}
	
	@Override
	public Report adminModify(@NotNull Report report) {
		Long id = report.getId();
		validate(id, NOT_NULL, "id is null");

		Report persistence = reportMapper.findOne(id);
		validate(persistence, NOT_NULL, "report id" + id + " not found");

		persistence.setImage(report.getImage());
		persistence.setAreaId(report.getAreaId());
		persistence.setJobId(report.getJobId());
		persistence.setTimes(report.getTimes());
		persistence.setGender(report.getGender());
		persistence.setRealname(report.getRealname());
		persistence.setPhone(report.getPhone());
		persistence.setAge(report.getAge());
		persistence.setText(report.getText());
		persistence.setReportResult(report.getReportResult());
		persistence.setReportedDate(report.getReportedDate());
		validate(persistence);
		
		String realname = report.getRealname();
		Integer times = report.getTimes();
		ReportQueryModel reportQueryModel = new ReportQueryModel();
		reportQueryModel.setRealnameEQ(realname);
		reportQueryModel.setPhoneEQ(report.getPhone());
		reportQueryModel.setTimesEQ(times);
		List<Report> reports = reportMapper.findAll(reportQueryModel);
		if(!reports.isEmpty()) {
			for(Report r : reports) {
				validate(r.getId(), v -> v.equals(id), realname + "，第" + times + "次检测结果已经提交，请勿重复提交，谢谢！");
			}
		}
		
		reportMapper.update(persistence);
		return persistence;
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
		validate(report.getPreConfirmStatus(), v -> v == ConfirmStatus.已通过, "pre confirm status error");
		if(report.getConfirmStatus() != ConfirmStatus.待审核) {
			return ;
		}

		report.setConfirmedTime(new Date());
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
	public void settleUp(@NotNull Long id) {
		Report report = reportMapper.findOne(id);
		validate(report, NOT_NULL, "report id " + id + " is not found");
		if (report.getIsSettledUp()) {
			return;
		}
		if (report.getConfirmStatus() != ConfirmStatus.已通过) {
			throw new BizException(BizCode.ERROR, "只有审核通过的报告才能结算");
		}

		Long userId = report.getUserId();
		User user = userMapper.findOne(userId);
		String title = "数据奖,检测报告编号" + id;
		Long topId = null; // 找到第一个特级代理
		if (user.getUserRank() == User.UserRank.V4) {
			/* 如果自己是特级代理 */
			topId = userId;
		} else {
			/* 否则递归查找 */
			Long parentId = user.getParentId();
			while (parentId != null) {
				User parent = userMapper.findOne(parentId);
				if (parent.getUserType() != User.UserType.代理) {
					logger.error("代理父级数据错误,parentId=" + parentId);
					throw new BizException(BizCode.ERROR, "代理父级数据错误"); // 防御性校验
				}
				if (parent.getUserRank() == User.UserRank.V4) {
					topId = parentId;
					break;
				}
				parentId = parent.getParentId();
			}
		}
		
		if (topId == null) {
			logger.error("代理父级数据错误,parentId=" + user.getParentId());
			throw new BizException(BizCode.ERROR, "结算失败,找不到特级代理");
		}

		/* 全额给一个人 */
		fncComponent.createProfit(topId, ProfitType.数据奖, id, title, CurrencyType.现金, new BigDecimal("18.00")); // TODO	 写死

		if (!topId.equals(userId)) {
			fncComponent.createTransfer(topId, userId, Transfer.TransferType.数据奖, id, title, CurrencyType.现金, new BigDecimal("15.00"));
		}

		report.setIsSettledUp(true);
		if (reportMapper.update(report) == 0) {
			throw new ConcurrentException();
		}

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
		if (persistence.getConfirmStatus() == ConfirmStatus.已通过) {
			throw new BizException(BizCode.ERROR, "状态不匹配");
		}

		persistence.setAppliedTime(new Date());
		persistence.setConfirmedTime(null);
		persistence.setPreConfirmedTime(null);
		persistence.setConfirmRemark(null);
		persistence.setConfirmStatus(ConfirmStatus.待审核);
		persistence.setPreConfirmStatus(ConfirmStatus.待审核);
		persistence.setGender(report.getGender());
		persistence.setRealname(report.getRealname());
		persistence.setAge(report.getAge());
		persistence.setImage(report.getImage());
		persistence.setText(report.getText());
		persistence.setReportResult(report.getReportResult());
		
		String realname = report.getRealname();
		Integer times = report.getTimes();
		ReportQueryModel reportQueryModel = new ReportQueryModel();
		reportQueryModel.setRealnameEQ(realname);
		reportQueryModel.setPhoneEQ(report.getPhone());
		reportQueryModel.setTimesEQ(times);
		List<Report> reports = reportMapper.findAll(reportQueryModel);
		if(!reports.isEmpty()) {
			for(Report r : reports) {
				validate(r.getId(), v -> v.equals(id), realname + "，第" + times + "次检测结果已经提交，请勿重复提交，谢谢！");
			}
		}
		validate(persistence);
		reportMapper.update(persistence);
		return persistence;
	}

	@Override
	public void preConfirm(@NotNull Long id, boolean isSuccess, String confirmRemark) {
		Report report = reportMapper.findOne(id);
		validate(report, NOT_NULL, "report id" + id + " not found");
		if(report.getPreConfirmStatus() != ConfirmStatus.待审核) {
			return ;
		}
		if(report.getConfirmStatus() != ConfirmStatus.待审核) {
			throw new BizException(BizCode.ERROR, "状态不匹配");
		}

		report.setPreConfirmedTime(new Date());
		if(isSuccess) {
			report.setPreConfirmStatus(ConfirmStatus.已通过);
			report.setConfirmRemark(confirmRemark);
		} else {
			validate(confirmRemark, NOT_BLANK, "confirm remark is null");
			report.setPreConfirmStatus(ConfirmStatus.未通过);
			report.setConfirmStatus(ConfirmStatus.未通过);
			report.setConfirmRemark(confirmRemark);
		}
		if (reportMapper.update(report) == 0) {
			throw new ConcurrentException();
		}

	}

	@Override
	public List<Report> findAll(@NotNull ReportQueryModel reportQueryModel) {
		return reportMapper.findAll(reportQueryModel);
	}

	@Override
	public long count(ReportQueryModel reportQueryModel) {
		return reportMapper.count(reportQueryModel);
	}

}
