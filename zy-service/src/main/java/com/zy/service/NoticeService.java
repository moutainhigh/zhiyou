package com.zy.service;

import java.util.List;

import com.zy.common.model.query.Page;
import com.zy.entity.cms.Notice;
import com.zy.model.query.NoticeQueryModel;

public interface NoticeService {

	void delete(Long id);
	Notice findOne(Long id);
	Notice create(Notice notice);
	Notice update(Notice notice);

	Page<Notice> findPage(NoticeQueryModel noticeQueryModel);
	
	List<Notice> findAll(NoticeQueryModel noticeQueryModel);
}
