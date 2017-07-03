package com.zy.mapper;


import com.zy.entity.tour.BlackOrWhite;
import com.zy.model.query.BlackOrWhiteQueryModel;

import java.util.List;


public interface BlackOrWhiteMapper {

	int insert(BlackOrWhite blackOrWhite);

	int update(BlackOrWhite blackOrWhite);

	BlackOrWhite findOne(Long id);

	BlackOrWhite findByUserId(Long id);

	List<BlackOrWhite> findAll(BlackOrWhiteQueryModel blackOrWhiteQueryModel);

	long count(BlackOrWhiteQueryModel blackOrWhiteQueryModel);

}