package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.tour.BlackOrWhite;
import com.zy.model.query.BlackOrWhiteQueryModel;

public interface BlackOrWhiteService {

	BlackOrWhite create(BlackOrWhite blackOrWhite);

	void modify(BlackOrWhite blackOrWhite);

	BlackOrWhite findOne(Long BlackOrWhiteId);

	void delete(Long BlackOrWhiteId);

	BlackOrWhite findByUserId(Long BlackOrWhiteId);

	Page<BlackOrWhite> findPage(BlackOrWhiteQueryModel blackOrWhiteQueryModel);
}
