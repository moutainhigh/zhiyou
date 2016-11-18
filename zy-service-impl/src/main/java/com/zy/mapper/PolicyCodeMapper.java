package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.act.PolicyCode;
import com.zy.model.query.PolicyCodeQueryModel;


public interface PolicyCodeMapper {

	int insert(PolicyCode policyCode);

	int update(PolicyCode policyCode);

	int merge(@Param("policyCode") PolicyCode policyCode, @Param("fields")String... fields);

	int delete(Long id);

	PolicyCode findOne(Long id);

	List<PolicyCode> findAll(PolicyCodeQueryModel policyCodeQueryModel);

	long count(PolicyCodeQueryModel policyCodeQueryModel);

	PolicyCode findByCode(String code);

}