package com.zy.admin.controller.rpt;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.common.util.DateUtil;
import com.zy.common.util.ExcelUtils;
import com.zy.common.util.WebUtils;
import com.zy.component.TeamReportNewComponent;
import com.zy.entity.report.TeamReportNew;
import com.zy.model.TeamReportVo;
import com.zy.model.query.TeamReportNewQueryModel;
import com.zy.service.SystemCodeService;
import com.zy.service.TeamReportNewService;
import com.zy.vo.TeamReportNewAdminVo;
import com.zy.vo.TeamReportNewExportVo;
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
 * Created by it001 on 2017/8/25.
 */
@Controller
@RequestMapping("/report/teamReportNew")
public class TeamReportNewController {

    @Autowired
    private TeamReportNewService teamReportNewService;

    @Autowired
    private TeamReportNewComponent teamReportNewComponent;

    @Autowired
    private SystemCodeService systemCodeService;
    /**
     * get 查询 所有数据
     * @param model
     * @param queryDate
     * @return
     * @throws ParseException
     */
    @RequiresPermissions("teamReportNew:view")
    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model, String queryDate) throws ParseException {
        model.addAttribute("month", DateUtil.getMoth(new Date())-1);
        model.addAttribute("year",DateUtil.getYear(new Date()));
        model.addAttribute("type",systemCodeService.findByType("LargeAreaType"));
        return "rpt/teamReportNewList";
    }


    /**
     *  get 查询 数据
     * @param model
     * @param teamReportNewQueryModel
     * @return
     * @throws ParseException
     */
    @RequiresPermissions("teamReportNew:view")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Grid<TeamReportNewAdminVo> list(Model model, TeamReportNewQueryModel teamReportNewQueryModel) throws ParseException {
        Page<TeamReportNew> page = teamReportNewService.findPage(teamReportNewQueryModel);
        Page<TeamReportNewAdminVo> voPage = PageBuilder.copyAndConvert(page, v -> teamReportNewComponent.buildTeamReportNewVolumeListVo(v));
        return new Grid<>(voPage);
     }

    /**
     * 导出报表
     * @param teamReportNewQueryModel
     * @param response
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @RequiresPermissions("teamReportNew:export")
    @RequestMapping("/export")
    public String export(TeamReportNewQueryModel teamReportNewQueryModel, HttpServletResponse response) throws IOException, ParseException{
        teamReportNewQueryModel.setPageSize(null);
        teamReportNewQueryModel.setPageNumber(null);
        List<TeamReportNew> salesVolume =  teamReportNewService.findExReport(teamReportNewQueryModel);
        String fileName = "销量报表.xlsx";
        WebUtils.setFileDownloadHeader(response, fileName);

        List<TeamReportNewExportVo> teamReportNewAdminVos = salesVolume.stream().map(teamReportNewComponent::buildTeamReportNewExportVo).collect(Collectors.toList());
        OutputStream os = response.getOutputStream();
        ExcelUtils.exportExcel(teamReportNewAdminVos, TeamReportNewExportVo.class, os);

        return null;
    }









}
