package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.tour.Tour;
import com.zy.entity.tour.TourTime;
import com.zy.entity.tour.TourUser;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserInfo;
import com.zy.service.TourService;
import com.zy.service.TourTimeService;
import com.zy.service.UserInfoService;
import com.zy.service.UserService;
import com.zy.util.GcUtils;
import com.zy.vo.TourUserAdminVo;
import com.zy.vo.TourUserExportVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.zy.util.GcUtils.getThumbnail;

/**
 * Author: Xuwq
 * Date: 2017/6/30.
 */
@Component
public class TourUserComponent {

    private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm";

    private static final String S_TIME_PATTERN = "yyyy-MM-dd";

    @Autowired
    private UserService userService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private TourService tourService;

    @Autowired
    private TourTimeService tourTimeService;


//    @Autowired
//    private TourService tourService;

    public TourUserAdminVo buildAdminVo(TourUser tourUser) {
        TourUserAdminVo tourUserAdminVo = new TourUserAdminVo();
        BeanUtils.copyProperties(tourUser, tourUserAdminVo);
        UserInfo userInfo = userInfoService.findByUserId(tourUser.getUserId());
        tourUserAdminVo.setUserName(userInfo.getRealname());
        UserInfo userIf = userInfoService.findByUserId(tourUser.getParentId());
        tourUserAdminVo.setParentName(userIf.getRealname());
        User user = userService.findOne(tourUser.getUserId());
        tourUserAdminVo.setUserPhone(user.getPhone());
        User use = userService.findOne(tourUser.getParentId());
        tourUserAdminVo.setParentPhone(use.getPhone());
        if (tourUser.getUpdateBy() != null){
            UserInfo userI = userInfoService.findByUserId(tourUser.getUpdateBy());
            tourUserAdminVo.setUpdateName(userI.getRealname());
        }
        Tour tour = tourService.findTourOne(tourUser.getTourId());
        tourUserAdminVo.setTourTitle(tour.getTitle());
        tourUserAdminVo.setCreatedDateLabel( GcUtils.formatDate(tourUser.getCreateDate(), TIME_PATTERN));
        tourUserAdminVo.setUpdateDateLabel( GcUtils.formatDate(tourUser.getUpdateDate(), TIME_PATTERN));
        tourUserAdminVo.setPlanTimeLabel( GcUtils.formatDate(tourUser.getPlanTime(), TIME_PATTERN));
        if (tourUser.getCarImages() != null) {
            tourUserAdminVo.setImageThumbnail(getThumbnail(tourUser.getCarImages(), 750, 450));
        }
        TourTime tourTime = tourTimeService.findOne(tourUser.getTourTimeId());
        tourUserAdminVo.setTourTime(GcUtils.formatDate(tourTime.getBegintime(), S_TIME_PATTERN) + "  至  " + GcUtils.formatDate(tourTime.getEndtime(), S_TIME_PATTERN));
        return tourUserAdminVo;
    }

    public TourUserExportVo buildExportVo(TourUser tourUser) {
        TourUserExportVo tourUserExportVo = new TourUserExportVo();
        BeanUtils.copyProperties(tourUser, tourUserExportVo);
        UserInfo userInfo = userInfoService.findByUserId(tourUser.getUserId());
        tourUserExportVo.setUserName(userInfo.getRealname());
        UserInfo userIf = userInfoService.findByUserId(tourUser.getParentId());
        tourUserExportVo.setParentName(userIf.getRealname());
        User user = userService.findOne(tourUser.getUserId());
        tourUserExportVo.setUserPhone(user.getPhone());
        User use = userService.findOne(tourUser.getParentId());
        tourUserExportVo.setParentPhone(use.getPhone());
        if (tourUser.getAuditStatus() == 1){
            tourUserExportVo.setAuditStatus("审核中");
        }else if (tourUser.getAuditStatus() == 2){
            tourUserExportVo.setAuditStatus("待补充");
        }else if (tourUser.getAuditStatus() == 3){
            tourUserExportVo.setAuditStatus("已生效");
        }else if (tourUser.getAuditStatus() == 4){
            tourUserExportVo.setAuditStatus("已完成");
        }else if (tourUser.getAuditStatus() == 5){
            tourUserExportVo.setAuditStatus("审核失败");
        }

        if (tourUser.getIsEffect() == 1){
            tourUserExportVo.setIsEffect("是");
        }else if (tourUser.getIsEffect() == 0){
            tourUserExportVo.setIsEffect("否");
        }

        if (tourUser.getIsTransfers() == 1){
            tourUserExportVo.setIsTransfers("是");
        }else if (tourUser.getIsTransfers() == 0){
            tourUserExportVo.setIsTransfers("否");
        }

        if (tourUser.getIsAddBed() == 1){
            tourUserExportVo.setIsAddBed("是");
        }else if (tourUser.getIsAddBed() == 0){
            tourUserExportVo.setIsAddBed("否");
        }

        if (tourUser.getHouseType() == 1){
            tourUserExportVo.setHouseType("标准间");
        }else if (tourUser.getHouseType() == 2){
            tourUserExportVo.setHouseType("三人间");
        }

        if (tourUser.getUpdateBy() != null){
            UserInfo userI = userInfoService.findByUserId(tourUser.getUpdateBy());
            tourUserExportVo.setUpdateName(userI.getRealname());
        }
        Tour tour = tourService.findTourOne(tourUser.getTourId());
        tourUserExportVo.setTourTitle(tour.getTitle());
        tourUserExportVo.setCreatedDateLabel( GcUtils.formatDate(tourUser.getCreateDate(), TIME_PATTERN));
        tourUserExportVo.setUpdateDateLabel( GcUtils.formatDate(tourUser.getUpdateDate(), TIME_PATTERN));
        tourUserExportVo.setPlanTimeLabel( GcUtils.formatDate(tourUser.getPlanTime(), TIME_PATTERN));
        if (tourUser.getCarImages() != null) {
            tourUserExportVo.setImageThumbnail(getThumbnail(tourUser.getCarImages(), 750, 450));
        }
        TourTime tourTime = tourTimeService.findOne(tourUser.getTourTimeId());
        tourUserExportVo.setTourTime(GcUtils.formatDate(tourTime.getBegintime(), S_TIME_PATTERN) + "  至  " + GcUtils.formatDate(tourTime.getEndtime(), S_TIME_PATTERN));
        return tourUserExportVo;
    }
}
