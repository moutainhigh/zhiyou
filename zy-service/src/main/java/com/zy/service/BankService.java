package com.zy.service;

import java.util.List;

import com.zy.common.model.query.Page;
import com.zy.entity.fnc.Bank;
import com.zy.model.query.BankQueryModel;

public interface BankService {
	
	Bank findOne(Long id);
	
	Bank create(Bank bank);
	
	void delete(Long id);
	
	void update(Bank bank);

	List<Bank> findAll(BankQueryModel bankQueryModel);
	
	Page<Bank> findPage(BankQueryModel bankQueryModel);
}
