package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.act.Activity;
import com.zy.entity.tour.BlackOrWhite;
import com.zy.common.util.BeanUtils;
import com.zy.entity.cms.Article;
import com.zy.entity.tour.Sequence;
import com.zy.entity.tour.TourTime;
import com.zy.entity.usr.User;
import com.zy.entity.tour.Tour;
import com.zy.model.dto.AreaDto;
import com.zy.service.TourService;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.*;
import com.zy.service.UserService;
import com.zy.util.GcUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.zy.util.GcUtils.getThumbnail;
import static java.util.Objects.isNull;

@Component
public class TourComponent {

    @Autowired
    private TourService tourService;


    @Autowired
    private UserService userService;


    private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm";

    @Autowired
    private CacheComponent cacheComponent;
    /**
     * 处理生成 旅游编号
     生成规则：T+YY(两位的年份)+NUM(7位的数字)
     */
    public String getgetNextTourID(){
        String year = dateToStr(new Date(),"yy");
        String newSeq = this.getNewSeq("seq_tour_num",7,year,1);
        return  "T"+year+newSeq;
    }


    /**
     * 查询 新的seq 并格式化成指定格式
     * @param seqName （seq 的那么）
     * @param length （格式化  多长）
     * @param year
     * @param incrementval 增长长度
     * @return
     */
    private  String getNewSeq( String seqName,int length,String year,int incrementval) {
        String returnStr = "";
        Sequence sequence = tourService.findSequenceOne(seqName,year);
        if (sequence == null ) {
            returnStr = "1";
            Sequence sequenceIn = new Sequence();
            sequenceIn.setSequenceName(seqName);
            sequenceIn.setSequenceType(year);
            sequenceIn.setCurrentVal(1L);
            sequenceIn.setIncrementval(incrementval);
            tourService.create(sequenceIn);
        } else {
            int oldSeq = sequence.getCurrentVal().intValue();
            int newSeq = oldSeq + sequence.getIncrementval();
            returnStr=newSeq+"";
            sequence.setCurrentVal(new Long(newSeq));
            tourService.updateSequence(sequence);
        }
        int seqLength =returnStr.length();
        if (seqLength < length) {
            int moreZerolength = length - seqLength;
            String moreZeroStr = "";
            for (int i = 1; i <= moreZerolength; i++) {
                moreZeroStr += "0";
            }
            returnStr = moreZeroStr + returnStr;
        }
        return returnStr;
    }


    /**
     * 格式化时间 成字符串
     * @param date
     * @param format
     * @return
     */
    public static String dateToStr(Date date, String format) {
        if (null == date) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 转成VO
     * @param tour
     * @param b
     * @return
     */
    public TourAdminVo buildAdminVo(Tour tour, boolean b) {
        TourAdminVo tourAdminVo = new TourAdminVo();
        BeanUtils.copyProperties(tour, tourAdminVo);
        tourAdminVo.setCreatedTime( GcUtils.formatDate(tour.getCreatedTime(), TIME_PATTERN));
        tourAdminVo.setUpdateTime( GcUtils.formatDate(tour.getUpdateTime(), TIME_PATTERN));
        if (b) {
            tourAdminVo.setImage(getThumbnail(tour.getImage(), 750, 450));
        }
        tourAdminVo.setCreateName(userService.findRealName(tour.getCreateby()));
        return tourAdminVo;
    }

    public BlackOrWhiteAdminVo buildblackOrWhiteAdminVo(BlackOrWhite blackOrWhite) {
        BlackOrWhiteAdminVo blackOrWhiteAdminVo = new BlackOrWhiteAdminVo();
        BeanUtils.copyProperties(blackOrWhite, blackOrWhiteAdminVo);
        Long userId = blackOrWhite.getUserId();
        if (userId != null) {
            User user = cacheComponent.getUser(userId);
            blackOrWhiteAdminVo.setUser(VoHelper.buildUserAdminSimpleVo(user));
        }
        return blackOrWhiteAdminVo;

    }

    /**
     * 将tourTime转成Vo
     * @param tourTime
     * @param b
     * @return
     */
    public TourTimeDetailVo buildTourTimeVo(TourTime tourTime, boolean b) {
        TourTimeDetailVo tourTimeDetailVo = new TourTimeDetailVo();
        BeanUtils.copyProperties(tourTime, tourTimeDetailVo);
        tourTimeDetailVo.setBegintimeLible(GcUtils.formatDate(tourTime.getBegintime(), TIME_PATTERN));
        tourTimeDetailVo.setEndtimeLible(GcUtils.formatDate(tourTime.getEndtime(), TIME_PATTERN));
        tourTimeDetailVo.setCreatedTimeLible(GcUtils.formatDate(tourTime.getCreatedTime(), TIME_PATTERN));
        tourTimeDetailVo.setCreateby(userService.findRealName(tourTime.getCreateby()));
        tourTimeDetailVo.setStarAddress(tourTime.getStarAddress());
        if(!isNull(tourTime.getAreaId())) {
            AreaDto areaDto = cacheComponent.getAreaDto(tourTime.getAreaId());
            if (areaDto != null) {
                tourTimeDetailVo.setProvince(areaDto.getProvince());
                tourTimeDetailVo.setCity(areaDto.getCity());
                tourTimeDetailVo.setDistrict(areaDto.getDistrict());
            }
        }
      return tourTimeDetailVo;
    }

    /**
     * 将vo转成实体
     * @param tourTimeVo
     * @return
     */
    public TourTime buildTourTime(TourTimeVo tourTimeVo) {
        TourTime tourTime = new TourTime();
        tourTime.setTrouId(tourTimeVo.getTourId());
        tourTime.setBegintime(tourTimeVo.getBegintime());
        tourTime.setEndtime(tourTimeVo.getEndtime());
        tourTime.setFee(tourTimeVo.getFee());
        tourTime.setIsreleased(tourTimeVo.getIsreleased());
        tourTime.setDelflag(0);
        tourTime.setAreaId(tourTimeVo.getAreaId());
         tourTime.setStarAddress(tourTimeVo.getAddress());
        tourTime.setCreatedTime(new Date());
        tourTime.setCreateby(tourTimeVo.getCreateby());
        return tourTime;
    }


}
