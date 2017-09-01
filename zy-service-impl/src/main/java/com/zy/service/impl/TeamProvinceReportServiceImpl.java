package com.zy.service.impl;

import com.zy.common.model.query.Page;
import com.zy.common.util.DateUtil;
import com.zy.entity.mal.Order;
import com.zy.entity.report.TeamProvinceReport;
import com.zy.entity.sys.Area;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserInfo;
import com.zy.mapper.TeamProvinceReportMapper;
import com.zy.model.query.*;
import com.zy.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by liang on 2017/8/28.
 */
@Service
@Validated
public class TeamProvinceReportServiceImpl implements TeamProvinceReportService {

    @Autowired
    private TeamProvinceReportMapper teamProvinceReportMapper;

    @Autowired
    private AreaService areaService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;


    @Override
    public Page<TeamProvinceReport> findPage(TeamProvinceReportQueryModel teamProvinceReportQueryModel) {
        if (teamProvinceReportQueryModel.getPageNumber() == null)
            teamProvinceReportQueryModel.setPageNumber(0);
        if (teamProvinceReportQueryModel.getPageSize() == null)
            teamProvinceReportQueryModel.setPageSize(20);
        long total = teamProvinceReportMapper.count(teamProvinceReportQueryModel);
        List<TeamProvinceReport> data = teamProvinceReportMapper.findAll(teamProvinceReportQueryModel);
        Page<TeamProvinceReport> page = new Page<>();
        page.setPageNumber(teamProvinceReportQueryModel.getPageNumber());
        page.setPageSize(teamProvinceReportQueryModel.getPageSize());
        page.setData(data);
        page.setTotal(total);
        return page;
    }

    @Override
    public List<TeamProvinceReport> findExReport(TeamProvinceReportQueryModel teamProvinceReportQueryModel) {
        return teamProvinceReportMapper.findAll(teamProvinceReportQueryModel);
    }

    @Override
    public void insert(TeamProvinceReport teamProvinceReport) {
        teamProvinceReportMapper.insert(teamProvinceReport);
    }

    @Override
    public void provinceActive() {
        List<Area> areas = areaService.findAll(new AreaQueryModel());
        areas = areas.stream().filter(v -> v.getAreaType() == Area.AreaType.省).collect(Collectors.toList());
        Map<Long, Area> areaMap= areaService.findAll(new  AreaQueryModel()).stream().collect(Collectors.toMap(v -> v.getId(), v -> v));
        Map<Long,UserInfo>userInfoMap = userInfoService.findAll(new UserInfoQueryModel()).stream().collect(Collectors.toMap(v -> v.getUserId(), v -> v));
        UserQueryModel userQueryModel = new UserQueryModel();
        OrderQueryModel orderQueryModel = new OrderQueryModel();
        List<User> users = userService.findAll(userQueryModel);
        List<Order> orders = orderService.findAll(orderQueryModel);
        users = users.stream().filter(v -> v.getUserRank() == User.UserRank.V4 || v.getUserRank() == User.UserRank.V3).collect(Collectors.toList());
        userInfoMap.putAll(userInfoService.findAll(UserInfoQueryModel.builder().build()).stream().collect(Collectors.toMap(v -> v.getUserId(), v -> v)));
        //循环处理用户地区
        for (User user : users) {
            user.setBossId(null);
            Long areaId =  userInfoMap.get(user.getId())==null?null:userInfoMap.get(user.getId()).getAreaId();
            Area area = null;
            if (areaId != null) {
                do {
                    area =  areaMap.get(areaId);
                    areaId=area!=null?area.getParentId():null;
                }while (areaId!=null);
                if (area!=null) {
                    user.setBossId(area.getId()); //暂时借用这个字段 修改时 注意
                }
            }
        }
        LocalDate now = LocalDate.now();
        LocalDate lastDate = now.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime localDateTime = LocalDateTime.of(lastDate, LocalTime.parse("23:59:59"));
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date end = Date.from(instant);

        lastDate = now.minusMonths(3).with(TemporalAdjusters.firstDayOfMonth());
        localDateTime = LocalDateTime.of(lastDate, LocalTime.parse("00:00:00"));
        instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date begin = Date.from(instant);

        List<Order> filterOrders = orders.stream()
                .filter(order -> order.getOrderStatus() == Order.OrderStatus.已完成
                        || order.getOrderStatus() == Order.OrderStatus.已支付 || order.getOrderStatus() == Order.OrderStatus.已发货)
                .filter(order -> order.getBuyerUserRank() == User.UserRank.V4)
                .filter(order -> order.getPaidTime().after(begin) && order.getPaidTime().before(end))
                .collect(Collectors.toList());
        Map<Long, List<Order>> orderMap = filterOrders.stream().collect(Collectors.groupingBy(Order::getUserId));
        Map<Long, TeamProvinceReport> map = areas.stream().collect(Collectors.toMap(v -> v.getId(), v -> {
            TeamProvinceReport teamProvinceReport = new TeamProvinceReport();
            teamProvinceReport.setCreateTime(new Date());
            teamProvinceReport.setMonth(DateUtil.getMothNum(DateUtil.getBeforeMonthBegin(new Date(),-1,0)));
            teamProvinceReport.setYear(DateUtil.getYear(DateUtil.getBeforeMonthBegin(new Date(),-1,0)));
            teamProvinceReport.setV3Number(0);
            teamProvinceReport.setV4Number(0);
            teamProvinceReport.setV4ActiveNumber(0);
            teamProvinceReport.setProvince(v.getName());
            teamProvinceReport.setV4ActiveRank(0);
            teamProvinceReport.setNewv3(0);
            teamProvinceReport.setNewv4(0);
            teamProvinceReport.setV4ActiveRate(0.00);
            return teamProvinceReport;
        }));
        for (User user : users) {
            TeamProvinceReport teamProvinceReport = map.get(user.getBossId());
            if(teamProvinceReport != null){
                if (user.getUserRank() == User.UserRank.V4) {
                    teamProvinceReport.setV4Number(teamProvinceReport.getV4Number() + 1);
                } else if (user.getUserRank() == User.UserRank.V3) {
                    teamProvinceReport.setV3Number(teamProvinceReport.getV3Number() + 1);
                }
                if (orderMap.get(user.getId()) != null) {
                    teamProvinceReport.setV4ActiveNumber(teamProvinceReport.getV4ActiveNumber() + 1);
                }
                map.put(user.getBossId(), teamProvinceReport);
            }
        }
        List<TeamProvinceReport> teamProvinceReports = new ArrayList<>(map.values());

        List<TeamProvinceReport> reports = teamProvinceReportMapper.findAll(TeamProvinceReportQueryModel.builder().yearEQ(DateUtil.getYear(DateUtil.getBeforeMonthBegin(new Date(), -2, 0))).monthEQ(DateUtil.getMothNum(DateUtil.getBeforeMonthBegin(new Date(), -1, 0))).build());
        Map<String, TeamProvinceReport> oldreportsmap= reports.stream().collect(Collectors.toMap(v -> v.getProvince(), v -> v));
        for (TeamProvinceReport t:teamProvinceReports) {
            if(t.getV4Number() != null && t.getV4Number() != 0){
                t.setV4ActiveRate(DateUtil.formatDouble( Double.valueOf(t.getV4ActiveNumber()) / Double.valueOf(t.getV4Number()) * 100));
            }else {
                t.setV4ActiveRate(0.00);
            }
            TeamProvinceReport pr = oldreportsmap.get(t.getProvince());
            if(pr != null){
                t.setNewv3(t.getV3Number() - pr.getV3Number());
                t.setNewv4(t.getV4Number() - pr.getV4Number());
            }
        }
        //对list 进行排序
        teamProvinceReports.sort((TeamProvinceReport h1, TeamProvinceReport h2) -> h2.getV4ActiveRate().compareTo(h1.getV4ActiveRate()));
        int i =1;
        Double rate = 0.00;
        for(TeamProvinceReport tp :teamProvinceReports) {
            if (tp.getV4ActiveRate()<rate){
                rate = tp.getV4ActiveRate();
                i++;
            }
            tp.setV4ActiveRank(i);
            if (rate == 0.00){
                rate = tp.getV4ActiveRate();
            }
            teamProvinceReportMapper.insert(tp);
        }
    }
}
