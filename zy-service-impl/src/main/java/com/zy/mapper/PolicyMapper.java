package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.act.Policy;
import com.zy.model.query.PolicyQueryModel;


public interface PolicyMapper {

	int insert(Policy policy);

	int update(Policy policy);

	int merge(@Param("policy") Policy policy, @Param("fields")String... fields);

	int delete(Long id);

	Policy findOne(Long id);

	List<Policy> findAll(PolicyQueryModel policyQueryModel);

	long count(PolicyQueryModel policyQueryModel);

}