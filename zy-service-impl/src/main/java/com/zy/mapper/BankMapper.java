package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.fnc.Bank;
import com.zy.model.query.BankQueryModel;


public interface BankMapper {

	int insert(Bank bank);

	int update(Bank bank);

	int merge(@Param("bank") Bank bank, @Param("fields")String... fields);

	int delete(Long id);

	Bank findOne(Long id);

	List<Bank> findAll(BankQueryModel bankQueryModel);

	long count(BankQueryModel bankQueryModel);

}