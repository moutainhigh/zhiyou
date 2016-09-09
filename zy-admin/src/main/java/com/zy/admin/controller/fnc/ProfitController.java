package com.zy.admin.controller.fnc;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.component.ProfitComponent;
import com.zy.entity.fnc.Profit;
import com.zy.entity.fnc.Profit.ProfitType;
import com.zy.entity.usr.User;
import com.zy.model.query.ProfitQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.ProfitService;
import com.zy.service.UserService;
import com.zy.vo.ProfitAdminVo;

@RequestMapping("/profit")
@Controller
public class ProfitController {

    @Autowired
    @Lazy
    private ProfitService profitService;

    @Autowired
    private ProfitComponent profitComponent;

    @Autowired
    private UserService userService;

    @RequiresPermissions("profit:view")
    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("profitTypes", ProfitType.values());
        return "fnc/profitList";
    }

    @RequiresPermissions("profit:view")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Grid<ProfitAdminVo> list(ProfitQueryModel profitQueryModel, String userPhoneEQ, String userNicknameLK) {

        if (StringUtils.isNotBlank(userPhoneEQ) || StringUtils.isNotBlank(userNicknameLK)) {
        	UserQueryModel userQueryModel = new UserQueryModel();
        	userQueryModel.setPhoneEQ(userPhoneEQ);
        	userQueryModel.setNicknameLK(userNicknameLK);
        	
            List<User> users = userService.findAll(userQueryModel);
            if (users == null || users.size() == 0) {
                return new Grid<ProfitAdminVo>(PageBuilder.empty(profitQueryModel.getPageSize(), profitQueryModel.getPageNumber()));
            }
            Long[] userIds = users.stream().map(v -> v.getId()).toArray(Long[]::new);
            profitQueryModel.setUserIdIN(userIds);
        }
        Page<Profit> page = profitService.findPage(profitQueryModel);
        Page<ProfitAdminVo> adminVoPage = PageBuilder.copyAndConvert(page, profitComponent::buildAdminVo);
        return new Grid<ProfitAdminVo>(adminVoPage);
    }

}
