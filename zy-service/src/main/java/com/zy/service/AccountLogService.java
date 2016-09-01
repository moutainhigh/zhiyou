package com.gc.service;


import java.util.List;

import com.zy.common.model.query.Page;
import com.gc.entity.fnc.AccountLog;
import com.gc.model.query.AccountLogQueryModel;

public interface AccountLogService {

	List<AccountLog> findAll(AccountLogQueryModel accountLogQueryModel);
	
	Page<AccountLog> findPage(AccountLogQueryModel accountLogQueryModel);
}
