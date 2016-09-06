package com.zy.service.impl;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.zy.common.model.query.Page;
import com.zy.entity.fnc.Bank;
import com.zy.mapper.BankMapper;
import com.zy.model.query.BankQueryModel;
import com.zy.service.BankService;

@Validated
@Service
public class BankServiceImpl implements BankService {

	@Autowired
	private BankMapper bankMapper;
	
	@Override
	public Bank create(@NotNull Bank bank) {
		bank.setIsDeleted(false);
		validate(bank);
		bankMapper.insert(bank);
		return bank;
	}

	@Override
	public void delete(@NotNull Long id) {
		Bank bank = bankMapper.findOne(id);
		validate(bank, NOT_NULL, "bank id" + id + " not foud");
		bank.setIsDeleted(true);
		bankMapper.update(bank);

	}

	@Override
	public void update(@NotNull Bank bank) {
		Long id = bank.getId();
		validate(id, NOT_NULL, "id is null");
		Bank persistence = bankMapper.findOne(id);
		validate(bank, NOT_NULL, "bank id" + id + " not foud");

		persistence.setCode(bank.getCode());
		persistence.setName(bank.getName());
		bankMapper.update(persistence);
	}

	@Override
	public List<Bank> findAll(BankQueryModel bankQueryModel) {
		return bankMapper.findAll(bankQueryModel);
	}

	@Override
	public Page<Bank> findPage(BankQueryModel bankQueryModel) {
		if (bankQueryModel.getPageNumber() == null)
			bankQueryModel.setPageNumber(0);
		if (bankQueryModel.getPageSize() == null)
			bankQueryModel.setPageSize(20);
		long total = bankMapper.count(bankQueryModel);
		List<Bank> data = bankMapper.findAll(bankQueryModel);
		Page<Bank> page = new Page<>();
		page.setPageNumber(bankQueryModel.getPageNumber());
		page.setPageSize(bankQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public Bank findOne(@NotNull Long id) {
		return bankMapper.findOne(id);
	}

}
