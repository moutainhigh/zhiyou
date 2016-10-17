package com.zy.admin.controller.rpt;

import com.zy.common.model.query.Page;
import com.zy.common.model.ui.Grid;
import com.zy.component.LocalCacheComponent;
import com.zy.entity.usr.User;
import com.zy.model.TeamReportVo;
import com.zy.util.GcUtils;
import com.zy.vo.UserReportVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/report/team")
public class TeamReportController {

	@Autowired
	private LocalCacheComponent localCacheComponent;

	@RequiresPermissions("teamReport:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("userRankMap", Arrays.asList(User.UserRank.values()).stream().collect(Collectors.toMap(v->v, v-> GcUtils.getUserRankLabel(v),(u, v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); }, LinkedHashMap::new)) );
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
				if (nicknameLK != null) {
					result = result && StringUtils.contains(userReportVo.getNickname(), nicknameLK);
				}
				if (phoneEQ != null) {
					result = result && phoneEQ.equals(userReportVo.getPhone());
				}
				if (v4UserNicknameLK != null) {
					result = result && StringUtils.contains(userReportVo.getV4UserNickname(), v4UserNicknameLK);
				}
				if (rootRootNameLK != null) {
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
			long v0Count = all.stream().filter(v -> v.getV4UserId() != null & v.getV4UserId().equals(userId)).filter(v -> v.getUserRank() == User.UserRank.V0).count();
			long v1Count = all.stream().filter(v -> v.getV4UserId() != null & v.getV4UserId().equals(userId)).filter(v -> v.getUserRank() == User.UserRank.V1).count();
			long v2Count = all.stream().filter(v -> v.getV4UserId() != null & v.getV4UserId().equals(userId)).filter(v -> v.getUserRank() == User.UserRank.V2).count();
			long v3Count = all.stream().filter(v -> v.getV4UserId() != null & v.getV4UserId().equals(userId)).filter(v -> v.getUserRank() == User.UserRank.V3).count();
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
		return new Grid<>(page);
	}
	
}
