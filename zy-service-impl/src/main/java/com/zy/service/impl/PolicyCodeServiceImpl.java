package com.zy.service.impl;

import static com.zy.common.util.ValidateUtils.validate;

import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.zy.common.exception.BizException;
import com.zy.common.model.query.Page;
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
	public PolicyCode findByCode(String code) {
		return policyCodeMapper.findByCode(code);
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
			StringBuffer code = new StringBuffer("YJ");
			long count2 = 0;
			do {
				code = code.append(getPolicyCode());
				count2 = policyCodeMapper.count(PolicyCodeQueryModel.builder().codeEQ(code.toString()).build());
			} while (count2 > 0);
			policyCode.setCode(code.toString());
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

    private String getPolicyCode() {  
    	char[] codeSeq = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9' };
    	Random random = new Random();
    	int length = codeSeq.length;
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < 12; i++) {
			s.append(codeSeq[random.nextInt(length)]);
		}
		return s.toString();
    }

}
