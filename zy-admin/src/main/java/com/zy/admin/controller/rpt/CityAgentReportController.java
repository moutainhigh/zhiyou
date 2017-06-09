package com.zy.admin.controller.rpt;

import com.zy.common.model.query.Page;
import com.zy.common.model.ui.Grid;
import com.zy.component.LocalCacheComponent;
import com.zy.entity.mal.Order;
import com.zy.entity.sys.Area;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserUpgrade;
import com.zy.model.CityAgentReportVo;
import com.zy.model.FinanceReportVo;
import com.zy.vo.UserReportVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/report/cityAgent")
public class CityAgentReportController {

	@Autowired
	private LocalCacheComponent localCacheComponent;

	@RequiresPermissions("cityAgent:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "rpt/cityAgentReport";
	}

	@RequiresPermissions("cityAgent:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<CityAgentReportVo> list(CityAgentReportVo.CityAgentReportVoQueryModel cityAgentReportVoQueryModel) {

		List<Area> areas = localCacheComponent.getAreas();
		List<UserReportVo> users = localCacheComponent.getuserReportVos();
		List<Order> orders = localCacheComponent.getOrders();
		List<UserUpgrade> userUpgrades = localCacheComponent.getUserUpgrades();

		LocalDate now = LocalDate.now();
		Date beginDateTime = getBeginDateTime(now.getYear(), now.getMonthValue());
		users = users.stream().filter(v -> v.getRegisterTime().before(beginDateTime)).collect(Collectors.toList());
		List<Long> userIds = Arrays.asList(users.stream().map(v -> v.getId()).toArray(Long[]::new));
		userUpgrades = userUpgrades.stream().filter(v -> v.getUpgradedTime().before(beginDateTime)).collect(Collectors.toList());

		Map<Long, Boolean> userUpgradeMap = userUpgrades.stream()
				.filter(v -> v.getToUserRank() == User.UserRank.V4 || v.getToUserRank() == User.UserRank.V3)
				.filter(v -> userIds.contains(v.getUserId()))
				.collect(Collectors.toMap(v -> v.getUserId(), v -> true, (existingValue, newValue) -> existingValue));

		List<Area> filterAreas = areas.stream()
				.filter(v -> v.getAreaType() == Area.AreaType.省)
				.collect(Collectors.toList());
		List<UserReportVo> filterUsers = users.stream()
				.filter(v -> userUpgradeMap.get(v.getV4UserId()) != null)
				.filter(v -> v.getProvinceId() != null)
				.collect(Collectors.toList());

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

		Map<Long, CityAgentReportVo> map = filterAreas.stream().collect(Collectors.toMap(v -> v.getId(), v -> {
			CityAgentReportVo cityAgentReportVo = new CityAgentReportVo();
			cityAgentReportVo.setActiveV4Count(0);
			cityAgentReportVo.setActiveCityOrderNumber(0);
			cityAgentReportVo.setCity(v.getName());
			cityAgentReportVo.setV3Count(0);
			cityAgentReportVo.setV4Count(0);
			return cityAgentReportVo;
		}));
		for (UserReportVo userReportVo : filterUsers) {
			CityAgentReportVo cityAgentReportVo = map.get(userReportVo.getProvinceId());
			if (userReportVo.getUserRank() == User.UserRank.V4) {
				cityAgentReportVo.setV4Count(cityAgentReportVo.getV4Count()+1);
			} else if (userReportVo.getUserRank() == User.UserRank.V3) {
				cityAgentReportVo.setV3Count(cityAgentReportVo.getV3Count()+1);
			}
			if (orderMap.get(userReportVo.getId()) != null) {
				cityAgentReportVo.setActiveV4Count(cityAgentReportVo.getActiveV4Count()+1);
			}

			map.put(userReportVo.getProvinceId(), cityAgentReportVo);
		}
		List<CityAgentReportVo> cityAgentReportVos = new ArrayList<>(map.values());
		cityAgentReportVos.sort((CityAgentReportVo h1, CityAgentReportVo h2) -> h2.getActiveV4Count().compareTo(h1.getActiveV4Count()));
		int i = 1;
		for (CityAgentReportVo cityAgentReportVo : cityAgentReportVos) {
			cityAgentReportVo.setId(i);
			cityAgentReportVo.setActiveCityOrderNumber(i);
			i++;
		}
		Page<CityAgentReportVo> page = new Page<>();
		page.setPageNumber(0);
		page.setPageSize(100);
		page.setData(cityAgentReportVos);
		page.setTotal(Long.valueOf(map.size()));
		return new Grid<>(page);
	}

	public static void main(String[] args) {
		LocalDate now = LocalDate.now();
		LocalDate lastDate = now.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
		LocalDateTime localDateTime = LocalDateTime.of(lastDate, LocalTime.parse("23:59:59"));
		Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
		Date end = Date.from(instant);
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(end));

		lastDate = now.minusMonths(3).with(TemporalAdjusters.firstDayOfMonth());
		localDateTime = LocalDateTime.of(lastDate, LocalTime.parse("00:00:00"));
		instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
		Date begin = Date.from(instant);
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(begin));
	}

	private Date getBeginDateTime(Integer year, Integer month) {
		LocalDate now = LocalDate.of(year, month, 1);
		LocalDate localDate = now.with(TemporalAdjusters.firstDayOfMonth());
		LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.parse("00:00:00"));
		Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
		return Date.from(instant);
	}
}
