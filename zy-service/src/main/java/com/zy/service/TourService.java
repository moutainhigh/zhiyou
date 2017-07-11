package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.tour.Sequence;
import com.zy.entity.tour.Tour;
import com.zy.entity.tour.TourTime;
import com.zy.entity.tour.TourUser;
import com.zy.entity.usr.UserInfo;
import com.zy.model.query.TourQueryModel;
import com.zy.model.query.TourTimeQueryModel;
import com.zy.model.query.TourUserQueryModel;

import java.math.BigDecimal;
import java.util.List;

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

     void deleteTour(Tour tour);

     void updateAuditStatus(Long id, boolean isSuccess, String revieweRemark, Long loginUserId);

     void reset(Long id, Long loginUserId);

     TourUser findTourUser(Long id);

     void modify(TourUser tourUser);

     List<Tour> findAllByTour(TourQueryModel tourQueryModel);

     List<TourTime> findTourTime(TourTimeQueryModel tourTimeQueryModel);

     TourTime findTourTimeOne(Long tourTimeid);

     void updateOrInster(UserInfo userInfo, TourUser tourUser);

     void addCarInfo(TourUser tourUser);

     void addInfo(Long id, Integer isJoin, BigDecimal amount, Long loginUserId);
}
