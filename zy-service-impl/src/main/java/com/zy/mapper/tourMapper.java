package com.zy.mapper;


import com.zy.entity.tour.Sequence;
import com.zy.entity.tour.Tour;
import com.zy.model.query.TourQueryModel;

import java.util.List;


public interface TourMapper {
	int insert(Tour tour);

	List<Tour> findAll(TourQueryModel tourQueryModel);

	long count(TourQueryModel tourQueryModel);

	Tour findOne(Long id);

	void update(Tour tourUp);
}