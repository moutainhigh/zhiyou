package com.zy.admin.controller.cms;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zy.common.exception.BizException;
import com.zy.common.model.query.Page;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.entity.cms.Feedback;
import com.zy.entity.cms.Feedback.FeedbackStatus;
import com.zy.model.query.FeedbackQueryModel;
import com.zy.service.FeedbackService;

@RequestMapping("/feedback")
@Controller
public class FeedbackController {

	@Autowired
	private FeedbackService feedbackService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "cms/feedbackList";
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<Feedback> list(FeedbackQueryModel feedbackQueryModel) {
		Page<Feedback> page = feedbackService.findPage(feedbackQueryModel);
		return new Grid<Feedback>(page);
	}
	
	@RequestMapping(value = "/solve", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> solve(@RequestParam Long id) {
		Feedback feedback = feedbackService.findOne(id);
		validate(feedback, NOT_NULL, "feedback is null, id = " + id);
		
		Feedback feedbackForMerge = new Feedback();
		feedbackForMerge.setId(id);
		feedbackForMerge.setFeedbackStatus(FeedbackStatus.客服已接手);
		feedbackService.merge(feedbackForMerge, "feedbackStatus");
		return new ResultBuilder<>().message("客服已接手").build();
	}
	
	@RequestMapping(value = "/reply", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> reply(@RequestParam Long id, String reply) {
		try{
			feedbackService.reply(id, reply);
		}catch (BizException e) {
			return new ResultBuilder<>().message(e.getMessage()).build();
		}
		return new ResultBuilder<>().message("审核成功").build();
	}
}
