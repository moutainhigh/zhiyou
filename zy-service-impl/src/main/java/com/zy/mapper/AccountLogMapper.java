package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.fnc.AccountLog;
import com.zy.model.query.AccountLogQueryModel;


public interface AccountLogMapper {

	int insert(AccountLog accountLog);

	int update(AccountLog accountLog);

	int merge(@Param("accountLog") AccountLog accountLog, @Param("fields")String... fields);

	int delete(Long id);

	AccountLog findOne(Long id);

	List<AccountLog> findAll(AccountLogQueryModel accountLogQueryModel);

	long count(AccountLogQueryModel accountLogQueryModel);

}