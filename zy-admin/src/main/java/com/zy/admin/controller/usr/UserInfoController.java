package com.zy.admin.controller.usr;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.component.UserInfoComponent;
import com.zy.entity.sys.Area;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.usr.Tag;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserInfo;
import com.zy.model.query.AreaQueryModel;
import com.zy.model.query.UserInfoQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.*;
import com.zy.vo.UserInfoAdminVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("/userInfo")
@Controller
public class UserInfoController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private TagService tagService;

	@Autowired
	private JobService jobService;

	@Autowired
	private AreaService areaService;

	@Autowired
	private UserInfoComponent userInfoComponent;
	
	@RequiresPermissions("userInfo:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("confirmStatuses", ConfirmStatus.values());
		return "usr/userInfoList";
	}
	
	@RequiresPermissions("userInfo:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<UserInfoAdminVo> list(UserInfoQueryModel userInfoQueryModel, String userPhoneEQ, String userNicknameLK
			, Long provinceIdEQ, Long cityIdEQ) {
		
		if (StringUtils.isNotBlank(userPhoneEQ) || StringUtils.isNotBlank(userNicknameLK)) {
        	UserQueryModel userQueryModel = new UserQueryModel();
        	userQueryModel.setPhoneEQ(userPhoneEQ);
        	userQueryModel.setNicknameLK(userNicknameLK);
        	
            List<User> users = userService.findAll(userQueryModel);
            if (users == null || users.size() == 0) {
                return new Grid<UserInfoAdminVo>(PageBuilder.empty(userInfoQueryModel.getPageSize(), userInfoQueryModel.getPageNumber()));
            }
            Long[] userIds = users.stream().map(v -> v.getId()).toArray(Long[]::new);
            userInfoQueryModel.setUserIdIN(userIds);
        }
		Long areaIdEQ = userInfoQueryModel.getAreaIdEQ();
		if(areaIdEQ == null) {
			List<Area> all = areaService.findAll(AreaQueryModel.builder().build());
			Map<Long, List<Area>> childrenMap = all.stream()
					.filter(area -> area.getParentId() != null)
					.collect(Collectors.groupingBy(Area::getParentId));
			if(cityIdEQ != null) {
				List<Area> areas = childrenMap.get(cityIdEQ);
				Long[] longs = areas.stream().map(v -> v.getId()).toArray(Long[]::new);
				userInfoQueryModel.setAreaIdIN(longs);
			} else {
				if(provinceIdEQ != null) {
					List<Area> cityAreas = childrenMap.get(provinceIdEQ);
					List<Area> districtAreas = new ArrayList<>();
					for(Area area : cityAreas) {
						districtAreas.addAll(childrenMap.get(area.getId()));
					}
					Long[] longs = districtAreas.stream().map(v -> v.getId()).toArray(Long[]::new);
					userInfoQueryModel.setAreaIdIN(longs);
				}
			}
        }
		
		Page<UserInfo> page = userInfoService.findPage(userInfoQueryModel);
		List<UserInfo> data = page.getData();
		long count = data.stream().filter(v -> StringUtils.isBlank(v.getIdCardNumber())).count();
		List<UserInfo> filter = data.stream().filter(v -> StringUtils.isNotBlank(v.getIdCardNumber())).collect(Collectors.toList());
		page.setData(filter);
		page.setTotal(page.getTotal() - count);
		Page<UserInfoAdminVo> voPage = PageBuilder.copyAndConvert(page, userInfoComponent::buildAdminVo);
		return new Grid<>(voPage);
	}
	
	@RequiresPermissions("userInfo:confirm")
	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> confirm(@RequestParam Long id, @RequestParam boolean isSuccess, String confirmRemark) {
		userInfoService.confirm(id, isSuccess, confirmRemark);
		return ResultBuilder.ok("操作成功");
	}

	@RequiresPermissions("userInfo:edit")
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public String update(@RequestParam Long userId, Model model) {
		UserInfo userInfo = userInfoService.findByUserId(userId);
		if(userInfo == null) {
			return "redirect:/userInfo";
		}
		model.addAttribute("jobs", jobService.findAll());
		//model.addAttribute("tags", getTags());
		model.addAttribute("userInfo", userInfoComponent.buildVo(userInfo));
		return "usr/userInfoUpdate";
	}

	@RequiresPermissions("userInfo:edit")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> update(UserInfo userInfo) {
		Long userId = userInfo.getUserId();
		UserInfo persistence = userInfoService.findByUserId(userId);
		if(userInfo == null) {
			return ResultBuilder.error("user id " + userId +  " not found");
		}
		userInfo.setId(persistence.getId());
		try {
			userInfoService.adminModify(userInfo);
			return ResultBuilder.ok("修改成功");
		} catch (Exception e) {
			return ResultBuilder.error(e.getMessage());
		}
	}

	private Map<String, List<Tag>> getTags() {
		Map<String, List<Tag>> tags = new HashMap<>();
		tagService.findAll().parallelStream().forEach(tag -> {
			if (!tags.containsKey(tag.getTagType())) {
				tags.put(tag.getTagType(), new ArrayList<>());
			}
			tags.get(tag.getTagType()).add(tag);
		});
		return tags;
	}
}
