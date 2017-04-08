package com.zy.mobile.controller;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.component.MatterComponent;
import com.zy.entity.cms.Matter;
import com.zy.entity.usr.User;
import com.zy.model.Principal;
import com.zy.model.query.MatterQueryModel;
import com.zy.service.MatterService;
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
 * 资源管理
 * @Author :Xuwq
 */
@Controller
@RequestMapping("/matter")
public class MatterController {

	final Logger logger = LoggerFactory.getLogger(MatterController.class);

	@Autowired
	private MatterComponent matterComponent;

	@Autowired
	private MatterService matterService;

	@Autowired
	private UserService userService;

	@RequestMapping
	public String index(Model model) {
		return "matter/matter";
	}

	/**
	 * 资源列表查询
	 * @return
     */
	@RequestMapping(value = "/matterList",method = RequestMethod.GET)
	public String list(Principal principal, @RequestParam int type , Model model) {
		MatterQueryModel matterQueryModel = new MatterQueryModel();
		User user = userService.findOne(principal.getUserId());
		matterQueryModel.setAuthority(user.getUserRank().getLevel());
		matterQueryModel.setType(type);
		matterQueryModel.setUserId(principal.getUserId());
		matterQueryModel.setPageNumber(0);
		matterQueryModel.setPageSize(10);
		matterQueryModel.setOrderBy("uploadTime");
		matterQueryModel.setDirection(Direction.DESC);
		Page<Matter> page = matterService.mobileFindPage(matterQueryModel);
		model.addAttribute("page", PageBuilder.copyAndConvert(page, matterComponent::buildMatterListVo));
		model.addAttribute("type", type);
		model.addAttribute("timeLT", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		return "matter/matterList";
	}

	/**
	 * 异步获取更多数据
	 * @param model
	 * @param principal
	 * @param timeLT
	 * @param type
	 * @param pageNumber
     * @return
     */
	@RequestMapping(value = "/matterList",method = RequestMethod.POST)
	@ResponseBody
	public Result<?> ajax(Model model, Principal principal, Date timeLT, @RequestParam int type , @RequestParam(required = true) Integer pageNumber) {
		if (timeLT == null) {
			timeLT = new Date();
		}
		MatterQueryModel matterQueryModel = new MatterQueryModel();
		User user = userService.findOne(principal.getUserId());
		matterQueryModel.setAuthority(user.getUserRank().getLevel());
		matterQueryModel.setType(type);
		matterQueryModel.setPageNumber(pageNumber);
		matterQueryModel.setPageSize(10);
		matterQueryModel.setOrderBy("uploadTime");
		matterQueryModel.setDirection(Direction.DESC);
		Page<Matter> page = matterService.mobileFindPage(matterQueryModel);

		Map<String, Object> map = new HashMap<>();
		map.put("page", PageBuilder.copyAndConvert(page, matterComponent::buildMatterListVo));
		map.put("timeLT", DateFormatUtils.format(timeLT, "yyyy-MM-dd HH:mm:ss"));

		return ResultBuilder.result(map);
	}

	/**
	 * 资源详情
	 * @param id
	 * @param model
     * @return
     */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String matterDetail(@PathVariable Long id, Principal principal, Model model) {
		MatterQueryModel matterQueryModel = new MatterQueryModel();
		matterQueryModel.setUserId(principal.getUserId());
		matterQueryModel.setId(id);
		Matter matter = matterService.findOne(matterQueryModel);
		//点击量 +1
		matterService.click(id);
		model.addAttribute("matter", matterComponent.buildMatterListVo(matter));
		if (matter.getType() == 1){
			return "matter/video";
		}else if (matter.getType() == 4){
			return "matter/audio";
		}
		return "";
	}

	/**
	 * 关注
	 * @param id
	 * @param principal
	 * @param model
     * @return
     */
	@RequestMapping(value = "/collect")
	@ResponseBody
	public Result<? > collect(Long id, Principal principal, Model model) {
		matterService.collect(id, principal.getUserId());
		return ResultBuilder.ok("ok");
	}

	/**
	 * 取消关注
	 * @param id
	 * @param principal
	 * @param model
     * @return
     */
	@RequestMapping("/uncollect")
	@ResponseBody
	public Result<?> uncollect(Long id, Principal principal, Model model) {
		matterService.uncollect(id, principal.getUserId());
		return ResultBuilder.ok("ok");
	}

	@RequestMapping("/clicke")
	@ResponseBody
	public Result<?> clicke(Long id, Principal principal, Model model) {
		MatterQueryModel matterQueryModel = new MatterQueryModel();
		matterQueryModel.setUserId(principal.getUserId());
		matterQueryModel.setId(id);
		Matter matter = matterService.findOne(matterQueryModel);
		//点击量 +1
		matterService.click(id);
		Map<String, Object> map = new HashMap<>();
		map.put("matter", matter);
		return ResultBuilder.result(map);
	}
}
