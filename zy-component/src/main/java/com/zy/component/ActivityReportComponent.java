package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.act.Activity;
import com.zy.entity.act.ActivityApply;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserInfo;
import com.zy.model.query.ActivityReportQueryModel;
import com.zy.service.ActivityService;
import com.zy.service.UserInfoService;
import com.zy.service.UserService;
import com.zy.util.GcUtils;
import com.zy.vo.ActionReportExportVo;
import com.zy.vo.ActivityReportVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.zy.util.GcUtils.formatDate;

/**
 * Created by it001 on 2017/6/6.
 */
@Component
public class ActivityReportComponent {
    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserInfoService userInfoService;

    public ActivityReportVo buildReportVo(ActivityApply activityApply, boolean flag, Map<String,Object>dataMap) {
        String shortFmt = "yyyy年M月d日 HH:mm";
        ActivityReportVo activityReportVo = new ActivityReportVo();
        BeanUtils.copyProperties(activityApply, activityReportVo);
        activityReportVo.setNotPayNum(dataMap.get("notPayNum")==null?0L:(Long)dataMap.get("notPayNum"));
        activityReportVo.setPayNum(dataMap.get("payNum")==null?0L:(Long)dataMap.get("payNum"));
        activityReportVo.setSignNum(dataMap.get("signNum")==null?0L:(Long)dataMap.get("signNum"));
        activityReportVo.setId(activityApply.getId());
        Activity activity =activityService.findOne(activityApply.getActivityId());
        activityReportVo.setAddress(activity.getAddress()==null?"":activity.getAddress());
        activityReportVo.setTitle(activity.getTitle()==null?"":activity.getTitle());
        Date starDate = activity.getStartTime();
        activityReportVo.setStarDate(formatDate(starDate, shortFmt));
        User user = userService.findOne(activityApply.getUserId());
        activityReportVo.setNickname(user.getNickname());
        activityReportVo.setPhone(user.getPhone());
        activityReportVo.setUserRankLable(GcUtils.getUserRankLabel(user.getUserRank()));
        if(null!=user.getParentId()){
            UserInfo puserInfo = userInfoService.findByUserId(user.getParentId());
            if (null!=puserInfo){
                activityReportVo.setParentName(puserInfo.getRealname()==null?"":puserInfo.getRealname());
            }
        }
        UserInfo userInfo = userInfoService.findByUserId(user.getId());
        if (userInfo!=null){
            activityReportVo.setRealname(userInfo.getRealname()==null?"":userInfo.getRealname());
        }
        return activityReportVo;
    }

    public ActionReportExportVo buildExportVo(ActivityApply activityApply) {
        ActionReportExportVo actionReportExportVo = new ActionReportExportVo();
        BeanUtils.copyProperties(activityApply, actionReportExportVo);
        ActivityReportVo  activityReportVo=this.buildReportVo(activityApply,false,new HashMap<String,Object>());
        actionReportExportVo.setId(activityReportVo.getId());
        actionReportExportVo.setAddress(activityReportVo.getAddress());
        actionReportExportVo.setNickname(activityReportVo.getNickname());
        actionReportExportVo.setParentName(activityReportVo.getParentName());
        actionReportExportVo.setPhone(activityReportVo.getPhone());
        actionReportExportVo.setStarDate(activityReportVo.getStarDate());
        actionReportExportVo.setTitle(activityReportVo.getTitle());
        actionReportExportVo.setUserRankLable(activityReportVo.getUserRankLable());
        actionReportExportVo.setRealname(activityReportVo.getRealname());
        return actionReportExportVo;
    }
}
