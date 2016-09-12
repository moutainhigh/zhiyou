package com.zy.service;

import com.zy.entity.usr.Job;

import java.util.List;

public interface JobService {

    List<Job> findAll();

    Job findOne(Long id);
}
