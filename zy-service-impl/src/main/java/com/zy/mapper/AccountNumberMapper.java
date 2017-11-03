package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.account.AccountNumber;
import com.zy.model.query.AccountNumberQueryModel;


public interface AccountNumberMapper {

	int insert(AccountNumber accountNumber);

	int update(AccountNumber accountNumber);

	int merge(@Param("accountNumber") AccountNumber accountNumber, @Param("fields")String... fields);

	int delete(Long id);

	AccountNumber findOne(Long id);

	List<AccountNumber> findAll(AccountNumberQueryModel accountNumberQueryModel);

	long count(AccountNumberQueryModel accountNumberQueryModel);

}