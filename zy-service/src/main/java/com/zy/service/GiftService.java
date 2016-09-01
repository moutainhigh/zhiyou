package com.gc.service;

import java.util.List;

import com.zy.common.model.query.Page;
import com.gc.entity.mal.Gift;
import com.gc.model.query.GiftQueryModel;

public interface GiftService {

	void delete(Long id);
	Gift findOne(Long id);
	void create(Gift gift);
	Gift modify(Gift gift);
	void on(Long id, Boolean isOn);
	Page<Gift> findPage(GiftQueryModel giftQueryModel);
	List<Gift> findAll(GiftQueryModel giftQueryModel);
	
}
