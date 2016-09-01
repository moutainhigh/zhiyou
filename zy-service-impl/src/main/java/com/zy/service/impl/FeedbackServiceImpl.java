package com.gc.service.impl;

import com.zy.common.model.query.Page;
import com.gc.entity.cms.Feedback;
import com.gc.entity.cms.Feedback.FeedbackStatus;
import com.gc.mapper.FeedbackMapper;
import com.gc.model.query.FeedbackQueryModel;
import com.gc.service.FeedbackService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

@Service
@Validated
public class FeedbackServiceImpl implements FeedbackService {

	@Autowired
	private FeedbackMapper feedbackMapper;
	
	@Override
	public Page<Feedback> findPage(@NotNull FeedbackQueryModel feedbackQueryModel) {
		if(feedbackQueryModel.getPageNumber() == null)
			feedbackQueryModel.setPageNumber(0);
		if(feedbackQueryModel.getPageSize() == null)
			feedbackQueryModel.setPageSize(20);
		long total = feedbackMapper.count(feedbackQueryModel);
		List<Feedback> data = feedbackMapper.findAll(feedbackQueryModel);
		Page<Feedback> page = new Page<>();
		page.setPageNumber(feedbackQueryModel.getPageNumber());
		page.setPageSize(feedbackQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public Feedback findOne(@NotNull Long id) {
		return feedbackMapper.findOne(id);
	}

	@Override
	public void create(Feedback feedback) {
		validate(feedback, NOT_NULL, "feedback is null");
		feedback.setFeedbackStatus(FeedbackStatus.等待客服接手);
		feedback.setReply(null);
		feedback.setCreatedTime(new Date());
		validate(feedback);
		
		feedbackMapper.insert(feedback);
	}

	@Override
	public void reply(@NotNull Long id,@NotNull String reply) {
		
		Feedback feedback = feedbackMapper.findOne(id);
		validate(feedback, NOT_NULL, "feedback is null, id = " + id);
		
		Feedback feedbackForMerge = new Feedback();
		feedbackForMerge.setId(id);
		feedbackForMerge.setReply(reply);
		feedbackForMerge.setRepliedTime(new Date());
		feedbackForMerge.setFeedbackStatus(FeedbackStatus.已关闭);
		feedbackMapper.merge(feedbackForMerge, "reply", "repliedTime", "feedbackStatus");

	}

	@Override
	public void merge(@NotNull Feedback feedback,@NotNull String... fields) {
		feedbackMapper.merge(feedback, fields);
	}

	@Override
	public long count(@NotNull FeedbackQueryModel feedbackQueryModel) {
		return feedbackMapper.count(feedbackQueryModel);
	}

}
