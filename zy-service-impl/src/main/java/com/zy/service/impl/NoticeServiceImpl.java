package com.zy.service.impl;

import com.zy.common.model.query.Page;
import com.zy.entity.cms.Notice;
import com.zy.mapper.NoticeMapper;
import com.zy.model.query.NoticeQueryModel;
import com.zy.service.NoticeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

import static com.zy.common.util.ValidateUtils.validate;

@Service
@Validated
public class NoticeServiceImpl implements NoticeService {

	@Autowired
	private NoticeMapper noticeMapper;
	
	@Override
	public void delete(@NotNull Long id, Long userId){
		Notice notice = new Notice();
		notice.setId(id);
		notice.setUpdateId(userId);
		notice.setUpdateTime(new Date());
		notice.setStatus(0);
		noticeMapper.delete(notice);
	}
	
	@Override
	public Notice findOne(@NotNull Long id) {
		return noticeMapper.findOne(id);
	}
	
	@Override
	public Notice create(@NotNull Notice notice) {
		notice.setCreatedTime(new Date());
		validate(notice);
		noticeMapper.insert(notice);
		return notice;
	}
	
	@Override
	public Notice update(@NotNull Notice notice) {
		noticeMapper.update(notice);
		return notice;
	}
	
	@Override
	public Page<Notice> findPage(@NotNull NoticeQueryModel noticeQueryModel) {
		if(noticeQueryModel.getPageNumber() == null)
			noticeQueryModel.setPageNumber(0);
		if(noticeQueryModel.getPageSize() == null)
			noticeQueryModel.setPageSize(20);
		long total = noticeMapper.count(noticeQueryModel);
		List<Notice> data = noticeMapper.findAll(noticeQueryModel);
		Page<Notice> page = new Page<>();
		page.setPageNumber(noticeQueryModel.getPageNumber());
		page.setPageSize(noticeQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public List<Notice> findAll(@NotNull NoticeQueryModel noticeQueryModel) {
		return noticeMapper.findAll(noticeQueryModel);
	}

}
