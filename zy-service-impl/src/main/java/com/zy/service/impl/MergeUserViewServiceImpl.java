package com.zy.service.impl;


import com.zy.common.model.query.Page;
import com.zy.entity.mergeusr.MergeUserView;
import com.zy.mapper.MergeUserViewMapper;
import com.zy.model.query.MergeUserViewQueryModel;
import com.zy.service.MergeUserViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

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


}
