package com.zy.mobile.controller.ucenter;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.component.NoticeComponent;
import com.zy.entity.cms.Notice;
import com.zy.entity.cms.Notice.NoticeType;
import com.zy.entity.usr.User;
import com.zy.model.Principal;
import com.zy.model.query.NoticeQueryModel;
import com.zy.service.NoticeService;
import com.zy.service.UserService;
import io.gd.generator.api.query.Direction;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 通知公告
 * @Author :liang
 */
@RequestMapping("/u/notice")
@Controller
public class NoticeController {

	Logger logger = LoggerFactory.getLogger(NoticeController.class);

	@Autowired
	private NoticeService noticeService;

	@Autowired
	private NoticeComponent noticeComponent;

	@Autowired
	private UserService userService;

	/**
	 * 通知公告列表
	 * @param model
	 * @param principal
     * @return
     */
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model , Principal principal) {
		User user = userService.findOne(principal.getUserId());
		NoticeQueryModel noticeQueryModel = new NoticeQueryModel();
		noticeQueryModel.setPageNumber(0);
		noticeQueryModel.setPageSize(10);
		noticeQueryModel.setDirection(Direction.DESC);
		noticeQueryModel.setOrderBy("createdTime");
		if(user.getUserRank() == User.UserRank.V0){
			noticeQueryModel.setNoticeTypeEQ(NoticeType.全体公告);
		}
		Page<Notice> page = noticeService.findPage(noticeQueryModel);
		model.addAttribute("page", PageBuilder.copyAndConvert(page, noticeComponent::buildListVo));
		model.addAttribute("timeLT", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		return "ucenter/notice/noticeList";
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Result<?> ajax(Model model, Principal principal, Date timeLT, @RequestParam(required = true) Integer pageNumber) {
		if (timeLT == null) {
			timeLT = new Date();
		}
		User user = userService.findOne(principal.getUserId());
		NoticeQueryModel noticeQueryModel = new NoticeQueryModel();
		noticeQueryModel.setPageNumber(pageNumber);
		noticeQueryModel.setPageSize(10);
		noticeQueryModel.setDirection(Direction.DESC);
		noticeQueryModel.setOrderBy("createdTime");
		if(user.getUserRank() == User.UserRank.V0){
			noticeQueryModel.setNoticeTypeEQ(NoticeType.全体公告);
		}
		Page<Notice> page = noticeService.findPage(noticeQueryModel);
		Map<String, Object> map = new HashMap<>();
		map.put("page", PageBuilder.copyAndConvert(page, noticeComponent::buildListVo));
		map.put("timeLT", DateFormatUtils.format(timeLT, "yyyy-MM-dd HH:mm:ss"));
		return ResultBuilder.result(map);
	}

	/**
	 * 通知公告详情
	 * @param id
	 * @param model
     * @return
     */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String detail(@PathVariable Long id, Model model) {
		Notice notice = noticeService.findOne(id);
		model.addAttribute("notice", noticeComponent.buildDetailVo(notice));
		return "ucenter/notice/noticeDetail";
	}

}
