package com.zy.service;

import com.zy.entity.usr.Job;

import java.util.List;


/**
 * Created by freeman on 16/8/11.
 */
public interface JobService {

    List<Job> findAll();

    Job findOne(Long id);
}
