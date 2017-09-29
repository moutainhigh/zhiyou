package com.zy.task.job;

import com.zy.common.exception.ConcurrentException;
import com.zy.common.model.tree.TreeHelper;
import com.zy.common.model.tree.TreeNode;
import com.zy.common.util.DateUtil;
import com.zy.entity.report.TeamReportNew;
import com.zy.entity.report.UserSpread;
import com.zy.entity.sys.Area;
import com.zy.entity.usr.User;
import com.zy.model.query.TeamReportNewQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.model.query.UserUpgradeQueryModel;
import com.zy.service.SystemCodeService;
import com.zy.service.TeamReportNewService;
import com.zy.service.UserService;
import com.zy.service.UserUpgradeService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by it001 on 2017/8/25.  处理 团队报表
 */
public class TeamReportNewJob implements Job {

    private Logger logger = LoggerFactory.getLogger(TeamReportNewJob.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserUpgradeService userUpgradeService;

    @Autowired
    private SystemCodeService systemCodeService;

    @Autowired
    private TeamReportNewService teamReportNewService;


    /**
     * job 开始
     * @param jobExecutionContext
     * @throws JobExecutionException
     */

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("TeamReportNewJob begin.......");
           this.disposeTeamReport();
        logger.info("TeamReportNewJob end.......");
    }

    //开始处理团队逻辑
    private void disposeTeamReport(){
        try {
            //统计所有的新晋特
            UserUpgradeQueryModel userUpgradeQueryModel = new UserUpgradeQueryModel();
            userUpgradeQueryModel.setToUserRankEQ(User.UserRank.V4);
            userUpgradeQueryModel.setUpgradedTimeGTE(DateUtil.getBeforeMonthBegin(new Date(), -1, 0));
            userUpgradeQueryModel.setUpgradedTimeLT(DateUtil.getBeforeMonthEnd(new Date(), 0, 0));
            long V4 = userUpgradeService.count(userUpgradeQueryModel);//统计 新晋特级
            userUpgradeQueryModel.setToUserRankEQ(User.UserRank.V3);
            long V3 = userUpgradeService.count(userUpgradeQueryModel);//统计新晋省级
            UserQueryModel userQueryModel = new UserQueryModel();
            List<User> userList = userService.findAll(userQueryModel);//统计所有用户
            userQueryModel.setUserRankEQ(User.UserRank.V4);
            List<User> userV4List = userService.findAll(userQueryModel);//统计 v4成员
            List<TeamReportNew> teamReportNewList = new ArrayList<>();
            List<Area> areaList = teamReportNewService.findParentAll();
            TeamReportNewQueryModel teamReportNewQueryModel =  new TeamReportNewQueryModel();
            teamReportNewQueryModel.setYearEQ(DateUtil.getYear(DateUtil.getBeforeMonthBegin(new Date(),-2,0)));
            teamReportNewQueryModel.setMonthEQ(DateUtil.getMothNum(DateUtil.getBeforeMonthBegin(new Date(),-2,0)));
            Map<Long,TeamReportNew> teamReportNewMapOld = teamReportNewService.findExReport(teamReportNewQueryModel).stream().collect(Collectors.toMap(v -> v.getUserId(), v -> v));
            for (User user : userV4List) {//当前特级下所成员
                List<User> children = TreeHelper.sortBreadth2(userList, user.getId().toString(), v -> {
                    TreeNode treeNode = new TreeNode();
                    treeNode.setId(v.getId().toString());
                    treeNode.setParentId(v.getParentId() == null ? null : v.getParentId().toString());
                    return treeNode;
                });
                TeamReportNew teamReportNew = new TeamReportNew();
                teamReportNew.setUserId(user.getId());
                teamReportNew.setCreateDate(new Date());
                teamReportNew.setPhone(user.getPhone());
                teamReportNew.setUserName(userService.findRealName(user.getId()));
                teamReportNew.setDistrictId(user.getLargearea() == null?null:new  Long(user.getLargearea()));
                teamReportNew.setIsBoss((user.getIsBoss()==null||user.getIsBoss()==false)?0:1);
                if (user.getLargearea() != null) {
                    teamReportNew.setDistrictName(systemCodeService.findByTypeAndValue("LargeAreaType", user.getLargearea() + "").getSystemName());
                }
                List<User> V4List = children.stream().filter(v -> v.getUserRank() == User.UserRank.V4).collect(Collectors.toList());
                teamReportNew.setExtraNumber(V4List.size());
                //沉睡成员
                List<User> sleepV4List = V4List.stream().filter((user1 -> user1.getLastloginTime().getTime()<DateUtil.getMonthData(new Date(),-3,0).getTime())).collect(Collectors.toList());
                teamReportNew.setSleepextraNumber(sleepV4List.size());
                //特级是不是上月新晋
                userUpgradeQueryModel.setToUserRankEQ(User.UserRank.V4);
                List<User> newV4List = new ArrayList<>();
                for (User userV4 : V4List) {
                    userUpgradeQueryModel.setUserIdEQ(userV4.getId());
                    Long count = userUpgradeService.count(userUpgradeQueryModel);
                    if (count != null && count != 0) {
                        newV4List.add(userV4);
                    }

                }
                teamReportNew.setNewextraNumber(newV4List.size());
                if(V4==0){
                    teamReportNew.setNewextraRate(0D);
                }else {
                    teamReportNew.setNewextraRate(DateUtil.formatDouble(new Double(newV4List.size()) / new Double(V4) * 100));
                }
                //计算省级
                List<User> V3List = children.stream().filter(v -> v.getUserRank() == User.UserRank.V3).collect(Collectors.toList());
                teamReportNew.setProvinceNumber(V3List.size());
                userUpgradeQueryModel.setToUserRankEQ(User.UserRank.V3);
                List<User> newV3List = new ArrayList<>();
                for (User userV3 : newV3List) {
                    userUpgradeQueryModel.setUserIdEQ(userV3.getId());
                    Long count = userUpgradeService.count(userUpgradeQueryModel);
                    if (count != null && count != 0) {
                        newV3List.add(userV3);
                    }
                }
                teamReportNew.setNewprovinceNumber(newV3List.size());
                if ( V3==0){
                    teamReportNew.setNewprovinceRate(0D);
                }else {
                    teamReportNew.setNewprovinceRate(DateUtil.formatDouble(new Double(newV3List.size()) /new Double(V3) * 100));
                }
                teamReportNew.setYear(DateUtil.getYear(DateUtil.getBeforeMonthBegin(new Date(),-1,0)));
                teamReportNew.setMonth(DateUtil.getMothNum(DateUtil.getBeforeMonthBegin(new Date(),-1,0)));
                teamReportNewList.add(teamReportNew);
            }
            //对list 进行排序
            teamReportNewList = teamReportNewList.stream().sorted((teamReportNew1, teamReportNew2) ->
                    teamReportNew2.getExtraNumber() - teamReportNew1.getExtraNumber()).collect(Collectors.toList());
            int i =1;
            int v4number=-1;
            for(TeamReportNew teamReportNew :teamReportNewList) {

                if (teamReportNew.getExtraNumber()<v4number){
                    v4number=teamReportNew.getExtraNumber();
                    i++;
                }
                teamReportNew.setRanking(i);
                TeamReportNew teamReportNewOld = teamReportNewMapOld.get(teamReportNew.getUserId());
                if(teamReportNewOld!=null){
                  teamReportNew.setChangRanking(i-teamReportNewOld.getRanking());
                    teamReportNew.setChangNewextraNumber(teamReportNew.getNewextraNumber()-teamReportNewOld.getNewextraNumber());
                }else{
                    teamReportNew.setChangRanking(i);
                    teamReportNew.setChangNewextraNumber(teamReportNew.getNewextraNumber());
                }
                if (v4number==-1){
                    v4number = teamReportNew.getExtraNumber();
                }
                teamReportNewService.insert(teamReportNew);
            }
        } catch (ConcurrentException e) {
                try {
                    TimeUnit.SECONDS.sleep(2);} catch (InterruptedException e1) {}
                    this.disposeTeamReport();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
    }
}
