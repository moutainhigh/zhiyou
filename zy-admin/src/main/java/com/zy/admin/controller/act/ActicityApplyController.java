package com.zy.admin.controller.act;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.component.ActivityApplyComponent;
import com.zy.component.ActivityComponent;
import com.zy.entity.act.Activity;
import com.zy.entity.act.ActivityApply;
import com.zy.entity.usr.User;
import com.zy.model.query.ActivityApplyQueryModel;
import com.zy.model.query.ActivityQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.ActivityApplyService;
import com.zy.service.ActivityService;
import com.zy.service.UserService;
import com.zy.vo.ActivityApplyAdminVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("/activityApply")
@Controller
public class ActicityApplyController {

	@Autowired
	private ActivityApplyService activityApplyService;

	@Autowired
	private UserService userService;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private ActivityApplyComponent activityApplyComponent;

	@Autowired
	private ActivityComponent activityComponent;

	@RequiresPermissions("activityApply:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "act/activityApplyList";
	}

	@RequiresPermissions("activityApply:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<ActivityApplyAdminVo> list(ActivityApplyQueryModel activityApplyQueryModel, String activityTitleLK
										, String nicknameLK, String phoneEQ, String payerNicknameLK, String payerPhoneEQ , String inviterNicknameLK, String inviterPhoneEQ) {
		if (StringUtils.isNotBlank(activityTitleLK)) {
			Page<Activity> page = activityService.findPage(ActivityQueryModel.builder().titleLK(activityTitleLK).build());
			List<Activity> data = page.getData();
			if (data.isEmpty()) {
				return new Grid<ActivityApplyAdminVo>(PageBuilder.empty(activityApplyQueryModel.getPageSize(), activityApplyQueryModel.getPageNumber()));
			}
			activityApplyQueryModel.setActivityIdIN(data.stream().map(v -> v.getId()).toArray(Long[]::new));
		}

		if (StringUtils.isNotBlank(nicknameLK) || StringUtils.isNotBlank(phoneEQ)) {
			List<User> all = userService.findAll(UserQueryModel.builder().nicknameLK(nicknameLK).phoneEQ(phoneEQ).build());
			if (all.isEmpty()) {
				return new Grid<ActivityApplyAdminVo>(PageBuilder.empty(activityApplyQueryModel.getPageSize(), activityApplyQueryModel.getPageNumber()));
			}
			activityApplyQueryModel.setUserIdIN(all.stream().map(v -> v.getId()).toArray(Long[]::new));
		}

		if (StringUtils.isNotBlank(payerNicknameLK) || StringUtils.isNotBlank(payerPhoneEQ)) {
			List<User> all = userService.findAll(UserQueryModel.builder().nicknameLK(payerNicknameLK).phoneEQ(payerPhoneEQ).build());
			if (all.isEmpty()) {
				return new Grid<ActivityApplyAdminVo>(PageBuilder.empty(activityApplyQueryModel.getPageSize(), activityApplyQueryModel.getPageNumber()));
			}
			activityApplyQueryModel.setPayerUserIdIN(all.stream().map(v -> v.getId()).toArray(Long[]::new));
		}

		if (StringUtils.isNotBlank(inviterNicknameLK) || StringUtils.isNotBlank(inviterPhoneEQ)) {
			List<User> all = userService.findAll(UserQueryModel.builder().nicknameLK(inviterNicknameLK).phoneEQ(inviterPhoneEQ).build());
			if (all.isEmpty()) {
				return new Grid<ActivityApplyAdminVo>(PageBuilder.empty(activityApplyQueryModel.getPageSize(), activityApplyQueryModel.getPageNumber()));
			}
			activityApplyQueryModel.setInviterIdIN(all.stream().map(v -> v.getId()).toArray(Long[]::new));
		}

		Page<ActivityApply> page = activityApplyService.findPage(activityApplyQueryModel);
		Page<ActivityApplyAdminVo> voPage = PageBuilder.copyAndConvert(page, v -> activityApplyComponent.buildAdminVo(v));
		return new Grid<>(voPage);
	}

	/**
	 * 后台操作免费报名
	 * @param activityId
	 * @param phone
     * @return
     */
	@RequiresPermissions("activityApply:edit")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> create(Long activityId, String phone) {
		try{
			String[] phones = phone.split(",");
			for(String p : phones) {
				User byPhone = userService.findByPhone(p);
				if (byPhone == null) {
					return ResultBuilder.error("手机号不存在");
				}
				activityApplyService.createAndPaid(activityId, byPhone.getId());
			}
			return ResultBuilder.ok("ok");
		} catch (Exception e) {
			return ResultBuilder.error(e.getMessage());
		}


	}

}
