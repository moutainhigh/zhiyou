package com.zy.admin.controller.rpt;


import com.zy.component.LocalCacheComponent;
import com.zy.entity.usr.User;
import com.zy.vo.UserReportVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("/report/v4Tree")
@Controller
public class V4TreeReportController {

	@Autowired
	private LocalCacheComponent localCacheComponent;

	@RequiresPermissions("v4TreeReport:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "rpt/v4TreeReport";
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("v4TreeReport:view")
	public List<Map<String, Object>> listAjax(Long parentId) {
		List<UserReportVo> v4Users = localCacheComponent.getuserReportVos().stream().filter(v -> v.getUserRank() == User.UserRank.V4).collect(Collectors.toList());

		List<UserReportVo> users;
		if (parentId == null) {
			users = v4Users.stream().filter(v -> v.getV4UserId() == null).collect(Collectors.toList());
		} else {
			users = v4Users.stream().filter(v -> parentId.equals(v.getV4UserId())).collect(Collectors.toList());
		}

		return users.stream().map(user -> {
			Map<String, Object> map = new HashMap<>();

			User.UserRank userRank = user.getUserRank();
			if (userRank != null && userRank != User.UserRank.V0) {
				map.put("iconSkin", "rank" + userRank.getLevel());
			}

			map.put("id", user.getId());
			map.put("name", user.getNickname());
			map.put("isParent", v4Users.stream().filter(v -> user.getId().equals(v.getV4UserId())).findFirst().isPresent());
			return map;
		}).collect(Collectors.toList());
	}


	@RequestMapping("/report")
	@RequiresPermissions("v4TreeReport:view")
	public String orderStastics(Long userId, Model model) {
		return "redirect:/report/v4Order?userId=" + userId;
	}
}
