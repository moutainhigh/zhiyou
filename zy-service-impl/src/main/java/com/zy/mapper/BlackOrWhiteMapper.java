package com.zy.mapper;


import com.zy.entity.act.Activity;
import com.zy.entity.tour.BlackOrWhite;
import com.zy.model.query.ActivityQueryModel;
import com.zy.model.query.BlackOrWhiteQueryModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface BlackOrWhiteMapper {

	int insert(BlackOrWhite blackOrWhite);

	int update(BlackOrWhite blackOrWhite);

	BlackOrWhite findOne(Long id);

	List<BlackOrWhite> findAll(BlackOrWhiteQueryModel blackOrWhiteQueryModel);

	long count(BlackOrWhiteQueryModel blackOrWhiteQueryModel);

}