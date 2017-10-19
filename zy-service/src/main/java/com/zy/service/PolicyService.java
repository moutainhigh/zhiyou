package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.act.Policy;
import com.zy.model.query.PolicyQueryModel;

import java.util.Date;
import java.util.List;

public interface PolicyService {

	Policy create(Policy policy);
	
	Policy findOne(Long id);

	void modifyValidTime(Long id, Date beginTime, Date endTime, Long userId);

	List<Policy> findAll(PolicyQueryModel policyQueryModel);
	
	Page<Policy> findPage(PolicyQueryModel policyQueryModel);

	void checkAndModify(Long id);

	void fail(Long id, Long userId);

	void updateProductNumber(String productNumber, boolean b);
}
