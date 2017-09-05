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

    /**
     * 封装vo
     * @param teamProvinceReport
     * @return
     */
    public TeamProvinceReportAdminVo buildTeamProvinceReportAdminVo(TeamProvinceReport teamProvinceReport){
        TeamProvinceReportAdminVo teamProvinceReportAdminVo = new TeamProvinceReportAdminVo();
        BeanUtils.copyProperties(teamProvinceReport, teamProvinceReportAdminVo);
        return teamProvinceReportAdminVo;

    }

    /**
     * 封装VO
     * @param teamProvinceReport
     * @return
     */
    public TeamProvinceReportExportVo buildTeamProvinceReportExportVo(TeamProvinceReport teamProvinceReport){
        TeamProvinceReportExportVo teamProvinceReportExportVo = new TeamProvinceReportExportVo();
        BeanUtils.copyProperties(teamProvinceReport, teamProvinceReportExportVo);
        teamProvinceReportExportVo.setDate(teamProvinceReport.getYear()+"/"+teamProvinceReport.getMonth());
        return teamProvinceReportExportVo;
    }
}
