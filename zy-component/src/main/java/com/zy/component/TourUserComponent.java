package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.tour.Tour;
import com.zy.entity.tour.TourUser;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserInfo;
import com.zy.service.TourService;
import com.zy.service.UserInfoService;
import com.zy.service.UserService;
import com.zy.util.GcUtils;
import com.zy.vo.TourUserAdminVo;
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

    @Autowired
    private UserService userService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private TourService tourService;

//    @Autowired
//    private TourService tourService;

    public TourUserAdminVo buildAdminVo(TourUser tourUser, boolean b) {
        TourUserAdminVo tourUserAdminVo = new TourUserAdminVo();
        BeanUtils.copyProperties(tourUser, tourUserAdminVo);
        UserInfo userInfo = userInfoService.findOne(tourUser.getUserId());
        tourUserAdminVo.setUserName(userInfo.getRealname());
        UserInfo userIf = userInfoService.findOne(tourUser.getParentId());
        tourUserAdminVo.setParentName(userIf.getRealname());
        if (tourUser.getUpdateBy() != null){
            UserInfo userI = userInfoService.findOne(tourUser.getUpdateBy());
            tourUserAdminVo.setUpdateName(userIf.getRealname());
        }
        Tour tour = tourService.findTourOne(tourUser.getTourId());
        tourUserAdminVo.setTourTitle(tour.getTitle());
        tourUserAdminVo.setCreatedDateLabel( GcUtils.formatDate(tourUser.getCreateDate(), TIME_PATTERN));
        tourUserAdminVo.setUpdateDateLabel( GcUtils.formatDate(tourUser.getUpdateDate(), TIME_PATTERN));
        tourUserAdminVo.setPlanTimeLabel( GcUtils.formatDate(tourUser.getPlanTime(), TIME_PATTERN));
        if (tourUser.getCarImages() != null) {
            tourUserAdminVo.setImageThumbnail(getThumbnail(tourUser.getCarImages(), 750, 450));
        }
        return tourUserAdminVo;
    }
}
