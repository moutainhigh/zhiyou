package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.fnc.Transfer;
import com.zy.model.query.TransferQueryModel;

import java.util.List;

public interface TransferService {

	void transfer(Long id, String transferRemark);
	
	void offlineTransfer(Long id, String transferRemark);
	
	Transfer findOne(Long id);
	
	Page<Transfer> findPage(TransferQueryModel transferQueryModel);

	List<Transfer> findAll(TransferQueryModel transferQueryModel);
	
	long count(TransferQueryModel transferQueryModel);
}
