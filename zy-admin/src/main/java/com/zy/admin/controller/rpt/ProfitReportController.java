package com.zy.admin.controller.rpt;

import com.zy.admin.controller.act.ActivityController;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.entity.usr.User;
import com.zy.model.query.UserQueryModel;
import com.zy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台收益
 * Author: Xuwq
 * Date: 2017/9/28.
 */
@Controller
@RequestMapping("/report/activitySummary")
public class ProfitReportController {
    Logger logger = LoggerFactory.getLogger(ActivityController.class);

    @Autowired
    private UserService userService;

    /**
     * 公司收益
     * @return
     */
    @RequestMapping(value = "/profit", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> profit() {
        //过滤大区非空特级
        List<User> v4Users = userService.findAll(UserQueryModel.builder().userRankEQ(User.UserRank.V4).build()).stream().filter(v -> v.getLargearea() != null).collect(Collectors.toList());


        return ResultBuilder.result(0);
    }

}
