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
import org.apache.http.ParseException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by liang on 2017/8/28.
 */
@Controller
@RequestMapping("/report/teamProvinceReport")
public class TeamProvinceReportController {

    @Autowired
    private TeamProvinceReportService teamProvinceReportService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private TeamProvinceReportComponent teamProvinceReportComponent;

    /**
     * get 查询 所有数据
     * @param model
     * @return
     * @throws ParseException
     */
    @RequiresPermissions("teamProvinceReport:view")
    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) throws ParseException {
        model.addAttribute("month", DateUtil.getMoth(new Date())-1);
        model.addAttribute("year",DateUtil.getYear(new Date()));
        model.addAttribute("areas",areaService.findAll(AreaQueryModel.builder().areaTypeEQ(Area.AreaType.省).build()));
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
    public Grid<TeamProvinceReportAdminVo> list(TeamProvinceReportQueryModel teamProvinceReportQueryModel) throws ParseException {
        if(teamProvinceReportQueryModel.getOrderBy() == null){
            teamProvinceReportQueryModel.setOrderBy("v4ActiveRank");
            teamProvinceReportQueryModel.setDirection(Direction.ASC);
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
    public String export(TeamProvinceReportQueryModel teamProvinceReportQueryModel, HttpServletResponse response) throws IOException, ParseException{
        teamProvinceReportQueryModel.setPageSize(null);
        teamProvinceReportQueryModel.setPageNumber(null);
        List<TeamProvinceReport> teamProvinceReports =  teamProvinceReportService.findExReport(teamProvinceReportQueryModel);
        String fileName = "省份服务商活跃报表.xlsx";
        WebUtils.setFileDownloadHeader(response, fileName);
        List<TeamProvinceReportExportVo> teamProvinceReportExportVos = teamProvinceReports.stream().map(teamProvinceReportComponent::buildTeamProvinceReportExportVo).collect(Collectors.toList());
        OutputStream os = response.getOutputStream();
        ExcelUtils.exportExcel(teamProvinceReportExportVos, TeamProvinceReportExportVo.class, os);
        return null;
    }

}
