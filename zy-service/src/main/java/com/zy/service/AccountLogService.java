package com.zy.service;


import java.util.List;

import com.zy.common.model.query.Page;
import com.zy.entity.fnc.AccountLog;
import com.zy.model.query.AccountLogQueryModel;

public interface AccountLogService {

	List<AccountLog> findAll(AccountLogQueryModel accountLogQueryModel);
	
	Page<AccountLog> findPage(AccountLogQueryModel accountLogQueryModel);
}
