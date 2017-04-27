package com.zy.admin.controller.rpt;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.zy.common.model.query.Page;
import com.zy.common.model.ui.Grid;
import com.zy.common.util.ExcelUtils;
import com.zy.common.util.WebUtils;
import com.zy.component.LocalCacheComponent;
import com.zy.entity.usr.User;
import com.zy.model.TeamReportVo;
import com.zy.model.query.ReportQueryModel;
import com.zy.vo.UserReportVo;

@Controller
@RequestMapping("/report/team")
public class TeamReportController {

	@Autowired
	private LocalCacheComponent localCacheComponent;

	@RequiresPermissions("teamReport:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("rootNames", localCacheComponent.getRootNames());
		return "rpt/teamReport";
	}

	@RequiresPermissions("teamReport:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<TeamReportVo> list(TeamReportVo.TeamReportQueryModel teamReportQueryModel) {

		List<UserReportVo> data = new ArrayList<>();

		List<UserReportVo> all = localCacheComponent.getuserReportVos();

		List<UserReportVo> filtered	= all.stream()
			.filter(userReportVo -> userReportVo.getUserRank() == User.UserRank.V4)
			.filter(userReportVo -> {
				boolean result = true;
				Long provinceIdEQ = teamReportQueryModel.getProvinceIdEQ();
				Long cityIdEQ = teamReportQueryModel.getCityIdEQ();
				Long districtIdEQ = teamReportQueryModel.getDistrictIdEQ();
				String nicknameLK = teamReportQueryModel.getNickNameLK();
				String phoneEQ = teamReportQueryModel.getPhoneEQ();
				String v4UserNicknameLK = teamReportQueryModel.getV4UserNicknameLK();
				String rootRootNameLK = teamReportQueryModel.getRootRootNameLK();

				if (cityIdEQ != null) {
					result = result && cityIdEQ.equals(userReportVo.getCityId());
				}
				if (districtIdEQ != null) {
					result = result && districtIdEQ.equals(userReportVo.getDistrictId());
				}
				if (provinceIdEQ != null) {
					result = result && provinceIdEQ.equals(userReportVo.getProvinceId());
				}
				if (!StringUtils.isBlank(nicknameLK)) {
					result = result && StringUtils.contains(userReportVo.getNickname(), nicknameLK);
				}
				if (!StringUtils.isBlank(phoneEQ)) {
					result = result && phoneEQ.equals(userReportVo.getPhone());
				}
				if (!StringUtils.isBlank(v4UserNicknameLK)) {
					result = result && StringUtils.contains(userReportVo.getV4UserNickname(), v4UserNicknameLK);
				}
				if (!StringUtils.isBlank(rootRootNameLK)) {
					result = result && StringUtils.contains(userReportVo.getRootRootName(), rootRootNameLK);
				}
				return result;
		}).collect(Collectors.toList());

		int totalCount = filtered.size();
		Integer pageSize = teamReportQueryModel.getPageSize();
		Integer pageNumber = teamReportQueryModel.getPageNumber();
		if (pageSize == null) {
			pageSize = 20;
		}
		if (pageNumber == null) {
			pageNumber = 0;
		}
		int from = pageNumber * pageSize;
		if (from < totalCount) {
			int to = (from + pageSize) > totalCount ? totalCount : from + pageSize;
			for (int index = from; index < to; index++) {
				data.add(filtered.get(index));
			}
		}

		List<TeamReportVo> result = data.stream().map(userReportVo -> {
			TeamReportVo teamReportVo = new TeamReportVo();
			Long userId = userReportVo.getId();
			teamReportVo.setUserId(userId);
			teamReportVo.setNickname(userReportVo.getNickname());
			teamReportVo.setRootRootName(userReportVo.getRootRootName());
			teamReportVo.setV4UserNickname(userReportVo.getV4UserNickname());
			long v0Count = all.stream().filter(v -> v.getV4UserId() != null && v.getV4UserId().equals(userId)).filter(v -> v.getUserRank() == User.UserRank.V0).count();
			long v1Count = all.stream().filter(v -> v.getV4UserId() != null && v.getV4UserId().equals(userId)).filter(v -> v.getUserRank() == User.UserRank.V1).count();
			long v2Count = all.stream().filter(v -> v.getV4UserId() != null && v.getV4UserId().equals(userId)).filter(v -> v.getUserRank() == User.UserRank.V2).count();
			long v3Count = all.stream().filter(v -> v.getV4UserId() != null && v.getV4UserId().equals(userId)).filter(v -> v.getUserRank() == User.UserRank.V3).count();
			long v123Count = v1Count + v2Count + v3Count;
			teamReportVo.setV0Count(v0Count);
			teamReportVo.setV1Count(v1Count);
			teamReportVo.setV2Count(v2Count);
			teamReportVo.setV3Count(v3Count);
			teamReportVo.setV123Count(v123Count);

			return teamReportVo;
		}).collect(Collectors.toList());

		Page<TeamReportVo> page = new Page<>();
		page.setPageNumber(pageNumber);
		page.setPageSize(pageSize);
		page.setData(result);
		page.setTotal(Long.valueOf(filtered.size()));
		return new Grid<>(page);
	}
	
	@RequiresPermissions("teamReport:export")
	@RequestMapping("/export")
	public String export(ReportQueryModel reportQueryModel, TeamReportVo.TeamReportQueryModel teamReportQueryModel, 
			HttpServletResponse response) throws IOException {

		List<UserReportVo> all = localCacheComponent.getuserReportVos();

		List<UserReportVo> filtered	= all.stream()
			.filter(userReportVo -> userReportVo.getUserRank() == User.UserRank.V4)
			.filter(userReportVo -> {
				boolean result = true;
				Long provinceIdEQ = teamReportQueryModel.getProvinceIdEQ();
				Long cityIdEQ = teamReportQueryModel.getCityIdEQ();
				Long districtIdEQ = teamReportQueryModel.getDistrictIdEQ();
				String nicknameLK = teamReportQueryModel.getNickNameLK();
				String phoneEQ = teamReportQueryModel.getPhoneEQ();
				String v4UserNicknameLK = teamReportQueryModel.getV4UserNicknameLK();
				String rootRootNameLK = teamReportQueryModel.getRootRootNameLK();

				if (cityIdEQ != null) {
					result = result && cityIdEQ.equals(userReportVo.getCityId());
				}
				if (districtIdEQ != null) {
					result = result && districtIdEQ.equals(userReportVo.getDistrictId());
				}
				if (provinceIdEQ != null) {
					result = result && provinceIdEQ.equals(userReportVo.getProvinceId());
				}
				if (!StringUtils.isBlank(nicknameLK)) {
					result = result && StringUtils.contains(userReportVo.getNickname(), nicknameLK);
				}
				if (!StringUtils.isBlank(phoneEQ)) {
					result = result && phoneEQ.equals(userReportVo.getPhone());
				}
				if (!StringUtils.isBlank(v4UserNicknameLK)) {
					result = result && StringUtils.contains(userReportVo.getV4UserNickname(), v4UserNicknameLK);
				}
				if (!StringUtils.isBlank(rootRootNameLK)) {
					result = result && StringUtils.contains(userReportVo.getRootRootName(), rootRootNameLK);
				}
				return result;
		}).collect(Collectors.toList());


		List<TeamReportVo> result = filtered.stream().map(userReportVo -> {
			TeamReportVo teamReportVo = new TeamReportVo();
			Long userId = userReportVo.getId();
			teamReportVo.setUserId(userId);
			teamReportVo.setNickname(userReportVo.getNickname());
			teamReportVo.setRootRootName(userReportVo.getRootRootName());
			teamReportVo.setV4UserNickname(userReportVo.getV4UserNickname());
			long v0Count = all.stream().filter(v -> v.getV4UserId() != null && v.getV4UserId().equals(userId)).filter(v -> v.getUserRank() == User.UserRank.V0).count();
			long v1Count = all.stream().filter(v -> v.getV4UserId() != null && v.getV4UserId().equals(userId)).filter(v -> v.getUserRank() == User.UserRank.V1).count();
			long v2Count = all.stream().filter(v -> v.getV4UserId() != null && v.getV4UserId().equals(userId)).filter(v -> v.getUserRank() == User.UserRank.V2).count();
			long v3Count = all.stream().filter(v -> v.getV4UserId() != null && v.getV4UserId().equals(userId)).filter(v -> v.getUserRank() == User.UserRank.V3).count();
			long v123Count = v1Count + v2Count + v3Count;
			teamReportVo.setV0Count(v0Count);
			teamReportVo.setV1Count(v1Count);
			teamReportVo.setV2Count(v2Count);
			teamReportVo.setV3Count(v3Count);
			teamReportVo.setV123Count(v123Count);

			return teamReportVo;
		}).collect(Collectors.toList());
		
		String fileName = "特级服务商下线人数.xlsx";
		WebUtils.setFileDownloadHeader(response, fileName);

		OutputStream os = response.getOutputStream();
		List<Map<String, Object>> dataList = result.stream().map(v -> {
			Map<String, Object> columnMap = new LinkedHashMap<>();
			columnMap.put("服务商", v.getNickname());
			columnMap.put("系统", v.getRootRootName());
			columnMap.put("直属特级", v.getV4UserNickname());
			columnMap.put("省级", v.getV3Count());
			columnMap.put("市级", v.getV2Count());
			columnMap.put("VIP", v.getV1Count());
			columnMap.put("意向服务商", v.getV0Count());
			columnMap.put("服务商总数量", v.getV123Count());
			return columnMap;
		}).collect(Collectors.toList());
		
		Map<String, Object> map = dataList.get(0);
		List<String> headers = new ArrayList<String>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			headers.add(entry.getKey());
		}
		ExcelUtils.exportExcel(dataList, headers, os);

		return null;
	}
}
