package com.zy.service.impl;

import static com.zy.common.util.ValidateUtils.validate;
import static com.zy.common.util.ValidateUtils.NOT_NULL;
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

	@Override
	public void create(@NotNull Job job) {
		validate(job);
		jobMapper.insert(job);
	}

	@Override
	public void modify(@NotNull Job job) {
		Long id = job.getId();
		validate(id, NOT_NULL, "id is null");
		
		Job persistence = jobMapper.findOne(id);
		validate(persistence, NOT_NULL, "job id" + id + " not found");
		
		jobMapper.update(job);
	}
}
