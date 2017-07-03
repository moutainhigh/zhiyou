package com.zy.service.impl;

import com.sun.istack.internal.NotNull;
import com.zy.common.exception.ConcurrentException;
import com.zy.common.model.query.Page;
import com.zy.entity.tour.Sequence;
import com.zy.entity.tour.Tour;
import com.zy.entity.tour.TourUser;
import com.zy.mapper.SequenceMapper;
import com.zy.mapper.TourMapper;
import com.zy.mapper.TourUserMapper;
import com.zy.model.query.TourQueryModel;
import com.zy.model.query.TourUserQueryModel;
import com.zy.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

/**
 * Created by it001 on 2017/6/27.
 */
@Service
@Validated
public class TourServiceImpl implements TourService {

    @Autowired
    private SequenceMapper equenceMapper;

    @Autowired
    private TourMapper tourMapper;

    @Autowired
    private TourUserMapper tourUserMapper;
    /**
     * 保存新的seq
     * @param sequence
     * @return
     */
    @Override
    public void create(Sequence sequence) {
      equenceMapper.insert(sequence);
    }

    /**s
     * 查询 seq
     * @param seqName
     * @return
     */
    @Override
    public Sequence findSequenceOne(String seqName,String seqType) {
        Map<String,Object> map = new HashMap<>();
        map.put("seqName",seqName);
        map.put("seqType",seqType);
        return equenceMapper.findOne(map);
    }

    /**
     * 更新 seq
     * @param sequence
     */
    @Override
    public void updateSequence(Sequence sequence) {
        equenceMapper.update(sequence);
    }

    /**
     * 创建旅游信息
     * @param tour
     */
    @Override
    public Tour createTour(Tour tour) {
        tour.setCreatedTime(new Date());
        tourMapper.insert(tour);
        return tour;
    }

    /**
     * 查询 旅游信息
     * @param tourQueryModel
     * @return
     */
    @Override
    public Page<Tour> findPageBy(TourQueryModel tourQueryModel) {
        if(tourQueryModel.getPageNumber() == null)
            tourQueryModel.setPageNumber(0);
        if(tourQueryModel.getPageSize() == null)
            tourQueryModel.setPageSize(20);
        long total = tourMapper.count(tourQueryModel);
        List<Tour> data = tourMapper.findAll(tourQueryModel);
        Page<Tour> page = new Page<>();
        page.setPageNumber(tourQueryModel.getPageNumber());
        page.setPageSize(tourQueryModel.getPageSize());
        page.setData(data);
        page.setTotal(total);
        return page;
    }

    /**
     * 查询单个旅游信息
     * @param id
     * @return
     */
    @Override
    public Tour findTourOne(Long id) {
        return  tourMapper.findOne(id);
    }

    /**
     * 更新 旅游信息
     * @param tour
     */
    @Override
    public void updatTour(Tour tour) {
        Long id = tour.getId();
        Tour tourUp = tourMapper.findOne(id);
        validate(tourUp, NOT_NULL, "Tour id " + id + " not found");
        tourUp.setUpdateby(tour.getUpdateby());
        tourUp.setBrief(tour.getBrief());
        tourUp.setContent(tour.getContent());
        tourUp.setImage(tour.getImage());
        tourUp.setTitle(tour.getTitle());
        tourUp.setUpdateTime(new Date());
        tourMapper.update(tourUp);
    }

    /**
     * 查询所有旅客信息
     * @param tourUserQueryModel
     * @return
     */
    @Override
    public Page<TourUser> findAll(TourUserQueryModel tourUserQueryModel) {
        if(tourUserQueryModel.getPageNumber() == null)
            tourUserQueryModel.setPageNumber(0);
        if(tourUserQueryModel.getPageSize() == null)
            tourUserQueryModel.setPageSize(20);
        long total = tourUserMapper.count(tourUserQueryModel);
        List<TourUser> data = tourUserMapper.findAll(tourUserQueryModel);
        Page<TourUser> page = new Page<>();
        page.setPageNumber(tourUserQueryModel.getPageNumber());
        page.setPageSize(tourUserQueryModel.getPageSize());
        page.setData(data);
        page.setTotal(total);
        return page;
    }


    @Override
    public void updateAuditStatus(@NotNull Long id, boolean isSuccess, String revieweRemark, Long loginUserId) {
        TourUser tourUser = tourUserMapper.findOne(id);
        validate(tourUser, NOT_NULL, "report id " + id + " is not found");
        validate(tourUser.getAuditStatus() == 4, "已完成", "pre auditStatus error");
        if (tourUser.getAuditStatus() != 1){
            return;
        }

        tourUser.setUpdateBy(loginUserId);
        tourUser.setUpdateDate(new Date());

        if (isSuccess) {
            tourUser.setAuditStatus(4);
            tourUser.setRevieweRemark(revieweRemark);
        } else {
            tourUser.setAuditStatus(5);
            tourUser.setRevieweRemark(revieweRemark);
        }
        tourUserMapper.updateAuditStatus(tourUser);
    }


}
