package com.zy.admin.controller.usr;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.component.UserInfoComponent;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserInfo;
import com.zy.model.query.UserInfoQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.UserInfoService;
import com.zy.service.UserService;
import com.zy.vo.UserInfoAdminVo;

@RequestMapping("/userInfo")
@Controller
public class UserInfoController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserInfoService userInfoService;
	
	@Autowired
	private UserInfoComponent userInfoComponent;
	
	@RequiresPermissions("userInfo:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("confirmStatuses", ConfirmStatus.values());
		return "usr/userInfoList";
	}
	
	@RequiresPermissions("userInfo:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<UserInfoAdminVo> list(UserInfoQueryModel userInfoQueryModel, String userPhoneEQ, String userNicknameLK) {
		
		if (StringUtils.isNotBlank(userPhoneEQ) || StringUtils.isNotBlank(userNicknameLK)) {
        	UserQueryModel userQueryModel = new UserQueryModel();
        	userQueryModel.setPhoneEQ(userPhoneEQ);
        	userQueryModel.setNicknameLK(userNicknameLK);
        	
            List<User> users = userService.findAll(userQueryModel);
            if (users == null || users.size() == 0) {
                return new Grid<UserInfoAdminVo>(PageBuilder.empty(userInfoQueryModel.getPageSize(), userInfoQueryModel.getPageNumber()));
            }
            Long[] userIds = users.stream().map(v -> v.getId()).toArray(Long[]::new);
            userInfoQueryModel.setUserIdIN(userIds);
        }
		
		Page<UserInfo> page = userInfoService.findPage(userInfoQueryModel);
		Page<UserInfoAdminVo> voPage = PageBuilder.copyAndConvert(page, userInfoComponent::buildAdminVo);
		return new Grid<>(voPage);
	}
	
	@RequiresPermissions("userInfo:confirm")
	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> confirm(@RequestParam Long id, @RequestParam boolean isSuccess, String confirmRemark) {
		userInfoService.confirm(id, isSuccess, confirmRemark);
		return ResultBuilder.ok("操作成功");
	}
}
