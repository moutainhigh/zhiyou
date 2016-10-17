package com.zy.admin.controller.rpt;

import com.zy.component.LocalCacheComponent;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserUpgrade;
import com.zy.model.UserUpgradeReportVo;
import com.zy.vo.UserReportVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/report/userUpgrade")
public class UserUpgradeReportController {

	@Autowired
	private LocalCacheComponent localCacheComponent;

	@RequiresPermissions("userUpgradeport:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model, UserUpgradeReportVo.UserUpgradeReportQueryModel userUpgradeReportQueryModel) {
		model.addAttribute("data", getData(userUpgradeReportQueryModel));
		model.addAttribute("queryModel", userUpgradeReportQueryModel);
		return "rpt/userUpgradeport";
	}


	public List<UserUpgradeReportVo> getData(UserUpgradeReportVo.UserUpgradeReportQueryModel userUpgradeReportQueryModel) {
		List<UserReportVo> all = localCacheComponent.getuserReportVos();

		List<UserUpgrade> userUpgrades = localCacheComponent.getUserUpgrades();

		List<UserReportVo> filtered	= all.stream()
				.filter(userReportVo -> {
					boolean result = true;
					Long provinceIdEQ = userUpgradeReportQueryModel.getProvinceIdEQ();
					Long cityIdEQ = userUpgradeReportQueryModel.getCityIdEQ();
					Long districtIdEQ = userUpgradeReportQueryModel.getDistrictIdEQ();
					String rootRootNameLK = userUpgradeReportQueryModel.getRootRootNameLK();

					if (cityIdEQ != null) {
						result = result && cityIdEQ.equals(userReportVo.getCityId());
					}
					if (districtIdEQ != null) {
						result = result && districtIdEQ.equals(userReportVo.getDistrictId());
					}
					if (provinceIdEQ != null) {
						result = result && provinceIdEQ.equals(userReportVo.getProvinceId());
					}
					if (rootRootNameLK != null) {
						result = result && StringUtils.contains(userReportVo.getRootRootName(), rootRootNameLK);
					}
					return result;
				}).collect(Collectors.toList());

		LocalDate begin = LocalDate.of(2015, 9, 1);
		LocalDate today = LocalDate.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年MM月");
		List<String> timeLabels = new ArrayList<>();
		List<UserUpgradeReportVo> userUpgradeReportVos = new ArrayList<>();

		for (LocalDate itDate = begin; itDate.isEqual(today) || itDate.isBefore(today); itDate = itDate.plusMonths(1)) {
			timeLabels.add(dateTimeFormatter.format(itDate));
			UserUpgradeReportVo userUpgradeReportVo = new UserUpgradeReportVo();
			LocalDate lastDayOfMonth = itDate.with(TemporalAdjusters.lastDayOfMonth());
			LocalDate firstDayOfMonth = itDate.with(TemporalAdjusters.firstDayOfMonth());

			LocalDateTime lastMinuteOfMonth = LocalDateTime.of(lastDayOfMonth, LocalTime.of(23, 59, 59));
			LocalDateTime firstMinuteOfMonth = LocalDateTime.of(firstDayOfMonth, LocalTime.of(0, 0, 0));

			long count = 0;
			long growthCount = 0;

			long v0Count = 0;
			long v0ConversionCount = 0;

			long v1Count = 0;
			long v1GrowthCount = 0;

			long v2Count = 0;
			long v2GrowthCount = 0;

			long v3Count = 0;
			long v3GrowthCount = 0;

			long v4Count = 0;
			long v4GrowthCount = 0;

			for (UserReportVo userReportVo : filtered) {
				boolean exist = true;
				Date registerTime = userReportVo.getRegisterTime();
				if (registerTime != null && LocalDateTime.ofInstant(registerTime.toInstant(), ZoneId.systemDefault()).isAfter(lastMinuteOfMonth)) {
					exist = false;
				}
				if (exist) {

					User.UserRank lastMinuteOfMonthUserRank = userReportVo.getUserRank();
					User.UserRank firstMinuteOfMonthUserRank = userReportVo.getUserRank();

					List<UserUpgrade> filteredUserUpgrades = userUpgrades.stream()
							.filter(v -> v.getUserId().equals(userReportVo.getId()))
							.filter(v -> LocalDateTime.ofInstant(v.getUpgradedTime().toInstant(), ZoneId.systemDefault()).isAfter(firstMinuteOfMonth))
							.sorted((u, v) -> u.getUpgradedTime().compareTo(v.getUpgradedTime()) * -1)
							.collect(Collectors.toList());
					for (UserUpgrade userUpgrade : filteredUserUpgrades) {
						if (userUpgrade.getToUserRank() == firstMinuteOfMonthUserRank) {
							firstMinuteOfMonthUserRank = userUpgrade.getFromUserRank();
						}
					}

					for (UserUpgrade userUpgrade : filteredUserUpgrades) {
						if (LocalDateTime.ofInstant(userUpgrade.getUpgradedTime().toInstant(), ZoneId.systemDefault()).isBefore(lastMinuteOfMonth)) {
							break;
						}
						if (userUpgrade.getToUserRank() == lastMinuteOfMonthUserRank) {
							lastMinuteOfMonthUserRank = userUpgrade.getFromUserRank();
						}
					}

					if (lastMinuteOfMonthUserRank != User.UserRank.V0) {
						count = count + 1;
						if (firstMinuteOfMonthUserRank == User.UserRank.V0) {
							growthCount = growthCount + 1;
							v0ConversionCount = v0ConversionCount + 1;
						}

						if (lastMinuteOfMonthUserRank == User.UserRank.V1) {
							v1Count = v1Count + 1;
							if (firstMinuteOfMonthUserRank != User.UserRank.V1) {
								v1GrowthCount = v1GrowthCount + 1;
							}
						} else if (lastMinuteOfMonthUserRank == User.UserRank.V2) {
							v2Count = v2Count + 1;
							if (firstMinuteOfMonthUserRank != User.UserRank.V2) {
								v2GrowthCount = v2GrowthCount + 1;
							}
						} else if (lastMinuteOfMonthUserRank == User.UserRank.V3) {
							v3Count = v3Count + 1;
							if (firstMinuteOfMonthUserRank != User.UserRank.V3) {
								v3GrowthCount = v3GrowthCount + 1;
							}
						} else if (lastMinuteOfMonthUserRank == User.UserRank.V4) {
							v4Count = v4Count + 1;
							if (firstMinuteOfMonthUserRank != User.UserRank.V4) {
								v4GrowthCount = v4GrowthCount + 1;
							}
						}
					} else {
						v0Count = v0Count + 1;
					}

				}
			}
			userUpgradeReportVo.setCount(count);
			userUpgradeReportVo.setGrowthCount(growthCount);
			userUpgradeReportVo.setV0Count(v0Count);
			userUpgradeReportVo.setV0ConversionCount(v0ConversionCount);
			userUpgradeReportVo.setV1Count(v1Count);
			userUpgradeReportVo.setV1GrowthCount(v1GrowthCount);
			userUpgradeReportVo.setV2Count(v2Count);
			userUpgradeReportVo.setV2GrowthCount(v2GrowthCount);
			userUpgradeReportVo.setV3Count(v3Count);
			userUpgradeReportVo.setV3GrowthCount(v3GrowthCount);
			userUpgradeReportVo.setV4Count(v4Count);
			userUpgradeReportVo.setV4GrowthCount(v4GrowthCount);

			userUpgradeReportVos.add(userUpgradeReportVo);

		}

		boolean first = true;
		UserUpgradeReportVo lastMonthUserUpgradeReportVo = null;
		for (UserUpgradeReportVo userUpgradeReportVo : userUpgradeReportVos) {
			if (first) {
				userUpgradeReportVo.setGrowthRateLabel("-");
				userUpgradeReportVo.setV0ConversionRateLabel("-");
				userUpgradeReportVo.setV1GrowthRateLabel("-");
				userUpgradeReportVo.setV2GrowthRateLabel("-");
				userUpgradeReportVo.setV3GrowthRateLabel("-");
				userUpgradeReportVo.setV4GrowthRateLabel("-");
				userUpgradeReportVo.setV4ConversionRateLabel("-");

				first = false;
			} else {
				long lastMonthCount = lastMonthUserUpgradeReportVo.getCount();
				//noinspection unused
				long lastMonthV0Count = lastMonthUserUpgradeReportVo.getV0Count();
				long lastMonthV1Count = lastMonthUserUpgradeReportVo.getV1Count();
				long lastMonthV2Count = lastMonthUserUpgradeReportVo.getV2Count();
				long lastMonthV3Count = lastMonthUserUpgradeReportVo.getV3Count();
				long lastMonthV4Count = lastMonthUserUpgradeReportVo.getV4Count();


				DecimalFormat decimalFormat = new DecimalFormat("0.00%");
				if (lastMonthCount == 0) {
					userUpgradeReportVo.setGrowthRateLabel("-");
				} else {
					double rate = (userUpgradeReportVo.getCount() - lastMonthCount) / (double)lastMonthCount;
					userUpgradeReportVo.setGrowthRateLabel(decimalFormat.format(rate));
				}

				if (lastMonthV1Count == 0) {
					userUpgradeReportVo.setV1GrowthRateLabel("-");
				} else {
					double rate = (userUpgradeReportVo.getV1Count() - lastMonthV1Count) / (double)lastMonthV1Count;
					userUpgradeReportVo.setV1GrowthRateLabel(decimalFormat.format(rate));
				}

				if (lastMonthV2Count == 0) {
					userUpgradeReportVo.setV2GrowthRateLabel("-");
				} else {
					double rate = (userUpgradeReportVo.getV2Count() - lastMonthV2Count) / (double)lastMonthV2Count;
					userUpgradeReportVo.setV2GrowthRateLabel(decimalFormat.format(rate));
				}

				if (lastMonthV3Count == 0) {
					userUpgradeReportVo.setV3GrowthRateLabel("-");
					userUpgradeReportVo.setV4ConversionRateLabel("-");
				} else {
					double rate = (userUpgradeReportVo.getV3Count() - lastMonthV3Count) / (double)lastMonthV3Count;
					userUpgradeReportVo.setV3GrowthRateLabel(decimalFormat.format(rate));

					rate = userUpgradeReportVo.getV4GrowthCount() / (double)lastMonthV3Count;
					userUpgradeReportVo.setV4ConversionRateLabel(decimalFormat.format(rate));
				}

				if (lastMonthV4Count == 0) {
					userUpgradeReportVo.setV4GrowthRateLabel("-");
				} else {
					double rate = (userUpgradeReportVo.getV4Count() - lastMonthV4Count) / (double)lastMonthV4Count;
					userUpgradeReportVo.setV4GrowthRateLabel(decimalFormat.format(rate));
				}

				long v0Count = userUpgradeReportVo.getV0Count();
				if (v0Count == 0) {
					userUpgradeReportVo.setV0ConversionRateLabel("-");
				} else {
					double rate = userUpgradeReportVo.getV0ConversionCount() / (double)v0Count;
					userUpgradeReportVo.setV0ConversionRateLabel(decimalFormat.format(rate));
				}


			}
			lastMonthUserUpgradeReportVo = userUpgradeReportVo;
		}

		return userUpgradeReportVos;
	}
	
}
