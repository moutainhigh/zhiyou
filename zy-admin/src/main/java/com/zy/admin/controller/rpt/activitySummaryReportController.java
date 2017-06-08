package com.zy.admin.controller.rpt;

import com.zy.admin.controller.act.ActivityController;
import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.common.util.ExcelUtils;
import com.zy.common.util.WebUtils;
import com.zy.component.ActivityComponent;
import com.zy.entity.act.Activity;
import com.zy.model.query.ActivityQueryModel;
import com.zy.service.ActivityService;
import com.zy.vo.ActivitySummaryExReportVo;
import com.zy.vo.ActivitySummaryReportVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: Xuwq
 * Date: 2017/6/6.
 */
@Controller
@RequestMapping("/report/activitySummary")
public class activitySummaryReportController {

    Logger logger = LoggerFactory.getLogger(ActivityController.class);

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityComponent activityComponent;

    @RequiresPermissions("activitySummaryReport:view")
    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "rpt/activitySummaryReport";
    }

    @RequiresPermissions("activitySummaryReport:view")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Grid<ActivitySummaryReportVo> list(ActivityQueryModel activityQueryModel) {
        Page<Activity> page = activityService.findReport(activityQueryModel);
        Page<ActivitySummaryReportVo> voPage = PageBuilder.copyAndConvert(page, v -> activityComponent.buildActivitySummaryReportVo(v));
        return new Grid<>(voPage);
    }

    @RequiresPermissions("activitySummaryReport:export")
    @RequestMapping("/export")
    public String export(ActivityQueryModel activityQueryModel, HttpServletResponse response) throws IOException {

        activityQueryModel.setPageSize(null);
        activityQueryModel.setPageNumber(null);
        List<Activity> activity =  activityService.findExReport(activityQueryModel);
        String fileName = "活动汇总报表.xlsx";
        WebUtils.setFileDownloadHeader(response, fileName);

        List<ActivitySummaryExReportVo> activitySummaryExReportVo = activity.stream().map(activityComponent::buildActivitySummaryExReportVo).collect(Collectors.toList());
        OutputStream os = response.getOutputStream();
        ExcelUtils.exportExcel(activitySummaryExReportVo, ActivitySummaryExReportVo.class, os);

        return null;
    }
}
