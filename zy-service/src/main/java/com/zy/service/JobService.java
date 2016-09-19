package com.zy.service;

import com.zy.entity.usr.Job;

import java.util.List;

public interface JobService {

	void create(Job job);
	
	void modify(Job job);
	
    List<Job> findAll();

    Job findOne(Long id);
}
