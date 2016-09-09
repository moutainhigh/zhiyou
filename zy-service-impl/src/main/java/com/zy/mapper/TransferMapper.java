package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.fnc.Transfer;
import com.zy.model.query.TransferQueryModel;


public interface TransferMapper {

	int insert(Transfer transfer);

	int update(Transfer transfer);

	int merge(@Param("transfer") Transfer transfer, @Param("fields")String... fields);

	int delete(Long id);

	Transfer findOne(Long id);

	List<Transfer> findAll(TransferQueryModel transferQueryModel);

	long count(TransferQueryModel transferQueryModel);

}