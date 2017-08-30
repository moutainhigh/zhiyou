package com.zy.task.job;

import com.zy.common.exception.ConcurrentException;
import com.zy.common.util.BeanUtils;
import com.zy.common.util.DateUtil;
import com.zy.entity.mal.Order;
import com.zy.entity.report.TeamProvinceReport;
import com.zy.entity.sys.Area;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserInfo;
import com.zy.model.query.AreaQueryModel;
import com.zy.model.query.OrderQueryModel;
import com.zy.model.query.UserInfoQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.*;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * Created by liang on 2017/8/29.  处理 省份服务商报表
 */
public class TeamProvinceReportJob implements Job {
    private Logger logger = LoggerFactory.getLogger(TeamProvinceReportJob.class);

    @Autowired
    private AreaService areaService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TeamProvinceReportService teamProvinceReportService;


//
//    private List<UserReportVo> userReportVos = new ArrayList<>();

    private Map<Long, Area> areaMap = new HashMap<>();

    private Map<Long, UserInfo> userInfoMap = new HashMap<>();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }


    /**
     * job 开始
     * @param jobExecutionContext
     * @throws JobExecutionException
     */

//    @Override
//    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        logger.info("TeamProvinceReportJob begin.......");
//           this.disposeTeamProvinceReport();
//        logger.info("TeamProvinceReportJob end.......");
//    }

    //开始处理逻辑
//    private void disposeTeamProvinceReport(){
//        try {
//            AreaQueryModel areaQueryModel = new AreaQueryModel();
//            List<Area> areas = areaService.findAll(areaQueryModel);
//            areas = areas.stream().filter(v -> v.getAreaType() == Area.AreaType.省).collect(Collectors.toList());
//            UserQueryModel userQueryModel = new UserQueryModel();
//            List<User> users = userService.findAll(userQueryModel);
//            OrderQueryModel orderQueryModel = new OrderQueryModel();
//            List<Order> orders = orderService.findAll(orderQueryModel);
//            userInfoMap.putAll(userInfoService.findAll(UserInfoQueryModel.builder().build()).stream().collect(Collectors.toMap(v -> v.getUserId(), v -> v)));
//            users = users.stream().filter(v -> v.getUserRank() == User.UserRank.V4 || v.getUserRank() == User.UserRank.V3).collect(Collectors.toList());
//
//            userReportVos = users.stream().map(user -> {
//                UserReportVo userReportVo = new UserReportVo();
//                BeanUtils.copyProperties(user, userReportVo);
//                Long userId = user.getId();
//                UserInfo userInfo = userInfoMap.get(userId);
//                if (userInfo != null) {
//                    Long areaId = userInfo.getAreaId();
//                    if (areaId != null) {
//                        Long districtId = areaId;
//                        Area district = areaMap.get(districtId);
//                        if (district != null && district.getAreaType() == Area.AreaType.区) {
//                            userReportVo.setDistrictId(districtId);
//                            Long cityId = district.getParentId();
//                            Area city = areaMap.get(cityId);
//                            if (city != null && city.getAreaType() == Area.AreaType.市) {
//                                userReportVo.setCityId(cityId);
//                                Long provinceId = city.getParentId();
//                                Area province = areaMap.get(provinceId);
//                                if (province != null && province.getAreaType() == Area.AreaType.省) {
//                                    userReportVo.setProvinceId(provinceId);
//                                }
//                            }
//                        }
//                    }
//                }
//                return userReportVo;
//            }).collect(Collectors.toList());
//            userReportVos = userReportVos.stream().filter(v -> v.getParentId() != null).collect(Collectors.toList());
//
//            LocalDate now = LocalDate.now();
//            LocalDate lastDate = now.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
//            LocalDateTime localDateTime = LocalDateTime.of(lastDate, LocalTime.parse("23:59:59"));
//            Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
//            Date end = Date.from(instant);
//
//            lastDate = now.minusMonths(3).with(TemporalAdjusters.firstDayOfMonth());
//            localDateTime = LocalDateTime.of(lastDate, LocalTime.parse("00:00:00"));
//            instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
//            Date begin = Date.from(instant);
//
//            List<Order> filterOrders = orders.stream()
//                    .filter(order -> order.getOrderStatus() == Order.OrderStatus.已完成
//                            || order.getOrderStatus() == Order.OrderStatus.已支付 || order.getOrderStatus() == Order.OrderStatus.已发货)
//                    .filter(order -> order.getBuyerUserRank() == User.UserRank.V4)
//                    .filter(order -> order.getPaidTime().after(begin) && order.getPaidTime().before(end))
//                    .collect(Collectors.toList());
//            Map<Long, List<Order>> orderMap = filterOrders.stream().collect(Collectors.groupingBy(Order::getUserId));
//            Map<Long, TeamProvinceReport> map = areas.stream().collect(Collectors.toMap(v -> v.getId(), v -> {
//                TeamProvinceReport teamProvinceReport = new TeamProvinceReport();
//                teamProvinceReport.setCreateTime(new Date());
//                teamProvinceReport.setMonth(DateUtil.getMothNum(DateUtil.getBeforeMonthBegin(new Date(),-1,0)));
//                teamProvinceReport.setYear(DateUtil.getYear(DateUtil.getBeforeMonthBegin(new Date(),-1,0)));
//                teamProvinceReport.setV3Number(0);
//                teamProvinceReport.setV4Number(0);
//                teamProvinceReport.setV4ActiveNumber(0);
//                teamProvinceReport.setProvince(v.getName());
//                teamProvinceReport.setV4ActiveRank(0);
//                teamProvinceReport.setV4ActiveRate(0.00);
//                return teamProvinceReport;
//            }));
//            for (UserReportVo userReportVo : userReportVos) {
//                TeamProvinceReport teamProvinceReport = map.get(userReportVo.getProvinceId());
//                if (userReportVo.getUserRank() == User.UserRank.V4) {
//                    teamProvinceReport.setV4Number(teamProvinceReport.getV4Number() + 1);
//                } else if (userReportVo.getUserRank() == User.UserRank.V3) {
//                    teamProvinceReport.setV3Number(teamProvinceReport.getV3Number() + 1);
//                }
//                if (orderMap.get(userReportVo.getId()) != null) {
//                    teamProvinceReport.setV4ActiveNumber(teamProvinceReport.getV4ActiveNumber() + 1);
//                }
//                map.put(userReportVo.getProvinceId(), teamProvinceReport);
//            }
//
//            List<TeamProvinceReport> teamProvinceReports = new ArrayList<>(map.values());
//            for (TeamProvinceReport t:teamProvinceReports) {
//                if(t.getV4Number() != null && t.getV4Number() != 0){
//                    t.setV4ActiveRate(DateUtil.formatDouble(t.getV4ActiveNumber() / t.getV4Number() * 100));
//                }else {
//                    t.setV4ActiveRate(0.00);
//                }
//            }
//            //对list 进行排序
//            teamProvinceReports.sort((TeamProvinceReport h1, TeamProvinceReport h2) -> h2.getV4ActiveRate().compareTo(h1.getV4ActiveRate()));
//            int i =1;
//            Double rate = 0.00;
//            for(TeamProvinceReport tp :teamProvinceReports) {
//
//                if (tp.getV4ActiveRate()<rate){
//                    rate = tp.getV4ActiveRate();
//                    i++;
//                }
//                tp.setV4ActiveRank(i);
//                if (rate == 0.00){
//                    rate = tp.getV4ActiveRate();
//                }
//                teamProvinceReportService.insert(tp);
//            }
//        } catch (ConcurrentException e) {
//                try {
//                    TimeUnit.SECONDS.sleep(2);} catch (InterruptedException e1) {}
//                    this.disposeTeamProvinceReport();
//        }catch (Exception e){
//            e.printStackTrace();
//            logger.error(e.getMessage(), e);
//        }
//    }
}
