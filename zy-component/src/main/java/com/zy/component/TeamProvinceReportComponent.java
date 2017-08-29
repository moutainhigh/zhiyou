package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.report.TeamProvinceReport;
import com.zy.vo.TeamProvinceReportAdminVo;
import com.zy.vo.TeamProvinceReportExportVo;
import org.springframework.stereotype.Component;

/**
 * Created by liang on 2017/8/28.
 */
@Component
public class TeamProvinceReportComponent {

    public TeamProvinceReportAdminVo buildTeamProvinceReportAdminVo(TeamProvinceReport teamProvinceReport){
        TeamProvinceReportAdminVo teamProvinceReportAdminVo = new TeamProvinceReportAdminVo();
        BeanUtils.copyProperties(teamProvinceReport, teamProvinceReportAdminVo);
        return teamProvinceReportAdminVo;

    }


    public TeamProvinceReportExportVo buildTeamProvinceReportExportVo(TeamProvinceReport teamProvinceReport){
        TeamProvinceReportExportVo teamProvinceReportExportVo = new TeamProvinceReportExportVo();
        BeanUtils.copyProperties(teamProvinceReport, teamProvinceReportExportVo);
        teamProvinceReportExportVo.setDate(teamProvinceReport.getYear()+"/"+teamProvinceReport.getMonth());
        return teamProvinceReportExportVo;
    }
}
