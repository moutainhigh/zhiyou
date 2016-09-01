package com.gc.service;

import java.util.List;

import com.zy.common.model.query.Page;
import com.gc.entity.fnc.BankCard;
import com.gc.model.query.BankCardQueryModel;

public interface BankCardService {
	
	BankCard findOne(Long id);
	
	Page<BankCard> findPage(BankCardQueryModel bankCardQueryModel);

	List<BankCard> findByUserId(Long userId);
	
	BankCard create(BankCard bankCard);
	
	void delete(Long id);
	
	void update(BankCard bankCard);

	long count(BankCardQueryModel bankCardQueryModel);
	
	void confirm(Long id, boolean isSuccess, String confirmRemark);
}
