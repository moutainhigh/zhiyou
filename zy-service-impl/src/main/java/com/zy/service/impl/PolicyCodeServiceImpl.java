package com.zy.service.impl;

import static com.zy.common.util.ValidateUtils.validate;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.zy.common.exception.BizException;
import com.zy.common.model.query.Page;
import com.zy.common.util.Identities;
import com.zy.entity.act.PolicyCode;
import com.zy.mapper.PolicyCodeMapper;
import com.zy.model.BizCode;
import com.zy.model.query.PolicyCodeQueryModel;
import com.zy.service.PolicyCodeService;

@Service
@Validated
public class PolicyCodeServiceImpl implements PolicyCodeService {

	@Autowired
	private PolicyCodeMapper policyCodeMapper;
	
	@Override
	public PolicyCode findOne(@NotNull Long id) {
		return policyCodeMapper.findOne(id);
	}

	@Override
	public List<PolicyCode> createByBatchCode(@NotBlank String batchCode, @NotNull Long time) {
		long count = policyCodeMapper.count(PolicyCodeQueryModel.builder().batchCodeEQ(batchCode).build());
		if(count > 0) {
			throw new BizException(BizCode.ERROR, "批次号[" + batchCode + "]已经存在");
		}
		
		PolicyCode policyCode = new PolicyCode();
		policyCode.setBatchCode(batchCode);
		policyCode.setCreatedTime(new Date());
		policyCode.setIsUsed(false);
		policyCode.setVersion(0);
		for(int i=0; i < time; i++) {
			policyCode.setCode(Identities.uuid());
			validate(policyCode);
			policyCodeMapper.insert(policyCode);
		}
		
		return policyCodeMapper.findAll(PolicyCodeQueryModel.builder().batchCodeEQ(batchCode).build());
	}

	@Override
	public List<PolicyCode> findAll(@NotNull PolicyCodeQueryModel policyCodeQueryModel) {
		return policyCodeMapper.findAll(policyCodeQueryModel);
	}

	@Override
	public Page<PolicyCode> findPage(@NotNull PolicyCodeQueryModel policyCodeQueryModel) {
		if (policyCodeQueryModel.getPageNumber() == null)
			policyCodeQueryModel.setPageNumber(0);
		if (policyCodeQueryModel.getPageSize() == null)
			policyCodeQueryModel.setPageSize(20);
		long total = policyCodeMapper.count(policyCodeQueryModel);
		List<PolicyCode> data = policyCodeMapper.findAll(policyCodeQueryModel);
		Page<PolicyCode> page = new Page<>();
		page.setPageNumber(policyCodeQueryModel.getPageNumber());
		page.setPageSize(policyCodeQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

}
