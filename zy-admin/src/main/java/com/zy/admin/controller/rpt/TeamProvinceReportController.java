package com.zy.admin.controller.rpt;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.common.util.DateUtil;
import com.zy.common.util.ExcelUtils;
import com.zy.common.util.WebUtils;
import com.zy.component.TeamProvinceReportComponent;
import com.zy.entity.report.TeamProvinceReport;
import com.zy.entity.sys.Area;
import com.zy.model.query.AreaQueryModel;
import com.zy.model.query.TeamProvinceReportQueryModel;
import com.zy.service.AreaService;
import com.zy.service.TeamProvinceReportService;
import com.zy.vo.TeamProvinceReportAdminVo;
import com.zy.vo.TeamProvinceReportExportVo;
import io.gd.generator.api.query.Direction;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.ParseException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by liang on 2017/8/28.
 */
@Controller
@RequestMapping("/report/teamProvinceReport")
public class TeamProvinceReportController {

    private static final String SPLIT_PATTERN = "-";

    @Autowired
    private TeamProvinceReportService teamProvinceReportService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private TeamProvinceReportComponent teamProvinceReportComponent;

    /**
     * get 查询数据
     * @param model
     * @return
     * @throws ParseException
     */
    @RequiresPermissions("teamProvinceReport:view")
    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model, String queryDate) throws ParseException {
        model.addAttribute("areas",areaService.findAll(AreaQueryModel.builder().areaTypeEQ(Area.AreaType.省).build()));
        if (StringUtils.isNotBlank(queryDate)){
            model.addAttribute("queryDate",queryDate);
        }else{
            model.addAttribute("queryDate",DateUtil.getYear(DateUtil.getBeforeMonthBegin(new Date(),-1,0))+SPLIT_PATTERN+DateUtil.getMothNum(DateUtil.getBeforeMonthBegin(new Date(),-1,0)));
        }
        model.addAttribute("queryDateLabels", getQueryTimeLabels());
        return "rpt/teamProvinceReportList";
    }


    /**
     *  get 查询 数据
     * @param teamProvinceReportQueryModel
     * @return
     * @throws ParseException
     */
    @RequiresPermissions("teamProvinceReport:view")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Grid<TeamProvinceReportAdminVo> list(TeamProvinceReportQueryModel teamProvinceReportQueryModel,String queryDate) throws ParseException {
        if(teamProvinceReportQueryModel.getOrderBy() == null){
            teamProvinceReportQueryModel.setOrderBy("v4ActiveRank");
            teamProvinceReportQueryModel.setDirection(Direction.ASC);
        }
        if (StringUtils.isNotBlank(queryDate)){
            String [] ym = queryDate.split(SPLIT_PATTERN);
            teamProvinceReportQueryModel.setYearEQ(Integer.valueOf(ym[0]));
            teamProvinceReportQueryModel.setMonthEQ(Integer.valueOf(ym[1]));
        }
        Page<TeamProvinceReport> page = teamProvinceReportService.findPage(teamProvinceReportQueryModel);
        Page<TeamProvinceReportAdminVo> voPage = PageBuilder.copyAndConvert(page, v -> teamProvinceReportComponent.buildTeamProvinceReportAdminVo(v));
        return new Grid<>(voPage);
     }

    /**
     * 导出报表
     * @param teamProvinceReportQueryModel
     * @param response
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @RequiresPermissions("teamProvinceReport:export")
    @RequestMapping("/export")
    public String export(TeamProvinceReportQueryModel teamProvinceReportQueryModel, HttpServletResponse response,@RequestParam String queryDate) throws IOException, ParseException{
        teamProvinceReportQueryModel.setPageSize(null);
        teamProvinceReportQueryModel.setPageNumber(null);
        if(teamProvinceReportQueryModel.getOrderBy() == null){
            teamProvinceReportQueryModel.setOrderBy("v4ActiveRank");
            teamProvinceReportQueryModel.setDirection(Direction.ASC);
        }
        if (StringUtils.isNotBlank(queryDate)){
            String [] ym = queryDate.split(SPLIT_PATTERN);
            teamProvinceReportQueryModel.setYearEQ(Integer.valueOf(ym[0]));
            teamProvinceReportQueryModel.setMonthEQ(Integer.valueOf(ym[1]));
        }
        List<TeamProvinceReport> teamProvinceReports =  teamProvinceReportService.findExReport(teamProvinceReportQueryModel);
        String fileName = "省份服务商活跃报表"+queryDate+".xlsx";
        WebUtils.setFileDownloadHeader(response, fileName);
        List<TeamProvinceReportExportVo> teamProvinceReportExportVos = teamProvinceReports.stream().map(teamProvinceReportComponent::buildTeamProvinceReportExportVo).collect(Collectors.toList());
        OutputStream os = response.getOutputStream();
        ExcelUtils.exportExcel(teamProvinceReportExportVos, TeamProvinceReportExportVo.class, os);
        return null;
    }

    private List<String> getQueryTimeLabels() {
        LocalDate begin = LocalDate.of(2016, 2, 1);
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        List<String> timeLabels = new ArrayList<>();
        for (LocalDate itDate = begin; itDate.isEqual(today) || itDate.isBefore(today); itDate = itDate.plusMonths(1)) {
            timeLabels.add(dateTimeFormatter.format(itDate));
        }
        Collections.reverse(timeLabels);
        return timeLabels;
    }

}
