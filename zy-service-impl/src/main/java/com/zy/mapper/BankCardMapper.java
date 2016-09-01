package com.gc.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.gc.entity.fnc.BankCard;
import com.gc.model.query.BankCardQueryModel;


public interface BankCardMapper {

	int insert(BankCard bankCard);

	int update(BankCard bankCard);

	int merge(@Param("bankCard") BankCard bankCard, @Param("fields")String... fields);

	int delete(Long id);

	BankCard findOne(Long id);

	List<BankCard> findAll(BankCardQueryModel bankCardQueryModel);

	long count(BankCardQueryModel bankCardQueryModel);

	BankCard findByUserId(Long userId);

}