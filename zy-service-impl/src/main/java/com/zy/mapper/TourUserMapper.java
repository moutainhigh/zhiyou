package com.zy.mapper;

import com.zy.entity.tour.TourUser;
import com.zy.model.query.TourUserQueryModel;

import java.util.List;

/**
 * Author: Xuwq
 * Date: 2017/6/30.
 */
public interface TourUserMapper {
    long count(TourUserQueryModel tourUserQueryModel);

    long listCount(TourUserQueryModel tourUserQueryModel);

    List<TourUser> findAll(TourUserQueryModel tourUserQueryModel);

    List<TourUser> findListAll(TourUserQueryModel tourUserQueryModel);

    TourUser findOne(Long id);

    void updateAuditStatus(TourUser tourUser);

    void reset(TourUser tourUser);

    void modify(TourUser tourUser);

    int insert(TourUser tourUser);

    void delete(Long id);

    List<TourUser> findJoinAll(TourUserQueryModel tourUserQueryModel);

    long joinCount(TourUserQueryModel tourUserQueryModel);

    List<TourUser> findByReportId(Long id);

    void updateVisitStatus(TourUser tourUser);

    void visitUser(TourUser tourUser);
}
