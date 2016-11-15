package com.zy.service;

import java.util.List;

import com.zy.common.model.query.Page;
import com.zy.entity.fnc.Deposit;
import com.zy.model.dto.DepositSumDto;
import com.zy.model.query.DepositQueryModel;

public interface DepositService {

	Deposit create(Deposit deposit);
	
	void success(Long id, String outerSn);

	void offlineSuccess(Long id, Long operatorId, String remark);
	
	void offlineFailure(Long id, Long operatorId, String remark);
	
	void modifyOffline(Long depositId, String offlineImage, String offlineMemo);
	
	void cancel(Long id);
	
	Deposit findOne(Long id);
	
	Deposit findBySn(String sn);

	long count(DepositQueryModel depositQueryModel);
	
	Page<Deposit> findPage(DepositQueryModel depositQueryModel);
	
	List<Deposit> findAll(DepositQueryModel depositQueryModel);
	
	DepositSumDto sum(DepositQueryModel depositQueryModel);
}
