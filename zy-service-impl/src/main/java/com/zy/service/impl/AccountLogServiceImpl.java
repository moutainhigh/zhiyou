package com.gc.service.impl;

import com.zy.common.model.query.Page;
import com.gc.entity.fnc.AccountLog;
import com.gc.mapper.AccountLogMapper;
import com.gc.model.query.AccountLogQueryModel;
import com.gc.service.AccountLogService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import java.util.List;

@Service
@Validated
public class AccountLogServiceImpl implements AccountLogService {

	Logger logger = LoggerFactory.getLogger(AccountLogServiceImpl.class);
	
	@Autowired
	private AccountLogMapper accountLogMapper;
	
	@Override
	public Page<AccountLog> findPage(@NotNull AccountLogQueryModel accountLogQueryModel) {
		if(accountLogQueryModel.getPageNumber() == null)
			accountLogQueryModel.setPageNumber(0);
		if(accountLogQueryModel.getPageSize() == null)
			accountLogQueryModel.setPageSize(20);
		long total = accountLogMapper.count(accountLogQueryModel);
		List<AccountLog> data = accountLogMapper.findAll(accountLogQueryModel);
		Page<AccountLog> page = new Page<>();
		page.setPageNumber(accountLogQueryModel.getPageNumber());
		page.setPageSize(accountLogQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public List<AccountLog> findAll(@NotNull AccountLogQueryModel accountLogQueryModel) {
		return accountLogMapper.findAll(accountLogQueryModel);
	}

}
