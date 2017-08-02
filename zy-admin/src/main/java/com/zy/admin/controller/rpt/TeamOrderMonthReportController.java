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
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DecimalFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
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

	private static final String SPLIT_PATTERN = "-";

	@RequiresPermissions("teamOrderMonth:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model, String monthLabel) {
		model.addAttribute("queryDateLabels", getDateLabels());

		List<String> monthForHeaders = new ArrayList<>();
		if (StringUtils.isEmpty(monthLabel)) {
			LocalDate now = LocalDate.now();
			monthForHeaders.add(buildDateLabel(now.minusMonths(1)));
			monthForHeaders.add(buildDateLabel(now.minusMonths(2)));

			monthLabel = buildDateLabel(now.minusMonths(1));
		} else {
			String[] split = monthLabel.split(SPLIT_PATTERN);
			Integer year = Integer.valueOf(split[0]);
			Integer month = Integer.valueOf(split[1]);

			monthForHeaders.add(year + SPLIT_PATTERN + month);
			if (month == 1) {
				year = year - 1;
				month = 12;
			} else {
				month = month - 1;
			}
			monthForHeaders.add(year + SPLIT_PATTERN + month);
		}
		model.addAttribute("monthLabel", monthLabel);
		model.addAttribute("monthForHeaders", monthForHeaders);

		return "rpt/teamOrderMonthReport";
	}

	@RequiresPermissions("teamOrderMonth:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<TeamMonthReportVo> ajax(TeamMonthReportVo.TeamMonthReportVoQueryModel queryModel) {

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

		Date currentMonthBeginDateTime = null;
		Date currentMonthEndDateTime = null;
		Date lastMonthBeginDateTime = null;
		Date lastMonthEndDateTime = null;
		String currentMonthLabel = null;
		String lastMonthLabel = null;
		String monthLabel = queryModel.getMonthLabel();
		if (StringUtils.isEmpty(monthLabel)) {
			LocalDate now = LocalDate.now();
			LocalDate localDate = now.minusMonths(1);
			currentMonthBeginDateTime = getBeginDateTime(localDate.getYear(), localDate.getMonthValue());
			currentMonthEndDateTime = getEndDateTime(localDate.getYear(), localDate.getMonthValue());
			localDate = now.minusMonths(2);
			lastMonthBeginDateTime = getBeginDateTime(localDate.getYear(), localDate.getMonthValue());
			lastMonthEndDateTime = getEndDateTime(localDate.getYear(), localDate.getMonthValue());

			lastMonthLabel = buildDateLabel(now.minusMonths(-1));
			currentMonthLabel = buildDateLabel(now.minusMonths(-2));
		} else {
			String[] split = monthLabel.split(SPLIT_PATTERN);
			Integer year = Integer.valueOf(split[0]);
			Integer month = Integer.valueOf(split[1]);
			lastMonthBeginDateTime = getBeginDateTime(year, month);
			lastMonthEndDateTime = getEndDateTime(year, month);

			lastMonthLabel = year + SPLIT_PATTERN + month;
			if (month == 1) {
				year = year - 1;
				month = 12;
			} else {
				month = month - 1;
			}
			currentMonthBeginDateTime = getBeginDateTime(year, month);
			currentMonthEndDateTime = getEndDateTime(year, month);
			currentMonthLabel = year + SPLIT_PATTERN + month;
		}
		final Date currentMonthBeginDateTimeFinal = currentMonthBeginDateTime;
		final Date currentMonthEndDateTimeFinal = currentMonthEndDateTime;
		final Date lastMonthBeginDateTimeFinal = lastMonthBeginDateTime;
		final Date lastMonthEndDateTimeFinal = lastMonthEndDateTime;
		final String currentMonthLabelFinal = currentMonthLabel;
		final String lastMonthLabelFinal = lastMonthLabel;
		final Date now = new Date();

		List<Order> filterOrders = orders.stream()
				.filter(order -> order.getOrderStatus() == Order.OrderStatus.已支付
						|| order.getOrderStatus() == Order.OrderStatus.已发货
						|| order.getOrderStatus() == Order.OrderStatus.已完成)
				.filter(v -> v.getPaidTime().before(now) && v.getPaidTime().after(lastMonthBeginDateTimeFinal))
				.filter(v -> childrenIds.contains(v.getUserId()) || bossUserIds.contains(v.getUserId()))
				.collect(Collectors.toList());

		Map<Long, Long> currentMonthMap = filterOrders.stream()
				.filter(v -> v.getPaidTime().after(currentMonthBeginDateTimeFinal))
				.collect(Collectors.toMap(Order::getUserId, Order::getQuantity, (x , y) -> x + y));
		Map<Long, Long> lastMonthMap = filterOrders.stream()
				.filter(v -> v.getPaidTime().before(lastMonthEndDateTimeFinal))
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

	private List<String> getDateLabels() {
		LocalDate now = LocalDate.now().minusMonths(1);
		LocalDate begin = now.withMonth(1);
		LocalDate today = now;
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy" + SPLIT_PATTERN + "MM");
		List<String> dateLabels = new ArrayList<>();
		for (LocalDate itDate = begin; itDate.isEqual(today) || itDate.isBefore(today); itDate = itDate.plusMonths(1)) {
			dateLabels.add(dateTimeFormatter.format(itDate));
		}
		return dateLabels;
	}

	private String buildDateLabel(LocalDate localDate) {
		return localDate.getYear() + SPLIT_PATTERN + localDate.getMonthValue();
	}

}
