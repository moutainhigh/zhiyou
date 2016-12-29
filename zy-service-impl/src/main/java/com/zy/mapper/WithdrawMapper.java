package com.zy.mapper;


import com.zy.entity.fnc.Withdraw;
import com.zy.model.query.WithdrawQueryModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface WithdrawMapper {

	int insert(Withdraw withdraw);

	int update(Withdraw withdraw);

	int merge(@Param("withdraw") Withdraw withdraw, @Param("fields")String... fields);

	int delete(Long id);

	Withdraw findOne(Long id);

	List<Withdraw> findAll(WithdrawQueryModel withdrawQueryModel);

	long count(WithdrawQueryModel withdrawQueryModel);

	Withdraw findBySn(String sn);
}