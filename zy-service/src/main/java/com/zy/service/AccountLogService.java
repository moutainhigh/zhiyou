package com.zy.service;


import com.zy.common.model.query.Page;
import com.zy.entity.fnc.AccountLog;
import com.zy.model.query.AccountLogQueryModel;

import java.util.List;

public interface AccountLogService {

	List<AccountLog> findAll(AccountLogQueryModel accountLogQueryModel);
	
	Page<AccountLog> findPage(AccountLogQueryModel accountLogQueryModel);

	void acknowledge(Long id);
}
