package com.zy.service.impl;

import com.zy.entity.tour.Sequence;
import com.zy.mapper.SequenceMapper;
import com.zy.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by it001 on 2017/6/27.
 */
@Service
@Validated
public class TourServiceImpl implements TourService {

    @Autowired
    private SequenceMapper equenceMapper;

    /**
     * 保存新的seq
     * @param sequence
     * @return
     */
    @Override
    public Sequence create(Sequence sequence) {
        return null;
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
}
