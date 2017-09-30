package com.zy.admin.controller.usr;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import com.zy.admin.model.AdminPrincipal;
import com.zy.common.exception.BizException;
import com.zy.common.exception.UnauthorizedException;
import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.tree.TreeHelper;
import com.zy.common.model.tree.TreeNode;
import com.zy.common.model.ui.Grid;
import com.zy.common.util.Identities;
import com.zy.common.util.JsonUtils;
import com.zy.component.CacheComponent;
import com.zy.component.LocalCacheComponent;
import com.zy.component.UserComponent;
import com.zy.entity.fnc.Profit;
import com.zy.entity.sys.SystemCode;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserRank;
import com.zy.entity.usr.User.UserType;
import com.zy.model.BizCode;
import com.zy.model.Constants;
import com.zy.model.query.UserQueryModel;
import com.zy.service.SystemCodeService;
import com.zy.service.UserService;
import com.zy.util.GcUtils;
import com.zy.vo.UserAdminVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;
import static com.zy.model.Constants.MODEL_ATTRIBUTE_RESULT;

@RequestMapping("/user")
@Controller
public class UserController {

	@Autowired
	@Lazy
	private UserService userService;

	@Autowired
	private CacheComponent cahceComponent;

	@Autowired
	private UserComponent userComponent;

	@Autowired
	private LocalCacheComponent localCacheComponent;

	@Autowired
	private SystemCodeService systemCodeService;

	@RequiresPermissions("user:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("userRankMap", Arrays.asList(UserRank.values()).stream().collect(Collectors.toMap(v->v, v->GcUtils.getUserRankLabel(v),(u,v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); }, LinkedHashMap::new)) );
		List<SystemCode> largeAreaTypes = systemCodeService.findByType("LargeAreaType");
		model.addAttribute("largeAreas",largeAreaTypes);
		return "usr/userList";
	}

	@RequiresPermissions("user:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<UserAdminVo> list(UserQueryModel userQueryModel, String inviterNicknameLK) {
		if(StringUtils.isNotBlank(inviterNicknameLK)) {
			UserQueryModel inviterQueryModel = new UserQueryModel();
			inviterQueryModel.setNicknameLK(inviterNicknameLK);
			List<User> inviters = userService.findAll(inviterQueryModel);
			if(inviters.isEmpty()) {
				return new Grid<UserAdminVo>(PageBuilder.empty(userQueryModel.getPageSize(), userQueryModel.getPageNumber()));
			}
			if(inviters != null && inviters.size() > 0) {
				Long[] inviterIds = inviters.stream().map(v -> v.getId()).toArray(Long[]::new);
				userQueryModel.setInviterIdIN(inviterIds);
			}
		}
		Page<User> page = userService.findPage(userQueryModel);
		Page<UserAdminVo> voPage = PageBuilder.copyAndConvert(page, userComponent::buildAdminVo);
		return new Grid<>(voPage);
	}

	@RequiresPermissions("user:view")
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public String detail(Long id, Model model, Boolean isPure) {

		model.addAttribute("isPure", isPure == null ? false : isPure);
		model.addAttribute("levelUuid", Identities.uuid2());
		model.addAttribute("treeUuid", Identities.uuid2());
		model.addAttribute("upgradeUuid", Identities.uuid2());

		User persistence = userService.findOne(id);
		validate(persistence, NOT_NULL, "user is null");
		model.addAttribute("user", userComponent.buildAdminFullVo(persistence));

		List<User> allUsers = localCacheComponent.getUsers();
		Map<Long, User> userMap = localCacheComponent.getUserMap();
		User current = userMap.get(id);
		List<User> parents = new ArrayList<User>();
		Long parentId = current.getParentId();
		while (parentId != null) {
			User parentUser = cahceComponent.getUser(parentId);
			parentId = parentUser.getParentId();
			parents.add(parentUser);
		}

		Collections.reverse(parents);

		List<Map<String, Object>> list = parents.stream().map(user -> {
			Map<String, Object> map = new HashMap<>();

			UserRank userRank = user.getUserRank();
			if (userRank != null && userRank != UserRank.V0) {
				map.put("iconSkin", "rank" + userRank.getLevel());
			}

			map.put("id", user.getId());
			map.put("pId", user.getParentId());
			map.put("name", user.getNickname());
			map.put("open", true);
			map.put("isParent", true);
			return map;
		}).collect(Collectors.toList());

		{
			Map<String, Object> map = new HashMap<>();
			UserRank userRank = persistence.getUserRank();
			if (userRank != null && userRank != UserRank.V0) {
				map.put("iconSkin", "rank" + userRank.getLevel());
			}
			map.put("id", persistence.getId());
			map.put("pId", persistence.getParentId());
			map.put("name", persistence.getNickname() + "  (自己)");
			map.put("open", true);
			map.put("isParent", allUsers.stream().filter(v -> persistence.getId().equals(v.getParentId())).findFirst().isPresent());
			list.add(map);
		}


		List<User> children = allUsers.stream().filter(v -> id.equals(v.getParentId())).collect(Collectors.toList());
		while (!children.isEmpty()) {
			List<Map<String, Object>> childrenList = children.stream().map(user -> {
				Map<String, Object> map = new HashMap<>();

				UserRank userRank = user.getUserRank();
				if (userRank != null && userRank != UserRank.V0) {
					map.put("iconSkin", "rank" + userRank.getLevel());
				}

				map.put("id", user.getId());
				map.put("pId", user.getParentId());
				map.put("name", user.getNickname());
				map.put("open", true);
				map.put("isParent", allUsers.stream().filter(v -> user.getId().equals(v.getParentId())).findFirst().isPresent());
				return map;
			}).collect(Collectors.toList());

			Map<Long, Boolean> childrenIdMap = children.stream().collect(Collectors.toMap(v -> v.getId(), v -> true));
			List<User> childrenOfChildren = allUsers.stream().filter(v -> childrenIdMap.get(v.getParentId()) != null).collect(Collectors.toList());
			children.clear();
			children.addAll(childrenOfChildren);
			list.addAll(childrenList);
		}
		model.addAttribute("json", JsonUtils.toJson(list));
		return "usr/userDetail";
	}

	@RequiresPermissions("user:edit")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String update(@PathVariable Long id, Model model) {
		checkAndValidateIsPlatform(id);
		model.addAttribute("user", userService.findOne(id));
		return "usr/userUpdate";
	}

	@RequiresPermissions("user:edit")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> update(Long userId, String newPassword, RedirectAttributes redirectAttributes) {

		checkAndValidateIsPlatform(userId);
		validate(userId, NOT_NULL, "user id is null");

		User persistence = userService.findOne(userId);
		validate(persistence, NOT_NULL, "user is null, id =" + userId);

		if (persistence.getUserType() != UserType.代理) {
			throw new BizException(BizCode.ERROR, "只能修改服务商密码");
		}

		try {
			userService.modifyPasswordAdmin(userId, newPassword, getPrincipalUserId());
		} catch (Exception e) {
			return ResultBuilder.error(e.getMessage());
		}
		return ResultBuilder.ok("用户[" + persistence.getNickname() + "]资料修改成功");
	}

	@RequiresPermissions("user:edit")
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> modify(@RequestParam Long userId, String nickname, String phone) {
		if(StringUtils.isNotBlank(nickname)) {
			userService.modifyNicknameAdmin(userId, nickname, getPrincipalUserId());
		}
		if(StringUtils.isNotBlank(phone)) {
			userService.modifyPhoneAdmin(userId, phone, getPrincipalUserId());
		}
		return ResultBuilder.ok("操作成功");
	}

	@RequiresPermissions("user:addVip")
	@RequestMapping("/addVip")
	@ResponseBody
	public Result<?> addVip(Long id, UserRank userRank, String remark) {
		checkAndValidateIsPlatform(id);
		try {
			userService.modifyUserRankAdmin(id, userRank, getPrincipalUserId(), remark);
		} catch (Exception e) {
			return new ResultBuilder<>().message(e.getMessage()).build();
		}
		return new ResultBuilder<>().message("加VIP成功").build();
	}

	/**
	 * 设置大区
	 * @param id
	 * @param largeArea3
	 * @param remark2
     * @return
     */
	@RequiresPermissions("user:setLargeArea")
	@RequestMapping("/setLargeArea")
	@ResponseBody
	public Result<?> setLargeArea(Long id, String largeArea3, String remark2) {
		User user = userService.findOne(id);
		if(user != null && user.getUserRank() == UserRank.V4){
			return new ResultBuilder<>().error("设置大区失败，特级用户才能设置大区");
		}
		try {
			userService.modifyLargeAreaAdmin(id, largeArea3, getPrincipalUserId(), remark2);
		} catch (Exception e) {
			return new ResultBuilder<>().message(e.getMessage()).build();
		}
		return new ResultBuilder<>().message("设置大区成功").build();
	}

	@RequiresPermissions("user:freeze")
	@RequestMapping("/freeze/{id}")
	public String freeze(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		checkAndValidateIsPlatform(id);
		try {
			userService.freezeAdmin(id, getPrincipalUserId());
			redirectAttributes.addFlashAttribute(ResultBuilder.ok("用户冻结成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error(e.getMessage()));
		}
		return "redirect:/user";
	}

	@RequiresPermissions("user:freeze")
	@RequestMapping("/unFreeze/{id}")
	public String unFreeze(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		checkAndValidateIsPlatform(id);
		try {
			userService.unfreezeAdmin(id, getPrincipalUserId());
			redirectAttributes.addFlashAttribute(ResultBuilder.ok("用户解结成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error(e.getMessage()));
		}
		return "redirect:/user";
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create() {
		return "usr/userCreate";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(Model model, RedirectAttributes redirectAttributes, String passwordSure) {
		redirectAttributes.addFlashAttribute(MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("注册失败"));
		// TODO
		return "redirect:/user";
	}

	@RequestMapping(value = "/checkPhone")
	@ResponseBody
	public boolean checkPhone(@RequestParam String phone, Long id) {
		User user = userService.findByPhone(phone);
		if(user != null) {
			if(user.getId() != id) {
				return false;
			}
		}
		return true;
	}

	@RequiresPermissions("user:modifyParent")
	@RequestMapping(value = "/modifyParent", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> modifyParent(Long userId, String parentPhone, String remark) {
		User user = userService.findOne(userId);
		validate(user, NOT_NULL, "user id" + userId + " not fount");

		User parentUser = userService.findByPhone(parentPhone);
		if(parentUser == null) {
			return ResultBuilder.error("邀请人手机号不存在");
		}
		try {
			userService.modifyParentIdAdmin(userId, parentUser.getId(), getPrincipalUserId(), remark);
			return ResultBuilder.ok("修改推荐人成功");
		} catch (Exception e) {
			return ResultBuilder.error(e.getMessage());
		}
	}

//	@RequiresPermissions("user:setRoot")
//	@RequestMapping(value = "/setRoot", method = RequestMethod.POST)
//	@ResponseBody
//	public Result<?> setRoot(@RequestParam Long id, @RequestParam String rootName, Boolean isRoot, String remark) {
//		userService.modifyIsRootAdmin(id, isRoot, rootName, getPrincipalUserId(), remark);
//		return ResultBuilder.ok("操作成功");
//	}

//	@RequiresPermissions("user:setBoss")
//	@RequestMapping(value = "/setBoss", method = RequestMethod.POST)
//	@ResponseBody
//	public Result<Void> setBoss(@RequestParam Long id, @RequestParam String bossName) {
//		userService.modifyIsBoss(id, true, bossName, getPrincipalUserId());
//		return ResultBuilder.ok("操作成功");
//	}
//
//	@RequiresPermissions("user:setBoss")
//	@RequestMapping(value = "/setBoss", method = RequestMethod.GET)
//	public String setBoss(@RequestParam Long id, RedirectAttributes redirectAttributes) {
//		try {
//			userService.modifyIsBoss(id, false, null, getPrincipalUserId());
//			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("操作成功"));
//		} catch (Exception e) {
//			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
//		}
//		return "redirect:/user";
//	}

//	@RequiresPermissions("user:setBoss")
//	@RequestMapping(value = "/boss/joinTeam", method = RequestMethod.POST)
//	@ResponseBody
//	public Result<Void> joinTeam(@RequestParam Long id, @RequestParam String bossPhone) {
//		User boss = userService.findByPhone(bossPhone);
//		if (boss == null) {
//			return ResultBuilder.error("总经理手机号不存在");
//		}
//		userService.modifyBossId(id, boss.getId());
//		return ResultBuilder.ok("已加入: " + boss.getBossName());
//	}

	/**
	 * 加入大区总裁团队
	 * @param id
	 * @param presidentPhone
     * @return
     */
	@RequestMapping(value = "/president/joinTeam", method = RequestMethod.POST)
	@ResponseBody
	public Result<Void> joinPresidentTeam(@RequestParam Long id, @RequestParam String presidentPhone) {
		User president = userService.findByPhone(presidentPhone);
		if (president == null) {
			return ResultBuilder.error("大区总裁手机号不存在");
		}
		userService.modifyPresidentId(id, president.getId());
		return ResultBuilder.ok("已加入: " + president.getNickname()+"大区总裁团队");
	}

	@RequiresPermissions("user:setDirector")
	@RequestMapping(value = "/setDirector", method = RequestMethod.GET)
	public String setDirector(@RequestParam Long id, RedirectAttributes redirectAttributes) {
		userService.modifyIsDirector(id, true);
		redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("操作成功"));
		return "redirect:/user";
	}

	@RequiresPermissions("user:setDirector")
	@RequestMapping(value = "/isHonorDirector", method = RequestMethod.GET)
	public String isHonorDirector(@RequestParam Long id, RedirectAttributes redirectAttributes) {
		userService.modifyIsHonorDirector(id, true);
		redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("操作成功"));
		return "redirect:/user";
	}

	@RequiresPermissions("user:setShareholder")
	@RequestMapping(value = "/setShareholder", method = RequestMethod.GET)
	public String setShareholder(@RequestParam Long id, RedirectAttributes redirectAttributes) {
		userService.modifyIsShareholder(id, true);
		redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("操作成功"));
		return "redirect:/user";
	}

//	@RequiresPermissions("user:setRoot")
//	@RequestMapping(value = "/setRoot", method = RequestMethod.GET)
//	public String setRoot(@RequestParam Long id, RedirectAttributes redirectAttributes) {
//		userService.modifyIsRootAdmin(id, false, null, getPrincipalUserId(), null);
//		redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("操作成功"));
//		return "redirect:/user";
//	}

	@RequiresPermissions("user:setIsDeleted")
	@RequestMapping(value = "/setIsDeleted", method = RequestMethod.GET)
	public String setIsDeleted(@RequestParam Long id, RedirectAttributes redirectAttributes) {
		userService.modifyIsDeleted(id, true);
		redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("操作成功"));
		return "redirect:/user";
	}

	@RequiresPermissions("user:setIsToV4")
	@RequestMapping(value = "/setIsToV4", method = RequestMethod.GET)
	public String setIsToV4(@RequestParam Long id, RedirectAttributes redirectAttributes) {
		userService.modifyIsToV4(id, true);
		redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("操作成功"));
		return "redirect:/user";
	}

	private void checkAndValidateIsPlatform(Long userId) {
		User user = userService.findOne(userId);
		validate(user, NOT_NULL, "user is null, id = " + userId);
		if(user.getUserType() == UserType.平台) {
			throw new UnauthorizedException();
		}
	}

	private Long getPrincipalUserId() {
		AdminPrincipal principal = (AdminPrincipal)SecurityUtils.getSubject().getPrincipal();
		return principal.getUserId();
	}


	@RequiresPermissions("user:edit")
	@RequestMapping(value = "/editLastLoginTime", method = RequestMethod.POST)
	@ResponseBody
	public boolean editLastLoginTime(@RequestParam Long id) {
		User user = userService.findOne(id);
		validate(user, NOT_NULL, "user not found, id is " + id);
		userService.modifyLastLoginTime(id);
		return true;
	}

	/**
	 * 设置大区总裁
	 * @param id
	 * @param redirectAttributes
	 * @param flag
     * @return
     */
	@RequiresPermissions("user:setIsPresident")
	@RequestMapping(value = "/setIsPresident", method = RequestMethod.GET)
	public String setIsPresident(@RequestParam Long id, RedirectAttributes redirectAttributes,Integer flag) {
		User user = userService.findOne(id);
		if(null != user && user.getUserRank() == UserRank.V4){
			if(user.getLargearea() == null){
				redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("操作失败，用户未设置大区"));
			}else {
				//设置大区总裁
				if(null != flag && flag == 0){
					userService.modifyIsPresident(id,getPrincipalUserId(), true);
				}else {//取消大区总裁
					userService.modifyIsPresident(id,getPrincipalUserId(),false);
				}
				redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("操作成功"));
			}
		}else{
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("操作失败，非特级用户不能设置大区总裁"));
		}
		return "redirect:/user";
	}

//	/**
//	 * 批量设置大区总裁
//	 * @param ids
//	 * @return
//     */
//	@RequiresPermissions("user:setIsPresident")
//	@RequestMapping(value = "/batchSetPresident")
//	@ResponseBody
//	public Result<?> batchSetPresident(@NotBlank String ids) {
//		if(StringUtils.isBlank(ids)){
//			return ResultBuilder.error("请至少选择一条记录！");
//		} else {
//			String[] idStringArray = ids.split(",");
//			for(String idString : idStringArray){
//				Long id = new Long(idString);
//				User user = userService.findOne(id);
//				validate(user, NOT_NULL, "user is null, id = " + id);
//				if(user.getUserRank() != UserRank.V4){
//					return ResultBuilder.error("操作失败，非特级用户不能设置大区总裁");
//				}
//				if(user.getLargearea() == null){
//					return ResultBuilder.error("操作失败，用户"+user.getNickname()+"未设置大区");
//				}
//				try {
//					userService.modifyIsPresident(id,true);
//				} catch (Exception e) {
//					return ResultBuilder.error(e.getMessage());
//				}
//			}
//			return ResultBuilder.ok("批量设置成功！");
//		}
//	}

//	/**
//	 * 批量取消大区总裁
//	 * @param ids
//	 * @return
//     */
//	@RequiresPermissions("user:setIsPresident")
//	@RequestMapping(value = "/batchCancelPresident")
//	@ResponseBody
//	public Result<?> batchCancelPresident(@NotBlank String ids) {
//		if(StringUtils.isBlank(ids)){
//			return ResultBuilder.error("请至少选择一条记录！");
//		} else {
//			String[] idStringArray = ids.split(",");
//			for(String idString : idStringArray){
//				Long id = new Long(idString);
//				try {
//					userService.modifyIsPresident(id,false);
//				} catch (Exception e) {
//					return ResultBuilder.error(e.getMessage());
//				}
//			}
//			return ResultBuilder.ok("批量取消成功！");
//		}
//	}

	/**
	 * 批量设置大区
	 * @param ids
	 * @param largeArea2
	 * @param remark3
     * @return
     */
	@RequiresPermissions("user:setLargeArea")
	@RequestMapping(value = "/batchSetLargeArea")
	@ResponseBody
	public Result<?> modifyValidTime(@NotBlank String ids, String largeArea2, String remark3) {
		if(StringUtils.isBlank(ids)){
			return ResultBuilder.error("请至少选择一条记录！");
		} else {
			String[] idStringArray = ids.split(",");
			for(String idString : idStringArray){
				Long id = new Long(idString);
				User user = userService.findOne(id);
				validate(user, NOT_NULL, "user id" + id + " not found");
				try {
					userService.modifyLargeAreaAdmin(id, largeArea2, getPrincipalUserId(), remark3);
				} catch (Exception e) {
					return ResultBuilder.error(e.getMessage());
				}
			}
			return ResultBuilder.ok("批量设置成功！");
		}
	}


}
