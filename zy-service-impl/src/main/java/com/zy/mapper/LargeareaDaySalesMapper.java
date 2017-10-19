package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.report.LargeareaDaySales;
import com.zy.model.query.LargeareaDaySalesQueryModel;


public interface LargeareaDaySalesMapper {

	int insert(LargeareaDaySales largeareaDaySales);

	int update(LargeareaDaySales largeareaDaySales);

	int merge(@Param("largeareaDaySales") LargeareaDaySales largeareaDaySales, @Param("fields")String... fields);

	int delete(Long id);

	LargeareaDaySales findOne(Long id);

	List<LargeareaDaySales> findAll(LargeareaDaySalesQueryModel largeareaDaySalesQueryModel);

	long count(LargeareaDaySalesQueryModel largeareaDaySalesQueryModel);

}