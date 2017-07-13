package com.zy.service.impl;

import com.zy.common.exception.BizException;
import com.zy.common.exception.ValidationException;
import com.zy.common.model.query.Page;
import com.zy.entity.act.Policy;
import com.zy.entity.act.PolicyCode;
import com.zy.entity.act.Report;
import com.zy.entity.usr.User;
import com.zy.extend.Producer;
import com.zy.mapper.PolicyCodeMapper;
import com.zy.mapper.PolicyMapper;
import com.zy.mapper.ReportMapper;
import com.zy.mapper.UserMapper;
import com.zy.model.BizCode;
import com.zy.model.query.PolicyQueryModel;
import com.zy.service.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static com.zy.common.util.ValidateUtils.*;
import static com.zy.model.Constants.TOPIC_POLICY_EXPIRE_SOON;

@Service
@Validated
public class PolicyServiceImpl implements PolicyService {

	@Autowired
	private PolicyMapper policyMapper;
	
	@Autowired
	private PolicyCodeMapper policyCodeMapper;
	
	@Autowired
	private ReportMapper ReportMapper;
	
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private Producer producer;

	@Override
	public Policy create(@NotNull Policy policy) {
		Long reportId = policy.getReportId();
		validate(reportId, NOT_NULL, "policy -> report id is null");
		Report report = ReportMapper.findOne(reportId);
		validate(report, NOT_NULL, "report id" + reportId +  " not found");
		
		Long userId = policy.getUserId();
		validate(userId, NOT_NULL, "policy -> user id is null");
		validate(userId, v -> v.equals(report.getUserId()), "policy user id not equals report user id");
		User user = userMapper.findOne(userId);
		validate(user, NOT_NULL, "policy -> user id " + userId + " not found");
		
		String code = policy.getCode();
		validate(code, NOT_BLANK, "code is null");
		PolicyCode policyCode = policyCodeMapper.findByCode(policy.getCode());
		if(policyCode == null) {
			throw new BizException(BizCode.ERROR, "保险单号不存在[" + policy.getCode() + "]");
		}
		if(policyCode.getIsUsed()) {
			throw new BizException(BizCode.ERROR, "保险单号已被使用[" + policy.getCode() + "]");
		}
		
		long count = policyMapper.count(PolicyQueryModel.builder().reportIdEQ(reportId).build());
		if(count > 0) {
			throw new BizException(BizCode.ERROR, "检测报告已被投保"); 
		}
		
		policy.setGender(report.getGender());
		policy.setPhone(report.getPhone());
		policy.setRealname(report.getRealname());
		policy.setUserId(report.getUserId());
		policy.setCreatedTime(new Date());
		policy.setReportId(reportId);
		policy.setPolicyStatus(Policy.PolicyStatus.审核中);
		policy.setVersion(0);
		validate(policy);
		if(policyMapper.insert(policy) > 0) {
			policyCode.setIsUsed(true);
			policyCode.setUsedTime(new Date());
			policyCodeMapper.update(policyCode);
		}

		return policy;
	}

	@Override
	public List<Policy> findAll(@NotNull PolicyQueryModel policyQueryModel) {
		return policyMapper.findAll(policyQueryModel);
	}

	@Override
	public Page<Policy> findPage(@NotNull PolicyQueryModel policyQueryModel) {
		if (policyQueryModel.getPageNumber() == null)
			policyQueryModel.setPageNumber(0);
		if (policyQueryModel.getPageSize() == null)
			policyQueryModel.setPageSize(20);
		long total = policyMapper.count(policyQueryModel);
		List<Policy> data = policyMapper.findAll(policyQueryModel);
		Page<Policy> page = new Page<>();
		page.setPageNumber(policyQueryModel.getPageNumber());
		page.setPageSize(policyQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public Policy findOne(@NotNull Long id) {
		return policyMapper.findOne(id);
	}

	@Override
	public void modifyValidTime(@NotNull Long id , @NotNull Date beginTime, @NotNull Date endTime) {
		Policy policy = findOne(id);
		validate(policy, NOT_NULL, "policy id " + id + " not found");
		Policy.PolicyStatus policyStatus = policy.getPolicyStatus();
		if (policyStatus == Policy.PolicyStatus.已生效) {
			return;
		}
		if (policyStatus != Policy.PolicyStatus.审核中) {
			throw new BizException(BizCode.ERROR, "policy status error: " + policyStatus);
		}

		if(beginTime.after(endTime) || beginTime.equals(endTime)) {
			 throw new ValidationException("保险单生效时间异常, 结束时间应大于起始时间");
		}
		if (endTime.before(new Date())) {
			throw new ValidationException("保险单生效时间异常, 结束时间应大于当前时间");
		}

		policy.setPolicyStatus(Policy.PolicyStatus.已生效);
		policy.setValidTimeBegin(beginTime);
		policy.setValidTimeEnd(endTime);
		policyMapper.merge(policy, "policyStatus", "validTimeBegin", "validTimeEnd");
	}

	@Override
	public void checkAndModify(@NotNull Long id) {
		Policy policy = findOne(id);
		validate(policy, NOT_NULL, "policy id " + id + " not found");
		if (policy.getPolicyStatus() == Policy.PolicyStatus.已到期) {
			return ;
		}
		if(policy.getPolicyStatus() != Policy.PolicyStatus.已生效) {
			throw new BizException(BizCode.ERROR, "policy status error");
		}

		Instant instant = policy.getValidTimeEnd().toInstant();
		ZoneId zone = ZoneId.systemDefault();
		LocalDateTime validTimeEnd = LocalDateTime.ofInstant(instant, zone);
		LocalDateTime now = LocalDateTime.now();
		if (validTimeEnd.isBefore(now)) {
			Policy merge = new Policy();
			merge.setId(id);
			merge.setPolicyStatus(Policy.PolicyStatus.已到期);
			policyMapper.merge(merge, "policyStatus");
		} else {
			LocalDateTime fifteenDaysAgo = validTimeEnd.minusDays(15);
			if(fifteenDaysAgo.isBefore(now)) {
				producer.send(TOPIC_POLICY_EXPIRE_SOON, id);
			}

		}
	}

	@Override
	public void fail(@NotNull Long id) {
		Policy policy = findOne(id);
		validate(policy, NOT_NULL, "policy id " + id + " not found");
		if (policy.getPolicyStatus() == Policy.PolicyStatus.未通过) {
			return ;
		}
		if (policy.getPolicyStatus() != Policy.PolicyStatus.审核中) {
			throw new BizException(BizCode.ERROR, "policy status error");
		}

		Policy merge = new Policy();
		merge.setId(id);
		merge.setPolicyStatus(Policy.PolicyStatus.未通过);
		policyMapper.merge(merge, "policyStatus");
	}


	/**
	 * 更新检测编号
	 * @param productNumber
	 * @param b
     */
	@Override
	public void updateProductNumber(String productNumber, boolean b) {
		PolicyCode policyCode = policyCodeMapper.findByCode(productNumber);
		if(policyCode!=null) {
			policyCode.setIsUsed(b);
			policyCode.setUsedTime(new Date());
			policyCodeMapper.update(policyCode);
		}
	}
}
