package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.tour.Sequence;
import com.zy.entity.tour.Tour;
import com.zy.entity.tour.TourTime;
import com.zy.entity.tour.TourUser;
import com.zy.model.query.TourQueryModel;
import com.zy.model.query.TourTimeQueryModel;
import com.zy.model.query.TourUserQueryModel;

public interface TourService {
     void create(Sequence sequence);

     Sequence findSequenceOne(String seqName,String seqType);

     void updateSequence(Sequence sequence);

     Tour createTour(Tour tour);

     Page<Tour> findPageBy(TourQueryModel tourQueryModel);

     Tour findTourOne(Long id);

     void updatTour(Tour tour);

     Page<TourTime> findTourTimePage(TourTimeQueryModel tourTimeQueryModel);

     void createTourTime(TourTime tourTime);

     void updateTourTime(TourTime tourTime);

     Page<TourUser> findAll(TourUserQueryModel tourUserQueryModel);
}
