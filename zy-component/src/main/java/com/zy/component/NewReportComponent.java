package com.zy.component;

import com.zy.common.util.DateUtil;
import com.zy.entity.report.NewReportTeam;
import com.zy.entity.sys.Area;
import com.zy.entity.sys.SystemCode;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserInfo;
import com.zy.entity.usr.UserUpgrade;
import com.zy.model.query.AreaQueryModel;
import com.zy.model.query.NewReportTeamQueryModel;
import com.zy.model.query.UserInfoQueryModel;
import com.zy.model.query.UserUpgradeQueryModel;
import com.zy.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by it001 on 2017-09-28.
 */
@Component
public class NewReportComponent {
    @Autowired
    private LocalCacheComponent localCacheComponent;

    @Autowired
    private SystemCodeService systemCodeService;

    @Autowired
    private UserUpgradeService userUpgradeService;

    @Autowired
    private TeamReportNewService teamReportNewService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private NewReportTeamService newReportTeamService;

    /**
     * 处理查询大区数据
     * @param type
     * @return
     */
    public Map<String,Object> disposeTeam(String type) {
        //过滤出所有的V4成员 且分过大区的 有大区总裁的
        List<User> users = localCacheComponent.getUsers().stream().filter(user -> user.getUserRank()==User.UserRank.V4).collect(Collectors.toList());
        List<User>largeUserList = users.stream().filter(user ->  user.getLargearea()!=null).collect(Collectors.toList());
        List<SystemCode> largeAreaTypes = systemCodeService.findByType("LargeAreaType");//查询说有大区
        List<User>presidentUserList =users.stream().filter(user -> user.getIsPresident()!=null).collect(Collectors.toList());//查询所有大区总裁
        Map<String ,Object> map = new HashMap<>();
        map.put("number",this.disposeTeamNumber(type,largeUserList,largeAreaTypes,presidentUserList));
        map.put("newV4",this.disposeTeamNewV4(type,largeUserList,largeAreaTypes,presidentUserList));
        map.put("sleep",this.disposeTeamSleep(type,largeUserList,largeAreaTypes,presidentUserList));
        map.put("areat",this.disposeTeamAreat(type,users));
        return map;
    }

    //处理团队人数
    private List<Map<String,Integer>> disposeTeamNumber(String type,List<User> userList,List<SystemCode> largeAreaTypes,List<User>presidentUserList){
        List<Map<String,Integer>> resultList = new ArrayList<>();
        Map<Integer,List<User>> userMap = userList.stream().collect( Collectors.groupingBy(User::getLargearea));
        if ("0".equals(type)){//公司不需要 过滤大区总裁
            for (SystemCode systemCode :largeAreaTypes){
                Map<String,Integer> naemap= new HashMap<>();
                naemap.put(systemCode.getSystemName(),userMap.get(systemCode.getSystemValue())!=null?userMap.get(systemCode.getSystemValue()).size():0);
                resultList.add(naemap);
            }
          }else{//统计各个大区的分布
           List<User> areatUserList = userMap.get(type);//获取大区的所有人
            Map<Long,List<User>> areatMap = areatUserList.stream().collect(Collectors.groupingBy(User::getPresidentId));
            for(User user :presidentUserList){
                Map<String,Integer> naemap= new HashMap<>();
                naemap.put(user.getNickname(),areatMap.get(user.getId())!=null?userMap.get(user.getId()).size():0);
                resultList.add(naemap);
            }
         }
       return resultList;
    }

    /**
     * 处理 新晋特级
     * @param type
     * @param userList
     * @param largeAreaTypes
     * @param presidentUserList
     * @return
     */
    private List<Map<String,Integer>> disposeTeamNewV4(String type,List<User> userList,List<SystemCode> largeAreaTypes,List<User>presidentUserList){
        List<Map<String,Integer>> resultList = new ArrayList<>();
        //统计所有的新晋特
        UserUpgradeQueryModel userUpgradeQueryModel = new UserUpgradeQueryModel();
        userUpgradeQueryModel.setToUserRankEQ(User.UserRank.V4);
        userUpgradeQueryModel.setUpgradedTimeGTE(DateUtil.getBeforeMonthBegin(new Date(), -1, 0));
        userUpgradeQueryModel.setUpgradedTimeLT(DateUtil.getBeforeMonthEnd(new Date(), 0, 0));
        Map<Long,List<UserUpgrade >>userUpgradeList = userUpgradeService.findAll(userUpgradeQueryModel).stream().collect(Collectors.groupingBy(UserUpgrade::getUserId));//查询所有的升级用户
        Map<Integer,List<User>> userMap = userList.stream().collect( Collectors.groupingBy(User::getLargearea));
        if ("0".equals(type)){//处理公司
            long V4 = userUpgradeService.count(userUpgradeQueryModel);//统计 新晋特级
            for (SystemCode systemCode :largeAreaTypes){
                int count = 0;
                Map<String,Integer> naemap= new HashMap<>();
                List<User> areatList = userMap.get(systemCode.getSystemValue());
                for(User user : areatList ){
                    if (userUpgradeList.get(user.getId())!=null){//能取到  说明是新晋的
                        count++;
                    }
                }
                if (V4==0){
                    naemap.put(systemCode.getSystemName()+":0.00%",0);
                }else{
                    Double duty  = DateUtil.formatDouble( new Double(count) / new Double(V4) * 100);
                    naemap.put(systemCode.getSystemName()+":" +duty+"%",count);
                }
                resultList.add(naemap);
            }
        }else{//处理  大区
            List<User> areatUserList = userMap.get(type);//获取大区的所有人
            int sum=0;
            for(User user0:areatUserList){
                if (userUpgradeList.get(user0.getId())!=null){//能取到  说明是新晋的
                    sum++;
                }
            }
            Map<Long,List<User>> areatMap = areatUserList.stream().collect(Collectors.groupingBy(User::getPresidentId));

            for(User user :presidentUserList){
                int count = 0;
                Map<String,Integer> naemap= new HashMap<>();
                List<User> areatuserList =areatMap.get(user.getId());
                for(User user1 :areatuserList){
                    if (userUpgradeList.get(user1.getId())!=null){//能取到  说明是新晋的
                        count++;
                    }
                }
                if (sum==0){
                    naemap.put(user.getNickname()+":0.00%",count);
                }else{
                    Double duty  = DateUtil.formatDouble( new Double(count) / new Double(sum) * 100);
                    naemap.put(user.getNickname()+":"+duty+"%",count);
                }
                naemap.put(user.getNickname(),count);
                resultList.add(naemap);
            }

        }
       return resultList;
    }

    /**
     * 处理沉睡用户
     * @param type
     * @param userList
     * @param largeAreaTypes
     * @param presidentUserList
     * @return
     */
    private List<Map<String,Integer>> disposeTeamSleep(String type,List<User> userList,List<SystemCode> largeAreaTypes,List<User>presidentUserList) {
        List<Map<String,Integer>> resultList = new ArrayList<>();
        Map<Integer,List<User>> userMap = userList.stream().collect( Collectors.groupingBy(User::getLargearea));
        if("0".equals(type)){//公司
           userList = userList.stream().filter(user -> user.getLastloginTime().getTime()<DateUtil.getMonthData(new Date(),-3,0).getTime()).collect(Collectors.toList());
          for (SystemCode systemCode :largeAreaTypes){
              Map<String,Integer> naemap= new HashMap<>();
              int count =0;
              List<User> areatUserList = userMap.get(systemCode.getSystemValue());
              if (areatUserList!=null){
                  areatUserList =  areatUserList.stream().filter(user -> user.getLastloginTime().getTime()<DateUtil.getMonthData(new Date(),-3,0).getTime()).collect(Collectors.toList());
                 count=areatUserList.size();
              }
              if (userList.size()==0){
                  naemap.put(systemCode.getSystemName()+":0.00%",0);
              }else{
                  Double duty  = DateUtil.formatDouble( new Double(userList.size()) / new Double(count) * 100);
                  naemap.put(systemCode.getSystemName()+":"+duty+"%",0);
              }
              resultList.add(naemap);
          }
        }else{//大区
            List<User> areatUserList = userMap.get(type);
            Map<Long,List<User>> areatMap = areatUserList.stream().collect(Collectors.groupingBy(User::getPresidentId));
            areatUserList =areatUserList.stream().filter(user -> user.getLastloginTime().getTime()<DateUtil.getMonthData(new Date(),-3,0).getTime()).collect(Collectors.toList());
            for(User user1 :presidentUserList){
             Map<String,Integer> naemap= new HashMap<>();
              int count =0;
              List<User>  presidentAreatUserList = areatMap.get(user1.getId());
                if (presidentAreatUserList!=null) {
                    presidentAreatUserList = presidentAreatUserList.stream().filter(user -> user.getLastloginTime().getTime() < DateUtil.getMonthData(new Date(), -3, 0).getTime()).collect(Collectors.toList());
                    count=presidentAreatUserList.size();
                }
                if (areatUserList.size()==0){
                    naemap.put(user1.getNickname()+":0.00%",0);
                }else{
                    Double duty  = DateUtil.formatDouble( new Double(areatUserList.size()) / new Double(count) * 100);
                    naemap.put(user1.getNickname()+":"+duty+"%",0);
                }
                resultList.add(naemap);
            }


        }
        return resultList;
    }

    /**
     * 处理人员分布
     * @param type
     * @return
     */
    private List<NewReportTeam> disposeTeamAreat(String type,List<User> userList){
        List<Area> areaList = teamReportNewService.findParentAll();//查询 所有省
        Map<Long, Area> areaMap= teamReportNewService.findAreaAll(new AreaQueryModel()).stream().collect(Collectors.toMap(v -> v.getId(), v -> v));//将区域转成map类型
        Map<Long,UserInfo>userInfoMap = userInfoService.findAll(new UserInfoQueryModel()).stream().collect(Collectors.toMap(v -> v.getUserId(), v -> v));
        NewReportTeamQueryModel newReportTeamQueryModel =  new NewReportTeamQueryModel();
        newReportTeamQueryModel.setYearEQ(DateUtil.getYear(DateUtil.getBeforeMonthBegin(new Date(),-2,0)));
        newReportTeamQueryModel.setMonthEQ(DateUtil.getMothNum(DateUtil.getBeforeMonthBegin(new Date(),-2,0)));
        Map<Integer,Map<Long,List<NewReportTeam>>> newReportTeamOld = newReportTeamService.findAll(newReportTeamQueryModel).stream().collect(Collectors.groupingBy(NewReportTeam::getRegion, Collectors.groupingBy(NewReportTeam::getProvinceId)));
        List<User> newList = new ArrayList<>();

        for (User user:userList ){//将没有实名认证的或者 没有地址的 过滤掉
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
        List<NewReportTeam> newReportTeamList = new ArrayList<>();
        if ("0".equals(type)){ //公司
            Map<Long,List<User>> gsMap = newList.stream().collect(Collectors.groupingBy(User::getBossId));
            for (Area area :areaList){
                List<User> newUserList = gsMap.get(area.getId());
                NewReportTeam newReportTeam = new NewReportTeam();
                newReportTeam.setCreateDate(new Date());
                newReportTeam.setProvinceId(area.getId());
                newReportTeam.setProvinceName(area.getName());
                newReportTeam.setYear(DateUtil.getYear(DateUtil.getBeforeMonthBegin(new Date(),-1,0)));
                newReportTeam.setMonth(DateUtil.getMothNum(DateUtil.getBeforeMonthBegin(new Date(),-1,0)));
                newReportTeam.setNumber(userList!=null?userList.size():0);
                newReportTeam.setRegion(0);
                newReportTeamList.add(newReportTeam);
            }
         }else{//大区
            newList =newList.stream().filter(user -> user.getLargearea()!=null).collect(Collectors.toList());//将没有分配大区的过滤掉
            Map<Integer, Map<Long, List<User>>> counting = newList.stream().collect(
                    Collectors.groupingBy(User::getLargearea, Collectors.groupingBy(User::getBossId)));

            Map<Long, List<User>>userMap= counting.get(type);
            if (userMap!=null){
                for (Area area :areaList){
                    List<User> newUserList = userMap.get(area.getId());
                    NewReportTeam newReportTeam = new NewReportTeam();
                    newReportTeam.setCreateDate(new Date());
                    newReportTeam.setProvinceId(area.getId());
                    newReportTeam.setProvinceName(area.getName());
                    newReportTeam.setYear(DateUtil.getYear(DateUtil.getBeforeMonthBegin(new Date(),-1,0)));
                    newReportTeam.setMonth(DateUtil.getMothNum(DateUtil.getBeforeMonthBegin(new Date(),-1,0)));
                    newReportTeam.setNumber(userList!=null?userList.size():0);
                    newReportTeam.setRegion(Integer.valueOf(type));
                    newReportTeamList.add(newReportTeam);
                }
            }

         }

        //开始处理排名
        newReportTeamList = newReportTeamList.stream().sorted((teamReportNew1, teamReportNew2) ->
                teamReportNew2.getNumber() - teamReportNew1.getNumber()).collect(Collectors.toList());
        int i =1;
        int v4number=-1;
        for(NewReportTeam newReportTeam :newReportTeamList) {

            if (newReportTeam.getNumber()<v4number){
                v4number=newReportTeam.getNumber();
                i++;
            }
            newReportTeam.setRank(i);
            NewReportTeam teamReportNewOld = newReportTeamOld.get(newReportTeam.getRegion()).get(newReportTeam.getProvinceId()).get(0);
            if(teamReportNewOld!=null){
                newReportTeam.setRankChange(i-newReportTeam.getRank());
            }else{
                newReportTeam.setRankChange(i);
            }
            if (v4number==-1){
                v4number = newReportTeam.getNumber();
            }
        }
        return newReportTeamList;
    }


    /**
     * 处理查询大区数据
     * @param type
     * @return
     */
    public Map<String,Object> disposeTeamAreat(String type) {
        List<User> users = localCacheComponent.getUsers().stream().filter(user -> user.getUserRank()==User.UserRank.V4).collect(Collectors.toList());
        Map<String ,Object> map = new HashMap<>();
        map.put("areat",this.disposeTeamAreat(type,users));
        return map;
    }
}
