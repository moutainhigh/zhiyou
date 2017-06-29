package com.zy.admin.controller.rpt;

import com.zy.common.model.query.Page;
import com.zy.common.model.tree.TreeHelper;
import com.zy.common.model.tree.TreeNode;
import com.zy.common.model.ui.Grid;
import com.zy.common.support.cache.CacheSupport;
import com.zy.component.LocalCacheComponent;
import com.zy.entity.mal.Order;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserUpgrade;
import com.zy.model.CityAgentReportVo;
import com.zy.model.Constants;
import com.zy.model.TeamModel;
import com.zy.model.V4ActivityReportVo;
import com.zy.util.GcUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/report/v4Activity")
public class V4ActivityReportController {

	@Autowired
	private LocalCacheComponent localCacheComponent;

	@RequiresPermissions("v4Activity:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "rpt/v4ActivityReport";
	}

	@RequiresPermissions("v4Activity:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<V4ActivityReportVo> listAjax() {

		List<User> users = localCacheComponent.getUsers();
		List<Order> orders = localCacheComponent.getOrders();
		List<UserUpgrade> userUpgrades = localCacheComponent.getUserUpgrades();

		LocalDate now = LocalDate.now();
		LocalDate localDate = now.minusMonths(1);
		LocalDate localDate1 = now.minusMonths(4);
		Date endDateTime = getEndDateTime(localDate.getYear(), localDate.getMonthValue());
		Date beginDateTime = getBeginDateTime(localDate1.getYear(), localDate1.getMonthValue());
		LocalDate lastLocalDate = now.minusMonths(2);
		LocalDate lastLocalDate1 = now.minusMonths(5);
		Date lastEndDateTime = getEndDateTime(lastLocalDate.getYear(), lastLocalDate.getMonthValue());
		Date lastBeginDateTime = getBeginDateTime(lastLocalDate1.getYear(), lastLocalDate1.getMonthValue());

		List<User> v4Children = users.stream()
				.filter(v -> v.getBossId() != null)
				.filter(v -> v.getUserRank() == User.UserRank.V4)
				.filter(v -> v.getRegisterTime().before(getBeginDateTime(now.getYear(), now.getMonthValue())))
				.collect(Collectors.toList());
		List<User> bossUsers = users.stream().filter(v -> v.getIsBoss() != null && v.getIsBoss()).collect(Collectors.toList());
		List<Long> v4Ids = Arrays.asList(v4Children.stream().map(v -> v.getId()).toArray(Long[]::new));
		Map<Long, Boolean> userUpgradeMap = userUpgrades.stream()
				.filter(v -> v.getUpgradedTime().after(getBeginDateTime(localDate.getYear(), localDate.getMonthValue()))
					&& v.getUpgradedTime().before(getEndDateTime(localDate.getYear(), localDate.getMonthValue())))
				.filter(v -> v.getToUserRank() == User.UserRank.V4)
				.filter(v -> v4Ids.contains(v.getUserId()))
				.collect(Collectors.toMap(v -> v.getUserId(), v -> true, (existingValue, newValue) -> existingValue));

		Map<Long, TeamModel> teamMap = bossUsers.stream().map(v -> {
			TeamModel teamModel = new TeamModel();
			teamModel.setUser(v);
			teamModel.setV4Children(TreeHelper.sortBreadth2(v4Children, String.valueOf(v.getId()), u -> {
				TreeNode treeNode = new TreeNode();
				treeNode.setId(String.valueOf(u.getId()));
				treeNode.setParentId(u.getBossId().toString());
				return treeNode;
			}));
			return teamModel;
		}).collect(Collectors.toMap(v -> v.getUser().getId(), Function.identity()));

		List<Order> filterOrder = orders.stream()
				.filter(v -> v.getOrderStatus() == Order.OrderStatus.已支付 || v.getOrderStatus() == Order.OrderStatus.已发货
						|| v.getOrderStatus() == Order.OrderStatus.已完成)
				.filter(v -> v4Ids.contains(v.getUserId()))
				.collect(Collectors.toList());
		Map<Long, Boolean> orderMap = filterOrder.stream()
				.filter(v -> v.getPaidTime().after(beginDateTime) && v.getPaidTime().before(endDateTime))
				.collect(Collectors.toMap(v -> v.getUserId(), v -> true, (existingValue, newValue) -> existingValue));
		Map<Long, Boolean> lastOrderMap = filterOrder.stream()
				.filter(v -> v.getPaidTime().after(lastBeginDateTime) && v.getPaidTime().before(lastEndDateTime))
				.collect(Collectors.toMap(v -> v.getUserId(), v -> true, (existingValue, newValue) -> existingValue));

		DecimalFormat decimalFormat = new DecimalFormat("0.00%");
		List<V4ActivityReportVo> v4ActivityReportVos = bossUsers.stream().map(v -> {

			Long id = v.getId();
			List<User> v4Children1 = teamMap.get(id).getV4Children();
			long v4Count = Long.valueOf(v4Children1.size());
			long recentCount = v4Children1.stream().filter(child -> userUpgradeMap.get(child.getId()) != null).count();
			long activityCount = v4Children1.stream().filter(child -> orderMap.get(child.getId()) != null).count();
			long inActivityCount = v4Count - activityCount;
			double rate = 0;
			if (v4Count != 0) {
				rate = activityCount / (double) v4Count;
			}
			String rateLabel = decimalFormat.format(rate);

			long lastActivityCount = v4Children1.stream().filter(child -> lastOrderMap.get(child.getId()) != null).count();
			double lastRate = 0;
			if (v4Count != 0) {
				lastRate = lastActivityCount / (double) v4Count;
			}
			String lastRateLabel = decimalFormat.format(lastRate);

			V4ActivityReportVo vo = new V4ActivityReportVo();
			vo.setBossId(id);
			vo.setBossName(v.getBossName());
			vo.setV4Count(v4Count);
			vo.setActivityCount(activityCount);
			vo.setInactiveCount(inActivityCount);
			vo.setRecentCount(recentCount);
			vo.setActivityRateLabel(rateLabel);
			vo.setActivityRate(rate);
			vo.setLastActivityRateLabel(lastRateLabel);
			vo.setLastActivityRate(lastRate);
			vo.setActivityRateOrderNumber(1);
			vo.setLastActivityRateOrderNumber(1);

			return vo;
		}).collect(Collectors.toList());

		v4ActivityReportVos.sort((V4ActivityReportVo h1, V4ActivityReportVo h2) -> String.valueOf(h2.getLastActivityRate()).compareTo(String.valueOf(h1.getLastActivityRate())));
		int orderNumber = 0;
		String activityRateLabel = null;
		for (V4ActivityReportVo vo : v4ActivityReportVos) {
			vo.setId(orderNumber);
			if (!vo.getLastActivityRateLabel().equals(activityRateLabel)) {
				orderNumber++;
				activityRateLabel = vo.getLastActivityRateLabel();
			}
			vo.setLastActivityRateOrderNumber(orderNumber);
		}

		v4ActivityReportVos.sort((V4ActivityReportVo h1, V4ActivityReportVo h2) -> String.valueOf(h2.getActivityRate()).compareTo(String.valueOf(h1.getActivityRate())));
		orderNumber = 0;
		activityRateLabel = null;
		for (V4ActivityReportVo vo : v4ActivityReportVos) {
			if (!vo.getActivityRateLabel().equals(activityRateLabel)) {
				orderNumber++;
				activityRateLabel = vo.getActivityRateLabel();
			}
			vo.setActivityRateOrderNumber(orderNumber);

			Integer lastActivityRateOrderNumber = vo.getLastActivityRateOrderNumber();
			if (lastActivityRateOrderNumber == orderNumber) {
				vo.setGrowth("持平");
			} else if (lastActivityRateOrderNumber < orderNumber) {
				vo.setGrowth("下降");
			} else {
				vo.setGrowth("上升");
			}
		}

		Page<V4ActivityReportVo> page = new Page<>();
		page.setPageNumber(0);
		page.setPageSize(100);
		page.setData(v4ActivityReportVos);
		page.setTotal(Long.valueOf(v4ActivityReportVos.size()));
		return new Grid<>(page);
	}

	private Date getBeginDateTime(Integer year, Integer month) {
		LocalDate now = LocalDate.of(year, month, 1);
		LocalDate localDate = now.with(TemporalAdjusters.firstDayOfMonth());
		LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.parse("00:00:00"));
		Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
		return Date.from(instant);
	}

	private Date getEndDateTime(Integer year, Integer month) {
		LocalDate now = LocalDate.of(year, month, 1);
		LocalDate localDate = now.with(TemporalAdjusters.lastDayOfMonth());
		LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.parse("23:59:59"));
		Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
		return Date.from(instant);
	}

}
