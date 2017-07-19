package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.common.util.DateUtil;
import com.zy.entity.tour.Tour;
import com.zy.entity.tour.TourTime;
import com.zy.entity.tour.TourUser;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserInfo;
import com.zy.model.dto.AreaDto;
import com.zy.service.TourService;
import com.zy.service.TourTimeService;
import com.zy.service.UserInfoService;
import com.zy.service.UserService;
import com.zy.util.GcUtils;
import com.zy.vo.TourJoinUserExportVo;
import com.zy.vo.TourUserAdminVo;
import com.zy.vo.TourUserExportVo;
import com.zy.vo.TourUserListVo;
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

    @Autowired
    private CacheComponent cacheComponent;

//    @Autowired
//    private TourService tourService;

    public TourUserAdminVo buildAdminVo(TourUser tourUser) {
        TourUserAdminVo tourUserAdminVo = new TourUserAdminVo();
        BeanUtils.copyProperties(tourUser, tourUserAdminVo);
        UserInfo userInfo = userInfoService.findByUserId(tourUser.getUserId());
        if (userInfo!=null) {
            tourUserAdminVo.setUserName(userInfo.getRealname());
            tourUserAdminVo.setIdCardNumber(userInfo.getIdCardNumber());
            tourUserAdminVo.setAge(DateUtil.getAge(userInfo.getIdCardNumber()));
            if (userInfo.getGender() != null && userInfo != null){
                tourUserAdminVo.setGender(userInfo.getGender().toString());
            }
            Long areaId = userInfo.getAreaId();
            tourUserAdminVo.setAreaId(areaId);
            if (areaId != null) {
                AreaDto areaDto = cacheComponent.getAreaDto(areaId);
                if (areaDto != null) {
                    tourUserAdminVo.setProvince(areaDto.getProvince());
                    tourUserAdminVo.setCity(areaDto.getCity());
                    tourUserAdminVo.setDistrict(areaDto.getDistrict());
                }
            }
        }
        UserInfo userIf = userInfoService.findByUserId(tourUser.getParentId());
        if (userIf!=null) {
            tourUserAdminVo.setParentName(userIf.getRealname());
        }
        User user = userService.findOne(tourUser.getUserId());
        tourUserAdminVo.setUserPhone(user.getPhone());
        User use = userService.findOne(tourUser.getParentId());
        tourUserAdminVo.setParentPhone(use.getPhone());
        if (tourUser.getUpdateBy() != null){
            UserInfo userI = userInfoService.findByUserId(tourUser.getUpdateBy());
            if(userI!=null) {
                tourUserAdminVo.setUpdateName(userI.getRealname());
            }
        }
        if (tourUser.getTourId() != null){
            Tour tour = tourService.findTourOne(tourUser.getTourId());
            tourUserAdminVo.setTourTitle(tour.getTitle());
        }
        tourUserAdminVo.setCreatedDateLabel( GcUtils.formatDate(tourUser.getCreateDate(), TIME_PATTERN));
        tourUserAdminVo.setUpdateDateLabel( GcUtils.formatDate(tourUser.getUpdateDate(), TIME_PATTERN));
        tourUserAdminVo.setPlanTimeLabel( GcUtils.formatDate(tourUser.getPlanTime(), TIME_PATTERN));
        tourUserAdminVo.setVisitTime( GcUtils.formatDate(tourUser.getVisitTime(), TIME_PATTERN));
        if (tourUser.getCarImages() != null) {
            tourUserAdminVo.setImageThumbnail(getThumbnail(tourUser.getCarImages(), 750, 450));
        }
        if (tourUser.getTourTimeId() != null){
            TourTime tourTime = tourTimeService.findOne(tourUser.getTourTimeId());
            tourUserAdminVo.setTourTime(GcUtils.formatDate(tourTime.getBegintime(), S_TIME_PATTERN) + "  至  " + GcUtils.formatDate(tourTime.getEndtime(), S_TIME_PATTERN));
        }
        return tourUserAdminVo;
    }

    public TourUserExportVo buildExportVo(TourUser tourUser) {
        TourUserExportVo tourUserExportVo = new TourUserExportVo();
        BeanUtils.copyProperties(tourUser, tourUserExportVo);
        if (tourUser.getCarImages() != null) {
            tourUserExportVo.setImageThumbnail(getThumbnail(tourUser.getCarImages(), 750, 450));
        }
        UserInfo userInfo = userInfoService.findByUserId(tourUser.getUserId());
        tourUserExportVo.setUserName(userInfo.getRealname());
        tourUserExportVo.setIdCardNumber(userInfo.getIdCardNumber());
        tourUserExportVo.setAge(DateUtil.getAge(userInfo.getIdCardNumber()));
        if (userInfo != null && userInfo.getGender() != null){
            tourUserExportVo.setGender(userInfo.getGender().toString());
        }
        UserInfo userIf = userInfoService.findByUserId(tourUser.getParentId());
        if (userIf != null){
            tourUserExportVo.setParentName(userIf.getRealname());
        }
        User user = userService.findOne(tourUser.getUserId());
        if (user != null){
            tourUserExportVo.setUserPhone(user.getPhone());
        }
        User use = userService.findOne(tourUser.getParentId());
        if (use != null){
            tourUserExportVo.setParentPhone(use.getPhone());
        }
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
        }else if (tourUser.getAuditStatus() == 0) {
            tourUserExportVo.setAuditStatus("我要报名");
        }

        if (tourUser.getFirstVisitStatus() == 1){
            tourUserExportVo.setFirstVisitStatus("审核中");
        }else if (tourUser.getFirstVisitStatus() == 2){
            tourUserExportVo.setFirstVisitStatus("审核成功");
        }else if (tourUser.getFirstVisitStatus() == 0){
            tourUserExportVo.setFirstVisitStatus("审核失败");
        }
        if (tourUser.getSecondVisitStatus() == 1){
            tourUserExportVo.setSecondVisitStatus("审核中");
        }else if (tourUser.getSecondVisitStatus() == 2){
            tourUserExportVo.setSecondVisitStatus("审核成功");
        }else if (tourUser.getSecondVisitStatus() == 0){
            tourUserExportVo.setSecondVisitStatus("审核失败");
        }
        if (tourUser.getThirdVisitStatus() == 1){
            tourUserExportVo.setThirdVisitStatus("审核中");
        }else if (tourUser.getThirdVisitStatus() == 2){
            tourUserExportVo.setThirdVisitStatus("审核成功");
        }else if (tourUser.getThirdVisitStatus() == 0){
            tourUserExportVo.setThirdVisitStatus("审核失败");
        }
        tourUserExportVo.setVisitTime(GcUtils.formatDate(tourUser.getVisitTime(), TIME_PATTERN));

        if (tourUser.getIsEffect() == 1){
            tourUserExportVo.setIsEffect("是");
        }else if (tourUser.getIsEffect() == 0){
            tourUserExportVo.setIsEffect("否");
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
        if (tourUser.getTourId() != null){
            Tour tour = tourService.findTourOne(tourUser.getTourId());
            tourUserExportVo.setTourTitle(tour.getTitle());
        }
        tourUserExportVo.setUpdateDateLabel( GcUtils.formatDate(tourUser.getUpdateDate(), TIME_PATTERN));
        if (tourUser.getCarImages() != null) {
            tourUserExportVo.setImageThumbnail(getThumbnail(tourUser.getCarImages(), 750, 450));
        }
        if (tourUser.getTourTimeId() != null){
            TourTime tourTime = tourTimeService.findOne(tourUser.getTourTimeId());
            tourUserExportVo.setTourTime(GcUtils.formatDate(tourTime.getBegintime(), S_TIME_PATTERN) + "  至  " + GcUtils.formatDate(tourTime.getEndtime(), S_TIME_PATTERN));
        }
        return tourUserExportVo;
    }

    public TourJoinUserExportVo buildJoinExportVo(TourUser tourUser) {
        TourJoinUserExportVo tourJoinUserExportVo = new TourJoinUserExportVo();
        BeanUtils.copyProperties(tourUser, tourJoinUserExportVo);
        if (tourUser.getCarImages() != null) {
            tourJoinUserExportVo.setImageThumbnail(getThumbnail(tourUser.getCarImages(), 750, 450));
        }
        UserInfo userInfo = userInfoService.findByUserId(tourUser.getUserId());
        if (userInfo != null && userInfo.getGender() != null){
            tourJoinUserExportVo.setGender(userInfo.getGender().toString());
            tourJoinUserExportVo.setUserName(userInfo.getRealname());
            tourJoinUserExportVo.setIdCardNumber(userInfo.getIdCardNumber());
            tourJoinUserExportVo.setAge(DateUtil.getAge(userInfo.getIdCardNumber()));
        }
        UserInfo userIf = userInfoService.findByUserId(tourUser.getParentId());
        if (userIf != null){
            tourJoinUserExportVo.setParentName(userIf.getRealname());
        }
        User user = userService.findOne(tourUser.getUserId());
        if (user != null){
            tourJoinUserExportVo.setUserPhone(user.getPhone());
        }
        User use = userService.findOne(tourUser.getParentId());
        if (use != null){
            tourJoinUserExportVo.setParentPhone(use.getPhone());
        }
        if (tourUser.getAuditStatus() == 1){
            tourJoinUserExportVo.setAuditStatus("审核中");
        }else if (tourUser.getAuditStatus() == 2){
            tourJoinUserExportVo.setAuditStatus("待补充");
        }else if (tourUser.getAuditStatus() == 3){
            tourJoinUserExportVo.setAuditStatus("已生效");
        }else if (tourUser.getAuditStatus() == 4){
            tourJoinUserExportVo.setAuditStatus("已完成");
        }else if (tourUser.getAuditStatus() == 5){
            tourJoinUserExportVo.setAuditStatus("审核失败");
        }else if (tourUser.getAuditStatus() == 0) {
            tourJoinUserExportVo.setAuditStatus("我要报名");
        }

        if (tourUser.getIsEffect() == 1){
            tourJoinUserExportVo.setIsEffect("是");
        }else if (tourUser.getIsEffect() == 0){
            tourJoinUserExportVo.setIsEffect("否");
        }

        if (tourUser.getIsAddBed() == 1){
            tourJoinUserExportVo.setIsAddBed("是");
        }else if (tourUser.getIsAddBed() == 0){
            tourJoinUserExportVo.setIsAddBed("否");
        }

        if (tourUser.getHouseType() == 1){
            tourJoinUserExportVo.setHouseType("标准间");
        }else if (tourUser.getHouseType() == 2){
            tourJoinUserExportVo.setHouseType("三人间");
        }

        if (tourUser.getUpdateBy() != null){
            UserInfo userI = userInfoService.findByUserId(tourUser.getUpdateBy());
            tourJoinUserExportVo.setUpdateName(userI.getRealname());
        }
        if (tourUser.getTourId() != null){
            Tour tour = tourService.findTourOne(tourUser.getTourId());
            tourJoinUserExportVo.setTourTitle(tour.getTitle());
        }
        if (tourUser.getIsTransfers() != null){
            if (tourUser.getIsTransfers() == 1){
                tourJoinUserExportVo.setIsTransfers("是");
            }else if (tourUser.getIsTransfers() == 0){
                tourJoinUserExportVo.setIsTransfers("否");
            }
        }

        if (tourUser.getIsJoin() != null){
            if (tourUser.getIsJoin() == 1){
                tourJoinUserExportVo.setIsJoin("是");
            }else if (tourUser.getIsJoin() == 0){
                tourJoinUserExportVo.setIsJoin("否");
            }
        }

        if (tourUser.getPlanTime() != null){
            tourJoinUserExportVo.setPlanTime(GcUtils.formatDate(tourUser.getPlanTime(), TIME_PATTERN));
        }
        tourJoinUserExportVo.setUpdateDateLabel( GcUtils.formatDate(tourUser.getUpdateDate(), TIME_PATTERN));
        if (tourUser.getCarImages() != null) {
            tourJoinUserExportVo.setImageThumbnail(getThumbnail(tourUser.getCarImages(), 750, 450));
        }
        if (tourUser.getTourTimeId() != null){
            TourTime tourTime = tourTimeService.findOne(tourUser.getTourTimeId());
            tourJoinUserExportVo.setTourTime(GcUtils.formatDate(tourTime.getBegintime(), S_TIME_PATTERN) + "  至  " + GcUtils.formatDate(tourTime.getEndtime(), S_TIME_PATTERN));
        }
        return tourJoinUserExportVo;
    }

    public TourUserListVo  buildListVo(TourUser tourUser) {
        TourUserListVo tourUserListVo = new TourUserListVo();
        BeanUtils.copyProperties(tourUser, tourUserListVo);
        Long tourTimeId = tourUser.getTourTimeId();
        Long parentId = tourUser.getParentId();
        if(tourTimeId != null){
            TourTime tourTime = tourTimeService.findOne(tourTimeId);
            tourUserListVo.setTourTime(GcUtils.formatDate(tourTime.getBegintime() , S_TIME_PATTERN));
            tourUserListVo.setTourTimeId(tourTimeId);
        }
        if(parentId != null){
            User user = userService.findOne(parentId);
            tourUserListVo.setParentPhone(user.getPhone());
        }
        if (tourUser.getTourId() != null){
            Tour tour = tourService.findTourOne(tourUser.getTourId());
            tourUserListVo.setTourTitle(tour.getTitle());
            tourUserListVo.setImage(tour.getImage());
        }
        return tourUserListVo ;
    }
}
