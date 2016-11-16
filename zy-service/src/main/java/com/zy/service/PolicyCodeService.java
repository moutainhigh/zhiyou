package com.zy.service;

import java.util.List;

import com.zy.common.model.query.Page;
import com.zy.entity.act.PolicyCode;
import com.zy.model.query.PolicyCodeQueryModel;

public interface PolicyCodeService {

	PolicyCode findOne(Long id);
	
	List<PolicyCode> createByBatchCode(String batchCode, Long time);
	
	List<PolicyCode> findAll(PolicyCodeQueryModel policyCodeQueryModel);
	
	Page<PolicyCode> findPage(PolicyCodeQueryModel policyCodeQueryModel);
}
