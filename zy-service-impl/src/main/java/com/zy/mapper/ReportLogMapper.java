package com.zy.mapper;


import com.zy.entity.act.ReportLog;
import com.zy.model.query.ReportLogQueryModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ReportLogMapper {

	int insert(ReportLog reportLog);

	int update(ReportLog reportLog);

	int merge(@Param("reportLog") ReportLog reportLog, @Param("fields")String... fields);

	int delete(Long id);

	ReportLog findOne(Long id);

	List<ReportLog> findAll(ReportLogQueryModel reportLogQueryModel);

	long count(ReportLogQueryModel reportLogQueryModel);

}