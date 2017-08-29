package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.report.TeamReportNew;
import com.zy.vo.TeamReportNewAdminVo;
import com.zy.vo.TeamReportNewExportVo;
import org.springframework.stereotype.Component;

/**
 * Created by it001 on 2017/8/25.
 */
@Component
public class TeamReportNewComponent {

    public TeamReportNewAdminVo buildTeamReportNewVolumeListVo(TeamReportNew teamReportNew ){
        TeamReportNewAdminVo teamReportNewAdminVo = new TeamReportNewAdminVo();
        BeanUtils.copyProperties(teamReportNew, teamReportNewAdminVo);
        return teamReportNewAdminVo;

    }


    public TeamReportNewExportVo buildTeamReportNewExportVo(TeamReportNew teamReportNew){
        TeamReportNewExportVo teamReportNewExportVo = new TeamReportNewExportVo();
        BeanUtils.copyProperties(teamReportNew, teamReportNewExportVo);
        teamReportNewExportVo.setDate(teamReportNew.getYear()+"/"+teamReportNew.getMonth());
        teamReportNewExportVo.setIsBoss(teamReportNew.getIsBoss()==0?"否":"是");
        return teamReportNewExportVo;
    }
}
