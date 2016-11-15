package com.zy.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zy.entity.fnc.Deposit;
import com.zy.model.dto.DepositSumDto;
import com.zy.model.query.DepositQueryModel;


public interface DepositMapper {

	int insert(Deposit deposit);

	int update(Deposit deposit);

	int merge(@Param("deposit") Deposit deposit, @Param("fields")String... fields);

	int delete(Long id);

	Deposit findOne(Long id);

	List<Deposit> findAll(DepositQueryModel depositQueryModel);

	long count(DepositQueryModel depositQueryModel);

	Deposit findBySn(String sn);

	DepositSumDto depositSum(DepositQueryModel depositQueryModel);

}