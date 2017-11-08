package com.zy.service.impl;


import com.zy.common.model.query.Page;
import com.zy.common.util.DateUtil;
import com.zy.entity.mergeusr.MergeUserView;
import com.zy.entity.usr.User;
import com.zy.mapper.MergeUserViewMapper;
import com.zy.model.query.MergeUserViewQueryModel;
import com.zy.service.MergeUserViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Validated
public class MergeUserViewServiceImpl implements MergeUserViewService {

    @Autowired
    private MergeUserViewMapper mergeUserViewMapper;

    @Override
    public MergeUserView findOne(Long id) {
        return mergeUserViewMapper.findOne(id);
    }

    @Override
    public Page<MergeUserView> findPage(MergeUserViewQueryModel mergeUserViewQueryModel) {
        if(mergeUserViewQueryModel.getPageNumber() == null)
            mergeUserViewQueryModel.setPageNumber(0);
        if(mergeUserViewQueryModel.getPageSize() == null)
            mergeUserViewQueryModel.setPageSize(20);
        long total = mergeUserViewMapper.count(mergeUserViewQueryModel);
        List<MergeUserView> data = mergeUserViewMapper.findAll(mergeUserViewQueryModel);
        Page<MergeUserView> page = new Page<>();
        page.setPageNumber(mergeUserViewQueryModel.getPageNumber());
        page.setPageSize(mergeUserViewQueryModel.getPageSize());
        page.setData(data);
        page.setTotal(total);
        return page;
    }

    @Override
    public List<MergeUserView> findAll(MergeUserViewQueryModel mergeUserViewQueryModel) {
        return mergeUserViewMapper.findAll(mergeUserViewQueryModel);
    }

    @Override
    public long count(MergeUserViewQueryModel mergeUserViewQueryModel) {
        return mergeUserViewMapper.count(mergeUserViewQueryModel);
    }

    /**
     *新进特级
     * @param ids
     * @return
     */
    @Override
    public Map<String, Object> findNewSup(long[] ids) {
        Map<String,Object>dataMap = new HashMap<String,Object>();
        dataMap.put("remark","%改为V4%");
        dataMap.put("endTime", DateUtil.getBeforeMonthEnd(new Date(),1,0));
        dataMap.put("beginTime",DateUtil.getBeforeMonthBegin(new Date(),0,0));
        dataMap.put("parentIdIN",ids);
        List<MergeUserView>myuserList = mergeUserViewMapper.findSupAll(dataMap);
        dataMap.put("MY",myuserList);
        return dataMap;
    }

    /**
     * 统计查询 活跃人数
     * @param mergeUserViewQueryModel
     * @return
     */
    @Override
    public long countByActive(MergeUserViewQueryModel mergeUserViewQueryModel) {
        return mergeUserViewMapper.countByActive(mergeUserViewQueryModel);
    }

    /**
     * 查询  不活跃的人数
     * @param mergeUserViewQueryModel
     * @param flag
     * @return
     */
    @Override
    public Page<MergeUserView> findActive(MergeUserViewQueryModel mergeUserViewQueryModel, boolean flag) {
        mergeUserViewQueryModel.setRegisterTimeLT(DateUtil.getBeforeMonthEnd(new Date(),1,0));
        mergeUserViewQueryModel.setRegisterTimeGTE(DateUtil.getBeforeMonthBegin(new Date(),-2,0));
        List<MergeUserView> userRankList= mergeUserViewMapper.findByNotActive(mergeUserViewQueryModel);
        Page<MergeUserView> page = new Page<>();
//        if (flag) {
//            long total = mergeUserViewMapper.countByNotActive(userQueryModel);
//            page.setTotal(total);
//        }
        page.setPageNumber(mergeUserViewQueryModel.getPageNumber());
        page.setPageSize(mergeUserViewQueryModel.getPageSize());
        page.setData(userRankList);
        return page;
    }


}
