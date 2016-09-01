package com.zy.service.impl;

import com.zy.entity.usr.Job;
import com.zy.mapper.JobMapper;
import com.zy.service.JobService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import java.util.List;

/**
 * Created by freeman on 16/8/11.
 */
@Service
@Validated
public class JobServiceImpl implements JobService {
    @Autowired
    private JobMapper jobMapper;

    @Override
    public List<Job> findAll() {
        return jobMapper.findAll();
    }

    @Override
    public Job findOne(@NotNull Long jobId) {
        return jobMapper.findOne(jobId);
    }
}
