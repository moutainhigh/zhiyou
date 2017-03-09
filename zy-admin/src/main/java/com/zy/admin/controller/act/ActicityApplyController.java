package com.zy.admin.controller.act;


import com.zy.common.model.query.Page;
import com.zy.common.model.ui.Grid;
import com.zy.entity.act.ActivityApply;
import com.zy.model.query.ActivityApplyQueryModel;
import com.zy.service.ActivityApplyService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/activityApply")
@Controller
public class ActicityApplyController {

	@Autowired
	private ActivityApplyService activityApplyService;

	@RequiresPermissions("activityApply:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "act/activityApplyList";
	}

	@RequiresPermissions("activityApply:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<ActivityApply> list(ActivityApplyQueryModel activityApplyQueryModel) {
		Page<ActivityApply> page = activityApplyService.findPage(activityApplyQueryModel);
		return new Grid<>(page);
	}
}
