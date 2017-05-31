package com.zy.admin.controller.act;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.component.ActivityTeamApplyComponent;
import com.zy.entity.act.Activity;
import com.zy.entity.act.ActivityTeamApply;
import com.zy.entity.usr.User;
import com.zy.model.query.ActivityQueryModel;
import com.zy.model.query.ActivityTeamApplyQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.ActivityService;
import com.zy.service.ActivityTeamApplyService;
import com.zy.service.UserService;
import com.zy.vo.ActivityTeamApplyAdminVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("/activityTeamApply")
@Controller
public class ActicityTeamApplyController {

	@Autowired
	private ActivityTeamApplyService activityTeamApplyService;

	@Autowired
	private UserService userService;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private ActivityTeamApplyComponent activityTeamApplyComponent;


	@RequiresPermissions("activityApply:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "act/activityTeamApplyList";
	}

	@RequiresPermissions("activityApply:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<ActivityTeamApplyAdminVo> list(ActivityTeamApplyQueryModel activityTeamApplyQueryModel, String activityTitleLK
										, String nicknameLK, String phoneEQ) {
		if (StringUtils.isNotBlank(activityTitleLK)) {
			Page<Activity> page = activityService.findPage(ActivityQueryModel.builder().titleLK(activityTitleLK).build());
			List<Activity> data = page.getData();
			if (data.isEmpty()) {
				return new Grid<ActivityTeamApplyAdminVo>(PageBuilder.empty(activityTeamApplyQueryModel.getPageSize(), activityTeamApplyQueryModel.getPageNumber()));
			}
			activityTeamApplyQueryModel.setActivityIdIN(data.stream().map(v -> v.getId()).toArray(Long[]::new));
		}

		if (StringUtils.isNotBlank(nicknameLK) || StringUtils.isNotBlank(phoneEQ)) {
			List<User> all = userService.findAll(UserQueryModel.builder().nicknameLK(nicknameLK).phoneEQ(phoneEQ).build());
			if (all.isEmpty()) {
				return new Grid<ActivityTeamApplyAdminVo>(PageBuilder.empty(activityTeamApplyQueryModel.getPageSize(), activityTeamApplyQueryModel.getPageNumber()));
			}
			activityTeamApplyQueryModel.setBuyerIdIN(all.stream().map(v -> v.getId()).toArray(Long[]::new));
		}
		Page<ActivityTeamApply> page = activityTeamApplyService.findPage(activityTeamApplyQueryModel);
		Page<ActivityTeamApplyAdminVo> voPage = PageBuilder.copyAndConvert(page, v -> activityTeamApplyComponent.buildAdminVo(v));
		return new Grid<>(voPage);
	}

}
