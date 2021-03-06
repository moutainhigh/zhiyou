package com.zy.admin.controller.rpt;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zy.common.util.ExcelUtils;
import com.zy.common.util.WebUtils;
import com.zy.component.LocalCacheComponent;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserUpgrade;
import com.zy.model.UserUpgradeReportVo;
import com.zy.model.query.ReportQueryModel;
import com.zy.vo.UserReportVo;

@Controller
@RequestMapping("/report/userUpgrade")
public class UserUpgradeReportController {

	@Autowired
	private LocalCacheComponent localCacheComponent;

	@RequiresPermissions("userUpgradeReport:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model, UserUpgradeReportVo.UserUpgradeReportQueryModel userUpgradeReportQueryModel) {

		model.addAttribute("rootNames", localCacheComponent.getRootNames());
		setModel(userUpgradeReportQueryModel, model);
		return "rpt/userUpgradReport";
	}

	@RequiresPermissions("userUpgradeReport:view")
	@RequestMapping(method = RequestMethod.POST)
	public String postList(Model model, UserUpgradeReportVo.UserUpgradeReportQueryModel userUpgradeReportQueryModel) {
		model.addAttribute("rootNames", localCacheComponent.getRootNames());
		//model.addAttribute("queryModel", userUpgradeReportQueryModel);
		Long provinceIdEQ = userUpgradeReportQueryModel.getProvinceIdEQ();
		Long cityIdEQ = userUpgradeReportQueryModel.getCityIdEQ();
		Long districtIdEQ = userUpgradeReportQueryModel.getDistrictIdEQ();
		Long areaId = null;
		if (districtIdEQ != null) {
			areaId = districtIdEQ;
		} else if (cityIdEQ != null) {
			areaId = cityIdEQ;
		} else if (provinceIdEQ != null) {
			areaId = provinceIdEQ;
		}
		model.addAttribute("areaId", areaId);
		model.addAttribute("rootRootName", userUpgradeReportQueryModel.getRootRootNameLK());
		setModel(userUpgradeReportQueryModel, model);
		return "rpt/userUpgradReport";
	}

	public void setModel(UserUpgradeReportVo.UserUpgradeReportQueryModel userUpgradeReportQueryModel, Model model) {
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
					if (!StringUtils.isBlank(rootRootNameLK)) {
						result = result && StringUtils.contains(userReportVo.getRootRootName(), rootRootNameLK);
					}
					return result;
				}).collect(Collectors.toList());

		LocalDate begin = LocalDate.of(2016, 2, 1);
		LocalDate today = LocalDate.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yy/M");
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
				//long lastMonthV0Count = lastMonthUserUpgradeReportVo.getV0Count();
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

		model.addAttribute("data", userUpgradeReportVos);
		model.addAttribute("timeLabels", timeLabels);
	}
	
	@RequiresPermissions("userUpgradeReport:export")
	@RequestMapping("/export")
	public String export(ReportQueryModel reportQueryModel, UserUpgradeReportVo.UserUpgradeReportQueryModel userUpgradeReportQueryModel, 
			HttpServletResponse response) throws IOException, ParseException{

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
					if (!StringUtils.isBlank(rootRootNameLK)) {
						result = result && StringUtils.contains(userReportVo.getRootRootName(), rootRootNameLK);
					}
					return result;
				}).collect(Collectors.toList());

		LocalDate begin = LocalDate.of(2016, 2, 1);
		LocalDate today = LocalDate.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yy/M");
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
				//long lastMonthV0Count = lastMonthUserUpgradeReportVo.getV0Count();
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
		
		String fileName = "服务商数量按月统计报表.xlsx";
		WebUtils.setFileDownloadHeader(response, fileName);
		OutputStream os = response.getOutputStream();
		
		List<String> rowLabels = initRowLabel();
		List<Map<String, Object>> dataList = new ArrayList<>();
		for(int i = 0; i < 19; i++) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("#", rowLabels.get(i));
			for(int j = 0; j < timeLabels.size(); j++) {
				switch (i) {
				case 0:
					map.put(timeLabels.get(j), userUpgradeReportVos.get(j).getCount());
					break;
				case 1:
					map.put(timeLabels.get(j), userUpgradeReportVos.get(j).getGrowthCount());
					break;
				case 2:
					map.put(timeLabels.get(j), userUpgradeReportVos.get(j).getGrowthRateLabel());
					break;
				case 3:
					map.put(timeLabels.get(j), userUpgradeReportVos.get(j).getV4Count());
					break;
				case 4:
					map.put(timeLabels.get(j), userUpgradeReportVos.get(j).getV4GrowthCount());
					break;
				case 5:
					map.put(timeLabels.get(j), userUpgradeReportVos.get(j).getV4GrowthRateLabel());
					break;
				case 6:
					map.put(timeLabels.get(j), userUpgradeReportVos.get(j).getV4ConversionRateLabel());
					break;
				case 7:
					map.put(timeLabels.get(j), userUpgradeReportVos.get(j).getV3Count());
					break;
				case 8:
					map.put(timeLabels.get(j), userUpgradeReportVos.get(j).getV3GrowthCount());
					break;
				case 9:
					map.put(timeLabels.get(j), userUpgradeReportVos.get(j).getV3GrowthRateLabel());
					break;
				case 10:
					map.put(timeLabels.get(j), userUpgradeReportVos.get(j).getV2Count());
					break;
				case 11:
					map.put(timeLabels.get(j), userUpgradeReportVos.get(j).getV2GrowthCount());
					break;
				case 12:
					map.put(timeLabels.get(j), userUpgradeReportVos.get(j).getV2GrowthRateLabel());
					break;
				case 13:
					map.put(timeLabels.get(j), userUpgradeReportVos.get(j).getV1Count());
					break;
				case 14:
					map.put(timeLabels.get(j), userUpgradeReportVos.get(j).getV1GrowthCount());
					break;
				case 15:
					map.put(timeLabels.get(j), userUpgradeReportVos.get(j).getV1GrowthRateLabel());
					break;
				case 16:
					map.put(timeLabels.get(j), userUpgradeReportVos.get(j).getV0Count());
					break;
				case 17:
					map.put(timeLabels.get(j), userUpgradeReportVos.get(j).getV0ConversionCount());
					break;
				case 18:
					map.put(timeLabels.get(j), userUpgradeReportVos.get(j).getV0ConversionRateLabel());
					break;
				default:
					break;
				}
				
			}
			dataList.add(map);
		}
		timeLabels.add(0, "#");
		ExcelUtils.exportExcel(dataList, timeLabels, os);

		return null;
	}
	
	private List<String> initRowLabel() {
		List<String> labels = new ArrayList<>();
		labels.add("服务商总数量");
		labels.add("当月新增服务商 ");
		labels.add("环比增长率（总）");
		labels.add("特级服务商总量） ");
		labels.add("当月新增特级 ");
		labels.add("环比增长率（特级） ");
		labels.add("特级转化率（本月新增特级/上月省级数量） ");
		labels.add("省级服务商总量 ");
		labels.add("当月新增省级");
		labels.add("环比增长率（省级） ");
		labels.add("市级服务商总量 ");
		labels.add("当月新增市级 ");
		labels.add("环比增长率（市级） ");
		labels.add("VIP服务商总量 ");
		labels.add("当月新增VIP ");
		labels.add("环比增长率（VIP） ");
		labels.add("意向服务商总量 ");
		labels.add("当月转化量 ");
		labels.add("转化率 ");
		return labels;
	}
}
