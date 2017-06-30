package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.tour.Sequence;
import com.zy.entity.tour.Tour;
import com.zy.model.query.TourQueryModel;

public interface TourService {
     void create(Sequence sequence);

     Sequence findSequenceOne(String seqName,String seqType);

     void updateSequence(Sequence sequence);

     Tour createTour(Tour tour);

     Page<Tour> findPageBy(TourQueryModel tourQueryModel);

     Tour findTourOne(Long id);

     void updatTour(Tour tour);
}
