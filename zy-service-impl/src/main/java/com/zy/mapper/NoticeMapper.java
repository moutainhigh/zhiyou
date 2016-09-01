package com.gc.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.gc.entity.cms.Notice;
import com.gc.model.query.NoticeQueryModel;


public interface NoticeMapper {

	int insert(Notice notice);

	int update(Notice notice);

	int merge(@Param("notice") Notice notice, @Param("fields")String... fields);

	int delete(Long id);

	Notice findOne(Long id);

	List<Notice> findAll(NoticeQueryModel noticeQueryModel);

	long count(NoticeQueryModel noticeQueryModel);

}