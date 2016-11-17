package com.zy.service;

import java.util.List;

import com.zy.common.model.query.Page;
import com.zy.entity.act.Policy;
import com.zy.model.query.PolicyQueryModel;

public interface PolicyService {

	Policy create(Policy policy);
	
	Policy findOne(Long id);
	
	List<Policy> findAll(PolicyQueryModel policyQueryModel);
	
	Page<Policy> findPage(PolicyQueryModel policyQueryModel);
}
