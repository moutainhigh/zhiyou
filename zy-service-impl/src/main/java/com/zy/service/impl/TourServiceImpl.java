package com.zy.service.impl;

import com.zy.common.model.query.Page;
import com.zy.entity.tour.Sequence;
import com.zy.entity.tour.Tour;
import com.zy.entity.tour.TourTime;
import com.zy.entity.tour.TourUser;
import com.zy.mapper.SequenceMapper;
import com.zy.mapper.TourMapper;
import com.zy.mapper.TourTimeMapper;
import com.zy.mapper.TourUserMapper;
import com.zy.model.query.TourQueryModel;
import com.zy.model.query.TourTimeQueryModel;
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
    private TourTimeMapper  tourTimeMapper;

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
     * 查询 旅游相关时间信息
     * @param tourTimeQueryModel
     * @return
     */
    @Override
    public Page<TourTime> findTourTimePage(TourTimeQueryModel tourTimeQueryModel) {
        if(tourTimeQueryModel.getPageNumber() == null)
            tourTimeQueryModel.setPageNumber(0);
        if(tourTimeQueryModel.getPageSize() == null)
            tourTimeQueryModel.setPageSize(20);
        long total = tourTimeMapper.count(tourTimeQueryModel);
        List<TourTime> data = tourTimeMapper.findAll(tourTimeQueryModel);
        Page<TourTime> page = new Page<>();
        page.setPageNumber(tourTimeQueryModel.getPageNumber());
        page.setPageSize(tourTimeQueryModel.getPageSize());
        page.setData(data);
        page.setTotal(total);
        return page;
}

    /**
     * 插入  旅游时间信息
     * @param tourTime
     */
    @Override
    public void createTourTime(TourTime tourTime) {
        tourTimeMapper.insert(tourTime);
    }


    /**
     *删除或下架 （更新出发时间信息）
     * @param tourTime
     */
    @Override
    public void updateTourTime(TourTime tourTime) {
        tourTime.setUpdateTime(new Date());
        tourTimeMapper.update(tourTime);
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


}
