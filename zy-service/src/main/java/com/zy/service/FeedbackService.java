package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.cms.Feedback;
import com.zy.model.query.FeedbackQueryModel;

public interface FeedbackService {

	Page<Feedback> findPage(FeedbackQueryModel feedbackQueryModel);
	
	long count(FeedbackQueryModel feedbackQueryModel);
	
	Feedback findOne(Long id);
	
	void merge(Feedback feedback, String... fields);
	
	void create(Feedback feedback);
	
	void reply(Long id, String reply);
}
