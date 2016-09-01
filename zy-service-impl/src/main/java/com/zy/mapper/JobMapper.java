package com.gc.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.gc.entity.usr.Job;


public interface JobMapper {

	int insert(Job job);

	int update(Job job);

	int merge(@Param("job") Job job, @Param("fields")String... fields);

	int delete(Long id);

	Job findOne(Long id);

	List<Job> findAll();

}