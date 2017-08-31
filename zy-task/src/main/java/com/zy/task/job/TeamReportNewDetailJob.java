package com.zy.task.job;

import com.zy.common.exception.ConcurrentException;
import com.zy.common.model.tree.TreeHelper;
import com.zy.common.model.tree.TreeNode;
import com.zy.common.util.DateUtil;
import com.zy.entity.report.TeamReportNew;
import com.zy.entity.report.UserSpread;
import com.zy.entity.sys.Area;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserInfo;
import com.zy.model.query.AreaQueryModel;
import com.zy.model.query.UserInfoQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.*;
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
 * Created by it001 on 2017/8/29.
 * 处理团队  分布
 */
public class TeamReportNewDetailJob implements Job {

    private Logger logger = LoggerFactory.getLogger(TeamReportNewDetailJob.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserUpgradeService userUpgradeService;

    @Autowired
    private TeamReportNewService teamReportNewService;

    @Autowired
    private UserInfoService userInfoService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException{
      logger.info("TeamReportNewDetailJob begin.......");
        this.disposeTeamReport();
      logger.info("TeamReportNewDetailJob end.......");
    }
    public void disposeTeamReport(){
          try {
                UserQueryModel userQueryModel = new UserQueryModel();
                List<User> userList = userService.findAll(userQueryModel);//统计所有用户
                userQueryModel.setUserRankEQ(User.UserRank.V4);
                List<User> userV4List = userService.findAll(userQueryModel);//统计 v4成员
                List<Area> areaList = teamReportNewService.findParentAll();
                Map<Long, Area> areaMap= teamReportNewService.findAreaAll(new  AreaQueryModel()).stream().collect(Collectors.toMap(v -> v.getId(), v -> v));
                Map<Long,UserInfo>userInfoMap = userInfoService.findAll(new UserInfoQueryModel()).stream().collect(Collectors.toMap(v -> v.getUserId(), v -> v));
               for (User user : userV4List) {//当前特级下所成员
                    List<User> children = TreeHelper.sortBreadth2(userList, user.getId().toString(), v -> {
                        TreeNode treeNode = new TreeNode();
                        treeNode.setId(v.getId().toString());
                        treeNode.setParentId(v.getParentId() == null ? null : v.getParentId().toString());
                        return treeNode;
                    });
                    List<User> newList = new ArrayList<>();
                    //循环处理用户地区
                    for (User userchildren : children) {
                        Long areaId =  userInfoMap.get(userchildren.getId())==null?null:userInfoMap.get(userchildren.getId()).getAreaId();
                        Area area =null;
                        if (areaId != null) {
                            do {
                                 area =  areaMap.get(areaId);
                                 areaId=area!=null?area.getParentId():null;
                            }while (areaId!=null);
                            if (area!=null) {
                                userchildren.setBossId(area.getId()); //暂时借用这个字段 修改时 注意
                                newList.add(userchildren);
                            }
                        }
                    }
                    Map<Long, Map<User.UserRank, List<User>>> counting = newList.stream().collect(
                            Collectors.groupingBy(User::getBossId, Collectors.groupingBy(User::getUserRank)));
                    for (Area area : areaList) {
                        UserSpread userSpread = new UserSpread();
                        userSpread.setUserId(user.getId());
                        userSpread.setProvinceId(area.getId());
                        userSpread.setProvinceName(area.getName());
                        Map<User.UserRank, List<User>> userRankListMap = counting.get(area.getId());
                        if (userRankListMap != null) {
                            userSpread.setV0(userRankListMap.get(User.UserRank.V0) == null ? 0 : userRankListMap.get(User.UserRank.V0).size());
                            userSpread.setV1(userRankListMap.get(User.UserRank.V1) == null ? 0 : userRankListMap.get(User.UserRank.V1).size());
                            userSpread.setV2(userRankListMap.get(User.UserRank.V2) == null ? 0 : userRankListMap.get(User.UserRank.V2).size());
                            userSpread.setV3(userRankListMap.get(User.UserRank.V3) == null ? 0 : userRankListMap.get(User.UserRank.V3).size());
                            userSpread.setV4(userRankListMap.get(User.UserRank.V4) == null ? 0 : userRankListMap.get(User.UserRank.V4).size());
                        } else {
                            userSpread.setV0(0);
                            userSpread.setV1(0);
                            userSpread.setV2(0);
                            userSpread.setV3(0);
                            userSpread.setV4(0);

                        }
                        userSpread.setYear(DateUtil.getYear(DateUtil.getBeforeMonthBegin(new Date(),-1,0)));
                        userSpread.setMonth(DateUtil.getMothNum(DateUtil.getBeforeMonthBegin(new Date(),-1,0)));
                        userSpread.setCreateDate(new Date());
                        teamReportNewService.insertUserSpread(userSpread);
                    }
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
