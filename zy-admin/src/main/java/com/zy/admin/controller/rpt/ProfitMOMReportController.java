package com.zy.admin.controller.rpt;

import com.zy.common.model.query.Page;
import com.zy.common.model.tree.TreeHelper;
import com.zy.common.model.tree.TreeNode;
import com.zy.common.model.ui.Grid;
import com.zy.component.LocalCacheComponent;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.fnc.Profit;
import com.zy.entity.usr.User;
import com.zy.model.ProfitMOMReportVo;
import com.zy.model.TeamModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
@RequestMapping("/report/profitMOM")
public class ProfitMOMReportController {

	@Autowired
	private LocalCacheComponent localCacheComponent;

	private static final String SPLIT_PATTERN = "-";

	@RequiresPermissions("profitMOM:view")
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
		return "rpt/profitMOMReport";
	}

	@RequiresPermissions("profitMOM:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<ProfitMOMReportVo> listMonth(ProfitMOMReportVo.ProfitMOMReportVoQueryModel queryModel) {

		List<User> users = localCacheComponent.getUsers();
		List<Profit> profits = localCacheComponent.getProfits();

		Map<Long, User> userMap = users.stream().collect(Collectors.toMap(v -> v.getId(), v -> v));
		List<User> v4Children = users.stream().filter(v -> v.getBossId() != null).filter(v -> v.getUserRank() == User.UserRank.V4).collect(Collectors.toList());
		List<User> bossUsers = users.stream().filter(v -> v.getIsBoss() != null && v.getIsBoss()).collect(Collectors.toList());

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

		List<Long> childrenIds = v4Children.stream().map(v -> v.getId()).collect(Collectors.toList());
		List<Long> bossUserIds = bossUsers.stream().map(v -> v.getId()).collect(Collectors.toList());

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

		List<Profit> filterProfits = profits.stream()
				.filter(v -> v.getProfitStatus() == Profit.ProfitStatus.已发放)
				.filter(v -> v.getProfitType() != Profit.ProfitType.订单收款)
				.filter(v -> v.getCurrencyType() == CurrencyType.现金)
				.filter(v -> childrenIds.contains(v.getUserId()) || bossUserIds.contains(v.getUserId()))
				.filter(v -> {
					boolean result = true;
					result = result && (v.getGrantedTime().after(currentMonthBeginDateTimeFinal) && v.getGrantedTime().before(lastMonthEndDateTimeFinal));
					return result;
				})
				.collect(Collectors.toList());
		Map<Long, BigDecimal> currentMonthProfitMap = filterProfits.stream()
				.filter(v -> v.getGrantedTime().before(currentMonthEndDateTimeFinal))
				.collect(Collectors.toMap(Profit::getUserId, Profit::getAmount, (x , y) -> x.add(y)));
		Map<Long, BigDecimal> lastMonthProfitMap = filterProfits.stream()
				.filter(v -> v.getGrantedTime().after(lastMonthBeginDateTimeFinal))
				.collect(Collectors.toMap(Profit::getUserId, Profit::getAmount, (x , y) -> x.add(y)));

		BigDecimal zero = BigDecimal.ZERO;
		Map<Long, BigDecimal> currentMonthTeamProfitMap = bossUsers.stream().collect(Collectors.toMap(v -> v.getId(), v -> {
			Long userId = v.getId();
			BigDecimal myProfit = currentMonthProfitMap.get(userId) == null ? zero : currentMonthProfitMap.get(userId);
			BigDecimal teamProfit = teamMap.get(userId).getV4Children().stream()
					.map(u -> currentMonthProfitMap.get(u.getId()) == null ? zero : currentMonthProfitMap.get(u.getId()))
					.reduce(zero, (x, y) -> x.add(y));
			return myProfit.add(teamProfit);
		}));
		Map<Long, BigDecimal> lastMonthTeamProfitMap = bossUsers.stream().collect(Collectors.toMap(v -> v.getId(), v -> {
			Long userId = v.getId();
			BigDecimal myProfit = lastMonthProfitMap.get(userId) == null ? zero : lastMonthProfitMap.get(userId);
			BigDecimal teamProfit = teamMap.get(userId).getV4Children().stream()
					.map(u -> lastMonthProfitMap.get(u.getId()) == null ? zero : lastMonthProfitMap.get(u.getId()))
					.reduce(zero, (x, y) -> x.add(y));
			return myProfit.add(teamProfit);
		}));

		List<ProfitMOMReportVo> result = bossUsers.stream().map(v -> {
			Long id = v.getId();

			ProfitMOMReportVo profitMOMReportVo = new ProfitMOMReportVo();
			profitMOMReportVo.setBossId(id);
			profitMOMReportVo.setBossNickname(userMap.get(id).getNickname());
			profitMOMReportVo.setBossName(v.getBossName());

			int childrenNumber = teamMap.get(id).getV4Children().size();
			BigDecimal currentMonthProfit = currentMonthProfitMap.get(id) == null ? zero : currentMonthProfitMap.get(id);
			BigDecimal lastMonthProfit = lastMonthProfitMap.get(id) == null ? zero : lastMonthProfitMap.get(id);
			BigDecimal currentMonthTeamProfit = currentMonthTeamProfitMap.get(id);
			BigDecimal lastMonthTeamProfit = lastMonthTeamProfitMap.get(id);

			List<ProfitMOMReportVo.ProfitMOMReportVoItem> profitMOMReportVoItems = new ArrayList<>();
			profitMOMReportVoItems.add(buildProfitMOMReportVoItem(currentMonthLabelFinal, currentMonthProfit, currentMonthTeamProfit, childrenNumber));
			profitMOMReportVoItems.add(buildProfitMOMReportVoItem(lastMonthLabelFinal, lastMonthProfit, lastMonthTeamProfit, childrenNumber));
			profitMOMReportVo.setProfitMOMReportVoItems(profitMOMReportVoItems);

			BigDecimal divide = null;
			if (currentMonthTeamProfit.compareTo(zero) > 0) {
				divide = (lastMonthTeamProfit.subtract(currentMonthTeamProfit)).divide(currentMonthTeamProfit, 4, RoundingMode.HALF_UP);
			} else {
				divide = lastMonthTeamProfit;
			}
			DecimalFormat decimalFormat = new DecimalFormat("0.00%");
			profitMOMReportVo.setProfitMOMRateLabel(decimalFormat.format(divide));
			return profitMOMReportVo;
		}).collect(Collectors.toList());

		Page<ProfitMOMReportVo> page = new Page<>();
		page.setData(result);
		page.setPageNumber(0);
		page.setPageSize(100);
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

	private String buildDateLabel(LocalDate localDate) {
		return localDate.getYear() + SPLIT_PATTERN + localDate.getMonthValue();
	}

	private ProfitMOMReportVo.ProfitMOMReportVoItem buildProfitMOMReportVoItem(String dateLabel, BigDecimal profit, BigDecimal teamProfit, int childrenNumber) {
		BigDecimal zero = new BigDecimal("0.00");
		BigDecimal avgProfit = zero;
		if (teamProfit.compareTo(zero) > 0) {
			avgProfit = teamProfit.divide(new BigDecimal(childrenNumber), 2, RoundingMode.HALF_UP);  //四舍五入
		}

		double inTeamProfitRate = 0.00;
		if (teamProfit.compareTo(zero) > 0) {
			inTeamProfitRate = profit.divide(teamProfit, 2, RoundingMode.HALF_UP).doubleValue();
		}

		ProfitMOMReportVo.ProfitMOMReportVoItem profitMOMReportVoItem = new ProfitMOMReportVo.ProfitMOMReportVoItem();
		profitMOMReportVoItem.setDateLabel(dateLabel);
		profitMOMReportVoItem.setProfit(profit);
		profitMOMReportVoItem.setTeamProfit(teamProfit);
		profitMOMReportVoItem.setAvgProfit(avgProfit);
		profitMOMReportVoItem.setInTeamProfitRate(inTeamProfitRate);
		return profitMOMReportVoItem;
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

	public static void main(String[] args) {
		BigDecimal zero = BigDecimal.ZERO;
		BigDecimal number = new BigDecimal("15");
		BigDecimal profit = new BigDecimal("12032.21");
		zero = profit.divide(number, 2, RoundingMode.HALF_UP);
		System.out.println(zero);

	}
}
