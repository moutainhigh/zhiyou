package com.zy.admin.controller.usr;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.ui.Grid;
import com.gc.component.PortraitComponent;
import com.gc.entity.usr.Portrait;
import com.gc.entity.usr.User;
import com.gc.model.query.PortraitQueryModel;
import com.gc.model.query.UserQueryModel;
import com.gc.service.PortraitService;
import com.gc.service.UserService;
import com.gc.vo.PortraitAdminVo;

@RequestMapping("/portrait")
@Controller
public class PortraitController {

	@Autowired
	private PortraitService portraitService;
	
	@Autowired
	private PortraitComponent portraitComponent;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "usr/portraitList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<PortraitAdminVo> list(PortraitQueryModel portraitQueryModel, String userPhoneEQ, String userNicknameLK) {
		
		if (StringUtils.isNotBlank(userPhoneEQ) || StringUtils.isNotBlank(userNicknameLK)) {
        	UserQueryModel userQueryModel = new UserQueryModel();
        	userQueryModel.setPhoneEQ(userPhoneEQ);
        	userQueryModel.setNicknameLK(userNicknameLK);
        	
            List<User> users = userService.findAll(userQueryModel);
            if (users == null || users.size() == 0) {
                return new Grid<PortraitAdminVo>(PageBuilder.empty(portraitQueryModel.getPageSize(), portraitQueryModel.getPageNumber()));
            }
            Long[] userIds = users.stream().map(v -> v.getId()).toArray(Long[]::new);
            //portraitQueryModel.setUserIdIN(userIds);
        }
		
		Page<Portrait> page = portraitService.findPage(portraitQueryModel);
		return new Grid<PortraitAdminVo>(PageBuilder.copyAndConvert(page, portraitComponent::buildAdminVo));
	}
}
