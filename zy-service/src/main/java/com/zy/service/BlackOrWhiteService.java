package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.act.Activity;
import com.zy.entity.tour.BlackOrWhite;
import com.zy.model.query.ActivityQueryModel;
import com.zy.model.query.BlackOrWhiteQueryModel;

import java.util.List;

public interface BlackOrWhiteService {

	BlackOrWhite create(BlackOrWhite blackOrWhite);

	void modify(BlackOrWhite blackOrWhite);

	BlackOrWhite findOne(Long BlackOrWhiteId);

	Page<BlackOrWhite> findPage(BlackOrWhiteQueryModel blackOrWhiteQueryModel);
}
