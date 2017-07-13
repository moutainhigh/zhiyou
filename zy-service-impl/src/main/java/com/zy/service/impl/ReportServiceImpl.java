package com.zy.service.impl;

import com.zy.Config;
import com.zy.common.exception.BizException;
import com.zy.common.exception.ConcurrentException;
import com.zy.common.model.query.Page;
import com.zy.component.ActComponent;
import com.zy.component.FncComponent;
import com.zy.entity.act.PolicyCode;
import com.zy.entity.act.Report;
import com.zy.entity.adm.Admin;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.fnc.Profit.ProfitType;
import com.zy.entity.fnc.Transfer;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.tour.TourUser;
import com.zy.entity.usr.User;
import com.zy.mapper.*;
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

@Service
@Validated
public class ReportServiceImpl implements ReportService {

	@Autowired
	private ReportMapper reportMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private AdminMapper adminMapper;

	@Autowired
	private FncComponent fncComponent;

	@Autowired
	private ActComponent actComponent;

	@Autowired
	private Config config;

	@Autowired
	private TourUserMapper tourUserMapper;

	@Autowired
	private PolicyCodeMapper policyCodeMapper;

	@Override
	public Report create(@NotNull Report report) {
		Long userId = report.getUserId();
		validate(userId, NOT_NULL, "user id is null");
		User user = userMapper.findOne(userId);
		validate(user, NOT_NULL, "user id " + userId + "  is not found");
		if (user.getUserType() != User.UserType.代理) {
			throw new BizException(BizCode.ERROR, "只有代理才能提交检测报告");
		}
		/*
		if (user.getUserRank() == null || user.getUserRank() == V0) {
			throw new BizException(BizCode.ERROR, "不具有提交检测报告的权限, 请升级代理等级");
		}
		*/

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
		actComponent.recordReportLog(id, "admin修改检测报告");
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
		actComponent.recordReportLog(id, "终审确认");
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

		Long productId = report.getProductId();
		if (productId == null) {
			return; // 防御性
		}

		Long userId = report.getUserId();
		User user = userMapper.findOne(userId);
		User.UserRank userRank = user.getUserRank();

		if (config.isOld(productId)) { // 旧产品, 按原有规则结算

//			String title = "数据奖,检测报告编号" + id;
//			Long topId = null; // 找到第一个特级代理
//			Long transferUserId = null; // transfer 给到的代理
//
//			Long parentId = user.getParentId();
//			if (parentId == null && userRank != User.UserRank.V4) {
//				//logger.error("上级为空暂不结算,编号" + id);
//				return; // 上级为空 暂不做结算
//			}
//
//			boolean hasTransfer = true;
//			if (userRank == User.UserRank.V4) {
//				/* 如果自己是特级代理 */
//				topId = userId;
//				hasTransfer = false;
//			} else {
//
//				boolean hitTransferUserId = false;
//				if (userRank != User.UserRank.V0) {
//					hitTransferUserId = true;
//					transferUserId = userId;
//				}
//
//
//				/* 否则递归查找 */
//				int times = 0;
//				while (parentId != null) {
//					if (times > 1000) {
//						throw new BizException(BizCode.ERROR, "循环引用");
//					}
//					User parent = userMapper.findOne(parentId);
//					if (parent.getUserType() != User.UserType.代理) {
//						logger.error("代理父级数据错误,parentId=" + parentId);
//						throw new BizException(BizCode.ERROR, "代理父级数据错误"); // 防御性校验
//					}
//					if (!hitTransferUserId && parent.getUserRank() != User.UserRank.V0) {
//						hitTransferUserId = true;
//						transferUserId = parentId;
//					}
//
//					if (parent.getUserRank() == User.UserRank.V4) {
//						topId = parentId;
//						break;
//					}
//					parentId = parent.getParentId();
//					times ++;
//				}
//
//				if (topId == null) {
//					//logger.error("特级代理为空暂不结算,编号" + id);
//					return; // 特级代理 暂不做结算
//				}
//
//				if (hitTransferUserId && transferUserId.equals(topId)) {
//					hasTransfer = false;
//				}
//			}
//
//
//
//			/* 全额给一个人 */
//			fncComponent.createProfit(topId, ProfitType.数据奖, id, title, CurrencyType.现金, new BigDecimal("18.00"), new Date(), null); // TODO	 写死
//
//			if (hasTransfer) {
//				fncComponent.createTransfer(topId, transferUserId, Transfer.TransferType.数据奖, id, title, CurrencyType.现金, new BigDecimal("15.00"), new Date());
//			}

			report.setIsSettledUp(true);
			if (reportMapper.update(report) == 0) {
				throw new ConcurrentException();
			}
		} else { // 新产品 结算不产生收益
			report.setIsSettledUp(true);
			if (reportMapper.update(report) == 0) {
				throw new ConcurrentException();
			}

		}
		actComponent.recordReportLog(id, "检测报告结算");
	}

	@Override
	public void checkReportResult(@NotNull Long id, @NotNull Report.ReportResult reportResult) {
		Report report = reportMapper.findOne(id);
		validate(report, NOT_NULL, "report id " + id + " not fount");

		if(report.getCheckReportResult() != null) {
			return;
		}
		if(report.getPreConfirmStatus() != ConfirmStatus.已通过) {
			throw new BizException(BizCode.ERROR);
		}
		if(report.getConfirmStatus() != ConfirmStatus.待审核) {
			throw new BizException(BizCode.ERROR);
		}
		report.setCheckReportResult(reportResult);
		reportMapper.update(report);
		actComponent.recordReportLog(id, "客服检测结果");
	}

	@Override
	public void visitUser(@NotNull Long id, @NotNull Long userId) {
		Report report = reportMapper.findOne(id);
		validate(report, NOT_NULL, "report id " + id + " not fount");
		validate(report, v -> v.getCheckReportResult() != null, "客服检测结果为空");

		Long visitUserId = report.getVisitUserId();
		if (visitUserId != null) {
			return;
		}
		if(report.getPreConfirmStatus() != ConfirmStatus.已通过) {
			throw new BizException(BizCode.ERROR);
		}
		if(report.getConfirmStatus() != ConfirmStatus.待审核) {
			throw new BizException(BizCode.ERROR);
		}

		User user = userMapper.findOne(userId);
		validate(user, NOT_NULL, "user id " + userId + " not found");

		Admin admin = adminMapper.findByUserId(userId);
		validate(admin, NOT_NULL, "admin user id " + userId + " not found");

		report.setVisitUserId(userId);
		reportMapper.update(report);
		actComponent.recordReportLog(id, "分配回访客服");
	}

	@Override
	public Report findOne(@NotNull Long id) {
		return reportMapper.findOne(id);
	}

	@Override
	public Report findReport(@NotNull ReportQueryModel reportQueryModel) {
		return reportMapper.findReport(reportQueryModel);
	}

	@Override
	public Report modify(@NotNull Report report) {
		Long id = report.getId();
		validate(id, NOT_NULL, "id is null");

		Report persistence = reportMapper.findOne(id);
		validate(persistence, NOT_NULL, "report id" + id + " not found");
		if (persistence.getConfirmStatus() != ConfirmStatus.未通过 && persistence.getPreConfirmStatus() != ConfirmStatus.待审核) {
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
		actComponent.recordReportLog(id, "用户修改检测报告");
		return persistence;
	}

	//添加 旅游信息 审核状态
	@Override
	public void preConfirm(@NotNull Long id, boolean isSuccess, String confirmRemark,Long userId) {
		Report report = reportMapper.findOne(id);
		validate(report, NOT_NULL, "report id" + id + " not found");
		if(report.getPreConfirmStatus() != ConfirmStatus.待审核) {
			return ;
		}
		if(report.getConfirmStatus() != ConfirmStatus.待审核) {
			throw new BizException(BizCode.ERROR, "状态不匹配");
		}
		//获取 用户旅游信息
		List<TourUser> tourUserList = tourUserMapper.findByReportId(id);
		if (tourUserList!=null&&!tourUserList.isEmpty()){
			TourUser tourUser = tourUserList.get(0);//之取到第一条 （原则上只有一条）
			tourUser.setUpdateDate(new Date());
			tourUser.setUpdateBy(userId);
			if (isSuccess){
				tourUser.setIsEffect(1);
			}else{//若果不通过 则将产品编号设置成可用
				if (report.getProductNumber()!=null){
					PolicyCode policyCode = policyCodeMapper.findByCode(report.getProductNumber());
					policyCode.setTourUsed(true);
					policyCodeMapper.update(policyCode);
				}

				tourUser.setIsEffect(0);
			}
			tourUserMapper.modify(tourUser);
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
		actComponent.recordReportLog(id, "预审核");
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
