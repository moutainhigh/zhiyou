package com.zy.admin.controller.rpt;

import com.zy.admin.controller.act.ActivityController;
import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.common.util.DateUtil;
import com.zy.entity.act.Activity;
import com.zy.entity.usr.User;
import com.zy.model.Principal;
import com.zy.model.query.ActivityQueryModel;
import com.zy.model.query.LargeAreaProfitQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.LargeAreaProfitService;
import com.zy.service.UserService;
import com.zy.vo.ActivitySummaryReportVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 后台收益
 * Author: Xuwq
 * Date: 2017/9/28.
 */
@Controller
@RequestMapping("/report/profit")
public class ProfitReportController {
    Logger logger = LoggerFactory.getLogger(ActivityController.class);

    @Autowired
    private LargeAreaProfitService largeAreaProfitService;

    /**
     * 公司收益
     * @return
     */
    @RequestMapping(value = "/profit", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> profit() {
        LargeAreaProfitQueryModel largeAreaProfitQueryModel = new LargeAreaProfitQueryModel();
        Map<String ,Object> returnMap = largeAreaProfitService.findAll(largeAreaProfitQueryModel);
        return ResultBuilder.result(returnMap);
    }

    /**
     * 大区各个总裁收益
     * @return
     */
    @RequestMapping(value = "/largeAreaProfit", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> largeAreaProfit(String largeAreaName) {
        LargeAreaProfitQueryModel largeAreaProfitQueryModel = new LargeAreaProfitQueryModel();
        largeAreaProfitQueryModel.setLargeAreaNameEQ(largeAreaName);
        Map<String ,Object> returnMap = largeAreaProfitService.findByLargeAreaName(largeAreaProfitQueryModel);
        return ResultBuilder.result(returnMap);
    }
}
