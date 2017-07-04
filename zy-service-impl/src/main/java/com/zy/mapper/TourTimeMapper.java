package com.zy.mapper;


import com.zy.entity.tour.TourTime;
import com.zy.model.query.TourTimeQueryModel;

import java.util.List;

public interface TourTimeMapper {
    int insert(TourTime tourTime);

    void update(TourTime tourTime);

    TourTime findOne(Long timeId);

    List<TourTime>findAll(TourTimeQueryModel tourTimeQueryModel);

    long count(TourTimeQueryModel tourTimeQueryModel);

    void delupdate(TourTimeQueryModel tourTimeQueryModel);

}