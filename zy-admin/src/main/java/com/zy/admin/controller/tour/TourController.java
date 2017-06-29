package com.zy.admin.controller.tour;

import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.entity.act.Activity;
import com.zy.entity.tour.BlackOrWhite;
import com.zy.entity.usr.User;
import com.zy.model.query.ActivityQueryModel;
import com.zy.model.query.BlackOrWhiteQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.UserService;
import com.zy.vo.ActivityAdminVo;
import com.zy.vo.ActivityTeamApplyAdminVo;
import com.zy.vo.BlackOrWhiteAdminVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by it001 on 2017/6/28.
 */
@RequestMapping("/tour")
@Controller
public class TourController {

    @Autowired
    private UserService userService;


    @RequiresPermissions("tourSetting:*")
    @RequestMapping(value = "/blackOrWhite" , method = RequestMethod.GET)
    public String list(Model model) {
        return "tour/blackOrWhiteList";
    }

    @RequiresPermissions("tourSetting:*")
    @RequestMapping(value = "/blackOrWhite" , method = RequestMethod.POST)
    @ResponseBody
    public Grid<BlackOrWhiteAdminVo> list(BlackOrWhiteQueryModel blackOrWhiteQueryModel, String nicknameLK, String phoneEQ) {

        if (StringUtils.isNotBlank(nicknameLK) || StringUtils.isNotBlank(phoneEQ)) {
            List<User> all = userService.findAll(UserQueryModel.builder().nicknameLK(nicknameLK).phoneEQ(phoneEQ).build());
            if (all.isEmpty()) {
                return new Grid<BlackOrWhiteAdminVo>(PageBuilder.empty(blackOrWhiteQueryModel.getPageSize(), blackOrWhiteQueryModel.getPageNumber()));
            }
            blackOrWhiteQueryModel.setUserIdIN(all.stream().map(v -> v.getId()).toArray(Long[]::new));
        }
//        Page<Activity> page = activityService.findPage(activityQueryModel);
 //       Page<ActivityAdminVo> voPage = PageBuilder.copyAndConvert(page, v -> activityComponent.buildAdminVo(v, false));
    //    return new Grid<>(voPage);
        return null;
    }

}
