package com.zy.task.job;

import com.zy.common.util.DateUtil;
import com.zy.entity.report.NewReportTeam;
import com.zy.entity.sys.Area;
import com.zy.entity.sys.SystemCode;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserInfo;
import com.zy.model.query.AreaQueryModel;
import com.zy.model.query.NewReportTeamQueryModel;
import com.zy.model.query.UserInfoQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.*;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by it001 on 2017-09-27.
 */
public class NewReportTeamJob implements Job {

    private Logger logger = LoggerFactory.getLogger(NewReportTeamJob.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SystemCodeService systemCodeService;

    @Autowired
    private TeamReportNewService teamReportNewService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private NewReportTeamService newReportTeamService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("NewReportTeamJob begin.......");
        this.disposeTeamReport();
        logger.info("NewReportTeamJob end.......");
    }

    //处理 逻辑开始
    public void disposeTeamReport(){

        UserQueryModel userQueryModel = new UserQueryModel();
        userQueryModel.setUserRankEQ(User.UserRank.V4);
        List<User> userV4List = userService.findAll(userQueryModel);//统计 v4成员
        List<SystemCode> largeAreaTypes = systemCodeService.findByType("LargeAreaType");//查询说有大区
        List<Area> areaList = teamReportNewService.findParentAll();//查询 所有地区
        Map<Long, Area> areaMap= teamReportNewService.findAreaAll(new AreaQueryModel()).stream().collect(Collectors.toMap(v -> v.getId(), v -> v));//将区域转成map类型
        Map<Long,UserInfo>userInfoMap = userInfoService.findAll(new UserInfoQueryModel()).stream().collect(Collectors.toMap(v -> v.getUserId(), v -> v));
        NewReportTeamQueryModel newReportTeamQueryModel =  new NewReportTeamQueryModel();
        newReportTeamQueryModel.setYearEQ(DateUtil.getYear(DateUtil.getBeforeMonthBegin(new Date(),-2,0)));
        newReportTeamQueryModel.setMonthEQ(DateUtil.getYear(DateUtil.getBeforeMonthBegin(new Date(),-2,0)));
        Map<Integer,Map<Long,List<NewReportTeam>>> newReportTeamOld = newReportTeamService.findAll(newReportTeamQueryModel).stream().collect(Collectors.groupingBy(NewReportTeam::getRegion, Collectors.groupingBy(NewReportTeam::getProvinceId)));
        userV4List =userV4List.stream().filter(user -> user.getLargearea()!=null).collect(Collectors.toList());//将没有分配大区的过滤掉
        List<User> newList = new ArrayList<>();
        for (User user:userV4List ){//将没有实名认证的或者 没有地址的 过滤掉
            Long areaId = userInfoMap.get(user.getId())==null?null:userInfoMap.get(user.getId()).getAreaId();
            Area area =null;
            if (areaId != null) {
                do {
                    area =  areaMap.get(areaId);
                    areaId=area!=null?area.getParentId():null;
                }while (areaId!=null);
                if (area!=null) {
                    user.setBossId(area.getId()); //暂时借用这个字段 修改时 注意
                    newList.add(user);
                }
            }
        }
        Map<Integer, Map<Long, List<User>>> counting = newList.stream().collect(
                Collectors.groupingBy(User::getLargearea, Collectors.groupingBy(User::getBossId)));

        Map<String,List<NewReportTeam>> map = new HashMap<>();
        //循环大区处理逻辑
      for(SystemCode systemCode:largeAreaTypes){
         List<NewReportTeam> newReportTeamList = new ArrayList<>();
         Map<Long,List<User>> UserMap = counting.get(systemCode.getSystemValue());
         Map<Long,List<NewReportTeam>> OldnewReportTeam =newReportTeamOld.get(systemCode.getSystemValue());
         for (Area area :areaList){
             List<User> userList = UserMap.get(area.getId());
             NewReportTeam newReportTeam = new NewReportTeam();
             newReportTeam.setCreateDate(new Date());
             newReportTeam.setProvinceId(area.getId());
             newReportTeam.setProvinceName(area.getName());
             newReportTeam.setYear(DateUtil.getYear(DateUtil.getBeforeMonthBegin(new Date(),-2,0)));
             newReportTeam.setMonth(DateUtil.getYear(DateUtil.getBeforeMonthBegin(new Date(),-2,0)));
             newReportTeam.setNumber(userList.size());
             newReportTeam.setRegion(Integer.parseInt(systemCode.getSystemValue()));
             newReportTeamList.add(newReportTeam);
         }
          map.put(systemCode.getSystemValue(),newReportTeamList);
      }
        //处理公司
        Map<Long,List<User>> gsMap = userV4List.stream().collect(Collectors.groupingBy(User::getBossId));
        List<NewReportTeam> gsnewReportTeamList = new ArrayList<>();
        for (Area area :areaList){
            List<User> userList = gsMap.get(area.getId());
            NewReportTeam newReportTeam = new NewReportTeam();
            newReportTeam.setCreateDate(new Date());
            newReportTeam.setProvinceId(area.getId());
            newReportTeam.setProvinceName(area.getName());
            newReportTeam.setYear(DateUtil.getYear(DateUtil.getBeforeMonthBegin(new Date(),-2,0)));
            newReportTeam.setMonth(DateUtil.getYear(DateUtil.getBeforeMonthBegin(new Date(),-2,0)));
            newReportTeam.setNumber(userList.size());
            newReportTeam.setRegion(0);
            gsnewReportTeamList.add(newReportTeam);
        }
        map.put("0",gsnewReportTeamList);


        //处理排名
        for (Object key : map.keySet()) {
            List<NewReportTeam>rankList= map.get(key);
            //对list 进行排序
            rankList = rankList.stream().sorted((teamReportNew1, teamReportNew2) ->
                    teamReportNew2.getNumber() - teamReportNew1.getNumber()).collect(Collectors.toList());
            int i =1;
            int v4number=-1;
            for(NewReportTeam newReportTeam :rankList) {

                if (newReportTeam.getNumber()<v4number){
                    v4number=newReportTeam.getNumber();
                    i++;
                }
                newReportTeam.setRank(i);
                NewReportTeam teamReportNewOld = newReportTeamOld.get(newReportTeam.getRegion()).get(newReportTeam.getProvinceId()).get(0);
                if(teamReportNewOld!=null){
                    newReportTeam.setRankChange(i-newReportTeam.getRank());
                    //teamReportNew.setChangNewextraNumber(teamReportNew.getNewextraNumber()-teamReportNewOld.getNewextraNumber());
                }else{
                    newReportTeam.setRankChange(i);
                    //teamReportNew.setChangNewextraNumber(teamReportNew.getNewextraNumber());
                }
                if (v4number==-1){
                    v4number = newReportTeam.getNumber();
                }
                newReportTeamService.insert(newReportTeam);
            }
        }
    }
}
