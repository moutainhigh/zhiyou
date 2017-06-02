package com.zy.admin.controller.rpt;

import com.zy.common.model.query.Page;
import com.zy.common.model.tree.TreeHelper;
import com.zy.common.model.tree.TreeNode;
import com.zy.common.model.ui.Grid;
import com.zy.component.LocalCacheComponent;
import com.zy.entity.mal.Order;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserUpgrade;
import com.zy.model.TeamModel;
import com.zy.model.TeamMonthReportVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DecimalFormat;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/report/teamOrderMonth")
public class TeamOrderMonthReportController {

	@Autowired
	private LocalCacheComponent localCacheComponent;

	@RequiresPermissions("teamOrderMonth:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "rpt/teamOrderMonthReport";
	}

	@RequiresPermissions("teamOrderMonth:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<TeamMonthReportVo> ajax() {

		List<User> users = localCacheComponent.getUsers();
		List<Order> orders = localCacheComponent.getOrders();
		List<UserUpgrade> userUpgrades = localCacheComponent.getUserUpgrades();

		List<User> v4Children = users.stream().filter(v -> v.getBossId() != null).filter(v -> v.getUserRank() == User.UserRank.V4).collect(Collectors.toList());
		List<User> bossUsers = users.stream().filter(v -> v.getIsBoss() != null && v.getIsBoss()).collect(Collectors.toList());
		List<Long> childrenIds = v4Children.stream().map(v -> v.getId()).collect(Collectors.toList());
		List<Long> bossUserIds = bossUsers.stream().map(v -> v.getId()).collect(Collectors.toList());

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

		LocalDate localDate = LocalDate.now();
		Date now = new Date();
		LocalDate lastLocalDate = localDate.minusMonths(1);
		Date currentMonthBegin = getBeginDateTime(localDate.getYear(), localDate.getMonthValue());
		Date lastMonthBegin = getBeginDateTime(lastLocalDate.getYear(), lastLocalDate.getMonthValue());
		Date lastMonthEnd = getEndDateTime(lastLocalDate.getYear(), lastLocalDate.getMonthValue());

		List<Order> filterOrders = orders.stream()
				.filter(order -> order.getOrderStatus() == Order.OrderStatus.已支付
						|| order.getOrderStatus() == Order.OrderStatus.已发货
						|| order.getOrderStatus() == Order.OrderStatus.已完成)
				.filter(v -> v.getPaidTime().before(now) && v.getPaidTime().after(lastMonthBegin))
				.filter(v -> childrenIds.contains(v.getUserId()) || bossUserIds.contains(v.getUserId()))
				.collect(Collectors.toList());

		Map<Long, Long> currentMonthMap = filterOrders.stream()
				.filter(v -> v.getPaidTime().after(currentMonthBegin))
				.collect(Collectors.toMap(Order::getUserId, Order::getQuantity, (x , y) -> x + y));
		Map<Long, Long> lastMonthMap = filterOrders.stream()
				.filter(v -> v.getPaidTime().before(lastMonthEnd))
				.collect(Collectors.toMap(Order::getUserId, Order::getQuantity, (x , y) -> x + y));

		Map<Long, Long> currentMonthTeamMap = bossUsers.stream().collect(Collectors.toMap(v -> v.getId(), v -> {
			Long userId = v.getId();
			//Long myQuantity = currentMonthMap.get(userId) == null ? 0L : currentMonthMap.get(userId);
			Long teamQuantity = teamMap.get(userId).getV4Children().stream()
					.map(u -> currentMonthMap.get(u.getId()) == null ? 0L : currentMonthMap.get(u.getId()))
					.reduce(0L, (x, y) -> x + y);
			//return myQuantity + teamQuantity;
			return teamQuantity;
		}));
		Map<Long, Long> lastMonthTeamMap = bossUsers.stream().collect(Collectors.toMap(v -> v.getId(), v -> {
			Long userId = v.getId();
			//Long myQuantity = lastMonthMap.get(userId) == null ? 0L : lastMonthMap.get(userId);
			Long teamQuantity = teamMap.get(userId).getV4Children().stream()
					.map(u -> lastMonthMap.get(u.getId()) == null ? 0L : lastMonthMap.get(u.getId()))
					.reduce(0L, (x, y) -> x + y);
			//return myQuantity + teamQuantity;
			return teamQuantity;
		}));

		final DecimalFormat decimalFormat = new DecimalFormat("0.00%");
		List<TeamMonthReportVo> result = bossUsers.stream().map(v -> {
			Long inQuantitySum = currentMonthTeamMap.get(v.getId());
			Long lastMonthInQuantitySum = lastMonthTeamMap.get(v.getId());
			double rate = 0;
			if (lastMonthInQuantitySum != 0) {
				rate = (inQuantitySum - lastMonthInQuantitySum) / (double) lastMonthInQuantitySum;
			}
			String rateLabel = decimalFormat.format(rate);

			List<User> v4Children1 = teamMap.get(v.getId()).getV4Children();
			Integer count = v4Children1 != null ? v4Children1.size() : 0;
			Long avgRate = 0L;
			if (count != 0) {
				avgRate = Long.valueOf(inQuantitySum / count);
			}

			TeamMonthReportVo teamMonthReportVo = new TeamMonthReportVo();
			teamMonthReportVo.setBossId(v.getId());
			teamMonthReportVo.setBossName(v.getNickname());
			teamMonthReportVo.setInQuantitySum(inQuantitySum);
			teamMonthReportVo.setLastMonthInQuantitySum(lastMonthInQuantitySum);
			teamMonthReportVo.setGrowthRateLabel(rateLabel);
			teamMonthReportVo.setAvgQuantity(avgRate);
			return teamMonthReportVo;
		}).collect(Collectors.toList());

		/* 合计 */
		{
			long inQuantitySum = result.stream().map(v -> v.getInQuantitySum()).reduce((sum, inQuantity) -> sum + inQuantity).get();
			long lastMonthInQuantitySum = result.stream().map(v -> v.getLastMonthInQuantitySum()).reduce((sum, inQuantity) -> sum + inQuantity).get();
			long sum = teamMap.values().stream().map(v -> v.getV4Children().size()).reduce((total, childrenSize) -> total + childrenSize).get();
			long avgRate = 0L;
			double rate = 0;
			if (lastMonthInQuantitySum != 0) {
				rate = (inQuantitySum - lastMonthInQuantitySum) / (double) lastMonthInQuantitySum;
			}
			String rateLabel = decimalFormat.format(rate);

			if (sum != 0) {
				avgRate = Long.valueOf(inQuantitySum / sum);
			}
			TeamMonthReportVo teamMonthReportVo = new TeamMonthReportVo();
			teamMonthReportVo.setBossName("团队汇总");
			teamMonthReportVo.setInQuantitySum(inQuantitySum);
			teamMonthReportVo.setLastMonthInQuantitySum(lastMonthInQuantitySum);
			teamMonthReportVo.setGrowthRateLabel(rateLabel);
			teamMonthReportVo.setAvgQuantity(avgRate);

			result.add(teamMonthReportVo);
		}


		Page<TeamMonthReportVo> page = new Page<>();
		page.setPageNumber(0);
		page.setPageSize(100);
		page.setData(result);
		page.setTotal(Long.valueOf(result.size()));
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
