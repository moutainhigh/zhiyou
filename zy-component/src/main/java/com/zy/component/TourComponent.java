package com.zy.component;

import com.sun.tools.corba.se.idl.IncludeGen;
import com.zy.common.util.BeanUtils;
import com.zy.common.util.DateUtil;
import com.zy.entity.act.Activity;
import com.zy.entity.act.Report;
import com.zy.entity.sys.SystemCode;
import com.zy.entity.tour.*;
import com.zy.common.util.BeanUtils;
import com.zy.entity.cms.Article;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserInfo;
import com.zy.model.dto.AreaDto;
import com.zy.model.query.TourTimeQueryModel;
import com.zy.service.*;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.*;
import com.zy.util.GcUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.zy.util.GcUtils.getThumbnail;
import static java.util.Objects.isNull;

@Component
public class TourComponent {

    @Autowired
    private TourService tourService;


    @Autowired
    private UserService userService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private SystemCodeService systemCodeService;


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

    /**
     * 转化Vo
     * @param blackOrWhite
     * @return
     */
    public BlackOrWhiteAdminVo buildBlackOrWhiteAdminVo(BlackOrWhite blackOrWhite) {
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

    /**
     * 查询线路的时间信息
     * @return
     */
    public List<TourTimeVo> findTourTimeVo(Long reporId,Long tourId,String tourTime)  {
        TourTimeQueryModel tourTimeQueryModel = new TourTimeQueryModel();
        Report report = reportService.findOne(reporId);
        Date create_date = report.getCreatedTime();
        int begin = DateUtil.getMothNum(create_date);
        int endinnum = DateUtil.getMothNum(DateUtil.getMonthData(create_date,3,-1));
        int tournum = 0;
        Date date =null;
        try {
             date = new SimpleDateFormat("yyyy-MM").parse(tourTime);
            tournum= DateUtil.getMothNum(date);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (begin==tournum){
            tourTimeQueryModel.setBegintimegt(create_date);
            tourTimeQueryModel.setBegintimelt(DateUtil.getBeforeMonthEnd(create_date,1,0));
        }else if (endinnum==tournum){
            tourTimeQueryModel.setBegintimegt(DateUtil.getBeforeMonthBegin(create_date,0,0));
            tourTimeQueryModel.setBegintimelt(DateUtil.getDateEnd(create_date));
        }else{
            tourTimeQueryModel.setBegintimegt(DateUtil.getBeforeMonthBegin(date,0,0));
            tourTimeQueryModel.setBegintimelt(DateUtil.getBeforeMonthEnd(date,1,0));
        }
        tourTimeQueryModel.setTourId(tourId);
        tourTimeQueryModel.setDelfage(0);
        tourTimeQueryModel.setIsreleased(1);
        List<TourTime> timeList = tourService.findTourTime(tourTimeQueryModel);
        return this.changeVo(timeList);
    }

    /**
     * 将 tourtime 转成TourtimeVo
     * @param timeList
     * @return
     */
    public List<TourTimeVo>changeVo(List<TourTime> timeList){
        List<TourTimeVo> tourTimeVoList = new ArrayList<TourTimeVo>();
        for (TourTime tourTime:timeList){
            TourTimeVo tourTimeVo = new TourTimeVo();
            tourTimeVo.setId(tourTime.getId());
            tourTimeVo.setBeginTimeStr(GcUtils.formatDate(tourTime.getBegintime(), "MM-dd"));
            tourTimeVo.setFee(tourTime.getFee());
            tourTimeVo.setWeekStr(DateUtil.getWeek(tourTime.getBegintime()));
            tourTimeVoList.add(tourTimeVo);
        }
        return tourTimeVoList;
    }

    /**
     * 查询 userinfo
     * @param userId
     * @return
     */
    public UserInfoVo findUserInfoVo(Long userId) {
        UserInfo userInfo = userInfoService.findByUserId(userId);
        UserInfoVo userInfoVo = new UserInfoVo();
        if (userInfo!=null) {
            Long areaId = userInfo.getAreaId();
            if (areaId != null) {
                AreaDto areaDto = cacheComponent.getAreaDto(areaId);
                if (areaDto != null) {
                    userInfoVo.setProvince(areaDto.getProvince());
                    userInfoVo.setCity(areaDto.getCity());
                    userInfoVo.setDistrict(areaDto.getDistrict());
                }
            }
            userInfoVo.setAge(userInfo.getAge());
            userInfoVo.setId(userInfo.getId());
            userInfoVo.setIdCardNumber(userInfo.getIdCardNumber());
            userInfoVo.setGender(userInfo.getGender());
            userInfoVo.setRealname(userInfo.getRealname());
        }
     return userInfoVo;
    }

    /**
     * 旅游信息  级检测报告参数封装
     * @param tourUserInfoVo
     */

    public void updateOrInster(TourUserInfoVo tourUserInfoVo,Long userid) {
       //封装用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setId(tourUserInfoVo.getUserId());
        userInfo.setUserId(userid);
        userInfo.setAge(tourUserInfoVo.getAge());
        userInfo.setGender(tourUserInfoVo.getGender());
        userInfo.setAreaId(tourUserInfoVo.getAreaId());
        userInfo.setIdCardNumber(tourUserInfoVo.getIdCartNumber());
        userInfo.setRealname(tourUserInfoVo.getRealname());
        userInfo.setRealFlag(0);
        //封装用户旅游信息
        TourUser tourUser = new TourUser();
        tourUser.setTourId(tourUserInfoVo.getTourId());
        tourUser.setUserId(userid);
        tourUser.setParentId(tourUserInfoVo.getParentId());
        tourUser.setTourTimeId(tourUserInfoVo.getTourTimeId());
        tourUser.setIsEffect(1);
        tourUser.setHouseType(tourUserInfoVo.getHouseType());
        tourUser.setUserRemark(tourUserInfoVo.getUserRemark());
        tourUser.setIsTransfers(1);
        tourUser.setAuditStatus(1);
        tourUser.setReportId(tourUserInfoVo.getReporId());
        tourUser.setSequenceId(this.getgetNextTourID());
        tourService.updateOrInster(userInfo,tourUser);
    }

    private static final Map<String,Object>  provinceMap= new HashMap<String, Object>(){{
        put("11","1");
        put("12","20");
        put("31","860");
        put("51","2462");
        put("15","375");
        put("65","3393");
        put("54","2983");
        put("64","3360");
        put("45","2292");
        put("23","706");
        put("22","628");
        put("21","499");
        put("13","39");
        put("14","233");
        put("63","3307");
        put("37","1484");
        put("41","1656");
        put("32","880");
        put("34","1119");
        put("33","1006");
        put("35","1257");
        put("36","1361");
        put("43","1981");
        put("42","1851");
        put("44","2131");
        put("71","3510");
        put("46","2431");
        put("62","3194");
        put("61","3066");
        put("51","2503");
        put("53","2829");
        put("81","3511");
        put("82","3512");
      /*  put("11","北京市");
        put("12","天津市");
        put("31","上海市");
        put("51","重庆");
        put("15","内蒙");
        put("65","新疆");
        put("54","西藏");
        put("64","宁夏");
        put("45","广西");
        put("23","黑龙江省");
        put("22","吉林省");
        put("21","辽宁省");
        put("13","河北省");
        put("14","山西省");
        put("63","青海省");
        put("37","山东省");
        put("41","河南省");
        put("32","江苏省");
        put("34","安徽省");
        put("33","浙江省");
        put("35","福建省");
        put("36","江西省");
        put("43","湖南省");
        put("42","湖北省");
        put("44","广东省");
        put("71","台湾省");
        put("46","海南省");
        put("62","甘肃省");
        put("61","陕西省");
        put("51","四川省");
        put("53","云南省");
        put("81","香港特别行政区");
        put("82","澳门特别行政区");
        */

    }};

    /**
     * 检测参数
     * @param tourUserInfoVo
     * @return
     */
    public String checkParam(TourUserInfoVo tourUserInfoVo) {
        int max =0;
        int min =0;
        //取到最大值
        SystemCode systemCode = systemCodeService.findByTypeAndName("TOURLIMITAGE", "MAX");
        if (systemCode==null||(systemCode.getSystemValue()==null||"".equals(systemCode.getSystemValue()))){
          return "系统产数配置异常";
        }else{
            try{
                max = Integer.valueOf(systemCode.getSystemValue());
            }catch (Exception e){
                e.printStackTrace();
                return "系统产数配置异常";
            }
        }
        //取到最小值
        SystemCode systemCodeMin = systemCodeService.findByTypeAndName("TOURLIMITAGE", "MIN");
        if (systemCodeMin==null||(systemCodeMin.getSystemValue()==null||"".equals(systemCodeMin.getSystemValue()))){
            return "系统产数配置异常";
        }else{
            try{
                min = Integer.valueOf(systemCodeMin.getSystemValue());
            }catch (Exception e){
                e.printStackTrace();
                return "系统产数配置异常";
            }
        }
        if(tourUserInfoVo.getAge()>max){
            return "抱歉！您以超过最大年龄";
        }
        if(tourUserInfoVo.getAge()<min){
            return "抱歉！您尚未满足最小年龄";
        }

        //检测地区  本省不能参加本省的
        Long areaId =null;
        if (tourUserInfoVo.getAreaId()!=null){
            areaId=tourUserInfoVo.getAreaId();
        }else{
            if (tourUserInfoVo.getUserId()!=null){
                UserInfo userInfo = userInfoService.findByUserId(tourUserInfoVo.getUserId());
                if (userInfo!=null){
                    areaId=  userInfo.getAreaId();
                }
            }
        }

       Long  provinceId =null;
        if (areaId!=null){
            AreaDto areaDto = cacheComponent.getAreaDto(areaId);
            if (areaDto != null) {
                areaDto.getProvinceId();
            }
        }


        return null;
    }
}
