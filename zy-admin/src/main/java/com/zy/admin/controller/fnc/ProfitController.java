package com.zy.admin.controller.fnc;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
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
import com.gc.component.ProfitComponent;
import com.gc.entity.fnc.Profit;
import com.gc.entity.usr.User;
import com.gc.model.Constants;
import com.gc.model.query.ProfitQueryModel;
import com.gc.model.query.UserQueryModel;
import com.gc.service.ProfitService;
import com.gc.service.UserService;
import com.gc.vo.ProfitAdminVo;

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
    List<String> values = Arrays.stream(Constants.ProfitBizName.class.getDeclaredFields()).map(f -> f.getName()).collect(toList());

    @RequiresPermissions("profit:view")
    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("profitTypes", values);
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
