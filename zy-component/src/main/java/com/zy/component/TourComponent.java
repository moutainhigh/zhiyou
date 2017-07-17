package com.zy.component;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.sun.tools.corba.se.idl.IncludeGen;
import com.zy.common.model.query.Page;
import com.zy.common.util.BeanUtils;
import com.zy.common.util.DateUtil;
import com.zy.entity.act.Activity;
import com.zy.entity.act.Policy;
import com.zy.entity.act.PolicyCode;
import com.zy.entity.act.Report;
import com.zy.entity.sys.SystemCode;
import com.zy.entity.tour.*;
import com.zy.common.util.BeanUtils;
import com.zy.entity.cms.Article;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserInfo;
import com.zy.model.dto.AreaDto;
import com.zy.model.query.*;
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

    @Autowired
    private BlackOrWhiteService blackOrWhiteService;

    @Autowired
    private PolicyCodeService policyCodeService;

    @Autowired
    private PolicyService policyService;

    private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm";
    private static final String S_PATTERN = "yyyy-MM-dd";

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
            tourTimeQueryModel.setBegintimegt(DateUtil.getBeforeMonthBegin(date,0,0));
            tourTimeQueryModel.setBegintimelt(DateUtil.getMonthData(create_date,3,-1));
        }else{
            tourTimeQueryModel.setBegintimegt(DateUtil.getBeforeMonthBegin(date,0,0));
            tourTimeQueryModel.setBegintimelt(DateUtil.getBeforeMonthEnd(date,1,0));
        }
        tourTimeQueryModel.setTourId(tourId);
        tourTimeQueryModel.setDelfage(0);
        tourTimeQueryModel.setIsreleased(1);
        List<TourTime> timeList = tourService.findTourTime(tourTimeQueryModel);
        return this.changeVo(timeList,true);
    }

    /**
     * 将 tourtime 转成TourtimeVo
     * @param timeList
     * @return
     */
    public List<TourTimeVo>changeVo(List<TourTime> timeList , Boolean flage){
        List<TourTimeVo> tourTimeVoList = new ArrayList<TourTimeVo>();
        for (TourTime tourTime:timeList){
            //开始时间必须大于今天
            if(flage) {
                if (tourTime.getBegintime().getTime()>new Date().getTime()) { //DateUtil.calculateDiffDays(new Date(), tourTime.getBegintime()) > 0
                    TourTimeVo tourTimeVo = new TourTimeVo();
                    tourTimeVo.setId(tourTime.getId());
                    tourTimeVo.setBeginTimeStr(GcUtils.formatDate(tourTime.getBegintime(), "MM-dd"));
                    tourTimeVo.setFee(tourTime.getFee());
                    tourTimeVo.setWeekStr(DateUtil.getWeek(tourTime.getBegintime()));
                    tourTimeVoList.add(tourTimeVo);
                }
            }else{
                TourTimeVo tourTimeVo = new TourTimeVo();
                tourTimeVo.setId(tourTime.getId());
                tourTimeVo.setBeginTimeStr(GcUtils.formatDate(tourTime.getBegintime(), "MM-dd"));
                tourTimeVo.setFee(tourTime.getFee());
                tourTimeVo.setWeekStr(DateUtil.getWeek(tourTime.getBegintime()));
                tourTimeVoList.add(tourTimeVo);
            }
        }
        return tourTimeVoList;
    }

    /**
     * 查询 userinfo
     * @param userId
     * @return
     */
    public UserInfoVo findUserInfoVo(Long userId) {
        UserInfo userInfo = userInfoService.findByUserIdandFlage(userId);
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

            userInfoVo.setUserId(userInfo.getUserId());
            userInfoVo.setAge(userInfo.getAge());
            userInfoVo.setId(userInfo.getId());
            userInfoVo.setIdCardNumber(userInfo.getIdCardNumber());
            userInfoVo.setImage1Thumbnail(getThumbnail(userInfo.getImage1(), 750, 450));
            userInfoVo.setBirthdayLabel(GcUtils.formatDate(userInfo.getBirthday(), S_PATTERN));
            userInfoVo.setGender(userInfo.getGender());
            userInfoVo.setRealname(userInfo.getRealname());
        }
     return userInfoVo;
    }


    /**
     * 查询 userinfo
     * @param userId
     * @return
     */
    public UserInfoVo findUserInfoVo(Long userId,Long reporId) {
        UserInfo userInfo = userInfoService.findByUserIdandFlage(userId);
        UserInfoVo userInfoVo = new UserInfoVo();
        Long areaId=null;
        if (userInfo!=null) {
            areaId = userInfo.getAreaId();
            userInfoVo.setUserId(userInfo.getUserId());
            userInfoVo.setAgestr(userInfo.getAge()==null?"":userInfo.getAge()+"");
            userInfoVo.setId(userInfo.getId());
            userInfoVo.setIdCardNumber(userInfo.getIdCardNumber()==null?"":userInfo.getIdCardNumber());
            userInfoVo.setImage1Thumbnail(getThumbnail(userInfo.getImage1(), 750, 450));
            userInfoVo.setBirthdayLabel(GcUtils.formatDate(userInfo.getBirthday(), S_PATTERN));
            userInfoVo.setGender(userInfo.getGender());
            userInfoVo.setRealname(userService.findRealName(userId));
        }else{
            userInfoVo.setAgestr("");
            userInfoVo.setIdCardNumber("");
            userInfoVo.setRealname("");
        }
        Report report = reportService.findOne(reporId);
        if (areaId == null) {
            areaId= report.getAreaId();
        }
        userInfoVo.setAreaId(areaId);
        if (userInfoVo.getRealname()!=null){
            userInfoVo.setRealname(report.getRealname());
        }
        AreaDto areaDto = cacheComponent.getAreaDto(areaId);
        if (areaDto != null) {
            userInfoVo.setProvince(areaDto.getProvince());
            userInfoVo.setCity(areaDto.getCity());
            userInfoVo.setDistrict(areaDto.getDistrict());
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
        if (tourUserInfoVo.getId()!=null&&!"null".equals(tourUserInfoVo.getId())){
         userInfo.setId(Long.valueOf(tourUserInfoVo.getId()));
        }
        userInfo.setUserId(userid);
        userInfo.setAge(tourUserInfoVo.getAges());
        userInfo.setGender(tourUserInfoVo.getGenders());
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
        tourUser.setIsAddBed(0);
        tourUser.setIsJoin(0);
        tourUser.setSequenceId(this.getgetNextTourID());
        tourService.updateOrInster(userInfo,tourUser,tourUserInfoVo.getProductNumber());
    }

    private static final Map<String,Long>  provinceMap= new HashMap<String, Long>(){{
        put("11",1L);
        put("12",20L);
        put("31",860L);
        put("51",2462L);
        put("15",375L);
        put("65",3393L);
        put("54",2983L);
        put("64",3360L);
        put("45",2292L);
        put("23",706L);
        put("22",628L);
        put("21",499L);
        put("13",39L);
        put("14",233L);
        put("63",3307L);
        put("37",1484L);
        put("41",1656L);
        put("32",880L);
        put("34",1119L);
        put("33",1006L);
        put("35",1257L);
        put("36",1361L);
        put("43",1981L);
        put("42",1851L);
        put("44",2131L);
        put("71",3510L);
        put("46",2431L);
        put("62",3194L);
        put("61",3066L);
        put("51",2503L);
        put("53",2829L);
        put("81",3511L);
        put("82",3512L);
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
    public String[] checkParam(TourUserInfoVo tourUserInfoVo) {
        int max =0;
        int min =0;
        //取到最大值
        SystemCode systemCode = systemCodeService.findByTypeAndName("TOURLIMITAGE", "MAX");
        if (systemCode==null||(systemCode.getSystemValue()==null||"".equals(systemCode.getSystemValue()))){
          return new String[]{"系统产数配置异常","0"};
        }else{
            try{
                max = Integer.valueOf(systemCode.getSystemValue());
            }catch (Exception e){
                e.printStackTrace();
                return new String[]{"系统产数配置异常","0"};
            }
        }
        //取到最小值
        SystemCode systemCodeMin = systemCodeService.findByTypeAndName("TOURLIMITAGE", "MIN");
        if (systemCodeMin==null||(systemCodeMin.getSystemValue()==null||"".equals(systemCodeMin.getSystemValue()))){
            return new String[]{"系统产数配置异常","0"};
        }else{
            try{
                min = Integer.valueOf(systemCodeMin.getSystemValue());
            }catch (Exception e){
                e.printStackTrace();
                return new String[]{"系统产数配置异常","0"};
            }
        }
        if(tourUserInfoVo.getAges()>= max&&tourUserInfoVo.getAges()<65){
            return  new String[]{"27岁以下与59岁至65岁者另加960元/人；儿童另加960元且门票住宿自理；如有疑问可与推荐人联系。","1"};
        }
        if(tourUserInfoVo.getAges()>=66){
            return new String[]{"66岁以上：因旅游公司不具备接待服务条件，暂不接受66岁以上客户，敬请谅解。","0"};
        }
        if(tourUserInfoVo.getAges()<=min){
            return  new String[]{"27岁以及下与59岁至65岁者另加960元/人；儿童另加960元且门票住宿自理；如有疑问可与推荐人联系。","1"};
        }

        //检测地区  本省不能参加本省的
      /*  Long areaId =null;
        TourTime tourTime = tourService.findTourTimeOne(tourUserInfoVo.getTourTimeId());
        if (tourTime!=null){
            areaId  = tourTime.getAreaId();
        }
       Long  provinceId =null;
        if (areaId!=null){
            AreaDto areaDto = cacheComponent.getAreaDto(areaId);
            if (areaDto != null) {
                provinceId= areaDto.getProvinceId();
            }
        }
         String  pid =tourUserInfoVo.getIdCartNumber().substring(0,2);
         if (provinceId==provinceMap.get(pid)){
             return "抱歉！不能参加户籍所在地旅游";
         }*/
        return null;
    }

    /**
     * 检测是否还可以申请 旅游
     * @param reportId
     * @return
     */
    public String checkTour(String reportId,Long userId) {
        TourUserQueryModel tourUserQueryModel = new TourUserQueryModel();
        tourUserQueryModel.setCreatedTime(DateUtil.getCurrYearFirst());
        tourUserQueryModel.setUserId(userId);
        tourUserQueryModel.setIsEffect(1);
        Page<TourUser> page = tourService.findAll(tourUserQueryModel);
        SystemCode systemCode = systemCodeService.findByTypeAndName("TOURAPPLYNUMBER", "TOUR");
        if (systemCode==null||(systemCode.getSystemValue()==null||"".equals(systemCode.getSystemValue()))){
            return "系统产数配置异常";
        }else{
            try{
              int  min = Integer.valueOf(systemCode.getSystemValue());
                if (page.getTotal()>=min){
                    return "申请旅游已经达到上限";

                }
            }catch (Exception e){
                e.printStackTrace();
                return "系统产数配置异常";
            }
        }
        tourUserQueryModel.setCreatedTime(null);
        tourUserQueryModel.setUserId(null);
        tourUserQueryModel.setReportId(Long.valueOf(reportId));
        Page<TourUser> page1 = tourService.findAll(tourUserQueryModel);
        if (page1!=null&&page1.getTotal()!=null&&page1.getTotal()>0){
            return "已经申请过";
        }
      if (!this.checkTourTime(reportId)){
          return "检测报告信息过期";
      }
        return null;
    }

    /**
     * 检测报告是否在3个月内
     * @param reportId
     * @return
     */
    public Boolean checkTourTime(String reportId){
        Report report = reportService.findOne(Long.valueOf(reportId));
        Date reportDate =DateUtil.getMonthData(report.getCreatedTime(),3,-1);
        Date newDate = new Date();
        if (newDate.getTime()>reportDate.getTime()) {
           return false;
        }
        return true;
    }

    public String checkReport(Long reportId, Long loginUserId) {
        ReportQueryModel reportQueryModel = new ReportQueryModel();
        reportQueryModel.setIdEQ(reportId);
        reportQueryModel.setUserIdEQ(loginUserId);
        Report report = reportService.findReport(reportQueryModel);
        if (report == null){
            return "还没有提交检测报告或者已经申请过保险";
        }
        return null;
    }

    /**
     * 查询 推荐人  可带人数量
     * @param userId
     * @return
     */
    public String  checkParetNumber(Long userId ,Long tourTimeId) {
        String   result = null;
        //推荐人检测
        int num=0;
        BlackOrWhite blackOrWhite=blackOrWhiteService.findByUserId(userId);
        if (blackOrWhite!=null){
            num= blackOrWhite.getNumber();
        }else {
            SystemCode systemCodewb = systemCodeService.findByTypeAndName("BLACKORWHITENUMBER", "BLACKORWHITE");
            if (systemCodewb == null || (systemCodewb.getSystemValue() == null || "".equals(systemCodewb.getSystemValue()))) {
                num = 8;
            } else {
              try{
                  num = Integer.valueOf(systemCodewb.getSystemValue());

               }catch (Exception e){
                  e.printStackTrace();
                  num =8;
              }
            }
        }
        TourUserQueryModel tourUserQueryModel = new TourUserQueryModel();
        tourUserQueryModel.setCreatedTime(DateUtil.getCurrYearFirst());
        tourUserQueryModel.setParentId(userId);
        tourUserQueryModel.setTourTimeId(tourTimeId);
        tourUserQueryModel.setIsEffect(1);
        Page<TourUser> page = tourService.findAll(tourUserQueryModel);
        if(page!=null&&page.getTotal()!=null&&page.getTotal()>num){
            result ="申请旅游人数已经超过上线" ;
        }
        int min =15;
        //判断所选路线是否相差15天
        SystemCode systemCodewb = systemCodeService.findByTypeAndName("TOURTIMEBAD", "MIN");
        if (systemCodewb == null || (systemCodewb.getSystemValue() == null || "".equals(systemCodewb.getSystemValue()))) {
            min =15;
        } else {
            try{
                min = Integer.valueOf(systemCodewb.getSystemValue());

            }catch (Exception e){
                e.printStackTrace();
                min =15;
            }
        }

        TourTime tourTime = tourService.findTourTimeOne(tourTimeId);
        int das = DateUtil.calculateDiffDays(new Date(),tourTime.getBegintime());
        if (das<min){
            result ="申请旅游时间要提前"+min+"天" ;
        }
        return  result;
    }

    /**
     * 查询产品编号
     * @param reporId
     * @return
     */
    public String findproductNumber(Long reporId) {
        String result=null;
        Report report = reportService.findOne(reporId);
            if (report!=null&&report.getProductNumber()==null){
                PolicyQueryModel policyQueryModel = new PolicyQueryModel();
                policyQueryModel.setReportIdEQ(reporId);
               List<Policy> policyList = policyService.findAll(policyQueryModel);
               if (policyList!=null&&!policyList.isEmpty()){
                   Policy policy = policyList.get(0);
                   result= policy.getCode();
               }
            }else if (report!=null){
                result=  report.getProductNumber();
            }
      return result;
    }
}
