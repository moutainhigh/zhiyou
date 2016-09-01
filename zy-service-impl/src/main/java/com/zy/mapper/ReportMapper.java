package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.act.Report;
import com.zy.model.query.ReportQueryModel;


public interface ReportMapper {

	int insert(Report report);

	int update(Report report);

	int merge(@Param("report") Report report, @Param("fields")String... fields);

	int delete(Long id);

	Report findOne(Long id);

	List<Report> findAll(ReportQueryModel reportQueryModel);

	long count(ReportQueryModel reportQueryModel);

	Report findByTaskItemId(Long taskItemId);

}