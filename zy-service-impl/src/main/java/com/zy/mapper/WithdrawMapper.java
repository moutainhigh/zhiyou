package com.gc.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.gc.entity.fnc.Withdraw;
import com.gc.model.query.WithdrawQueryModel;


public interface WithdrawMapper {

	int insert(Withdraw withdraw);

	int update(Withdraw withdraw);

	int merge(@Param("withdraw") Withdraw withdraw, @Param("fields")String... fields);

	int delete(Long id);

	Withdraw findOne(Long id);

	List<Withdraw> findAll(WithdrawQueryModel withdrawQueryModel);

	long count(WithdrawQueryModel withdrawQueryModel);

}