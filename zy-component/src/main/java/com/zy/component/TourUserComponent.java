package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.tour.TourUser;
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

    public TourUserAdminVo buildAdminVo(TourUser tourUser, boolean b) {
        TourUserAdminVo tourUserAdminVo = new TourUserAdminVo();
        BeanUtils.copyProperties(tourUser, tourUserAdminVo);
        tourUserAdminVo.setCreatedDateLabel( GcUtils.formatDate(tourUser.getCreateDate(), TIME_PATTERN));
        tourUserAdminVo.setUpdateDateLabel( GcUtils.formatDate(tourUser.getUpdateDate(), TIME_PATTERN));
        tourUserAdminVo.setPlanTimeLabel( GcUtils.formatDate(tourUser.getPlanTime(), TIME_PATTERN));
        if (tourUser.getCarImages() != null) {
            tourUserAdminVo.setImageThumbnail(getThumbnail(tourUser.getCarImages(), 750, 450));
        }
        return tourUserAdminVo;
    }
}
