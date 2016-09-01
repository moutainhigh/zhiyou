package com.gc.service;

import java.util.List;

import com.zy.common.model.query.Page;
import com.gc.entity.cms.Notice;
import com.gc.model.query.NoticeQueryModel;

public interface NoticeService {

	void delete(Long id);
	Notice findOne(Long id);
	Notice create(Notice notice);
	Notice update(Notice notice);

	Page<Notice> findPage(NoticeQueryModel noticeQueryModel);
	
	List<Notice> findAll(NoticeQueryModel noticeQueryModel);
}
