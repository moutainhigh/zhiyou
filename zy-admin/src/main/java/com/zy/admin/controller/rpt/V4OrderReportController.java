package com.zy.admin.controller.rpt;

import com.zy.common.exception.BizException;
import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.tree.TreeHelper;
import com.zy.common.model.tree.TreeNode;
import com.zy.common.model.ui.Grid;
import com.zy.component.LocalCacheComponent;
import com.zy.entity.mal.Order;
import com.zy.entity.usr.User;
import com.zy.model.BizCode;
import com.zy.model.TeamModel;
import com.zy.model.V4OrderReportVo;
import com.zy.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/report/v4Order")
public class V4OrderReportController {

	@Autowired
	private UserService userService;

	@Autowired
	private LocalCacheComponent localCacheComponent;

	@RequiresPermissions("v4TreeReport:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model, Long userId) {
		if (userId != null) {
			model.addAttribute("directV4ParentIdEQ", userId);
		}
		return "rpt/v4OrderReport";
	}

	@RequiresPermissions("v4TreeReport:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<V4OrderReportVo> list(V4OrderReportVo.V4OrderReportVoQueryModel v4OrderReportVoQueryModel) {
		Long directV4ParentIdEQ = v4OrderReportVoQueryModel.getDirectV4ParentIdEQ();
		if(directV4ParentIdEQ == null) {
			return new Grid<>(PageBuilder.empty(v4OrderReportVoQueryModel.getPageSize(), 0));
		}
		List<User> pageV4Users = new ArrayList<User>();
		Predicate<User> predicate = v -> {
			User.UserRank userRank = v.getUserRank();
			if (userRank == User.UserRank.V4) {
				return true;
			}
			return false;
		};
		List<User> users = localCacheComponent.getUsers();
		List<User> v4Users = users.stream().filter(predicate).collect(Collectors.toList());
		Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, Function.identity()));
		Map<Long, TeamModel> teamMap = v4Users.stream()
				.map(v -> {
					TeamModel teamModel = new TeamModel();
					teamModel.setUser(v);
					teamModel.setV4Children(TreeHelper.sortBreadth2(v4Users, String.valueOf(v.getId()), u -> {
						TreeNode treeNode = new TreeNode();
						treeNode.setId(String.valueOf(u.getId()));
						Long directV4ParentId = getDirectV4ParentId(predicate, userMap, u);
						treeNode.setParentId(u.getParentId() == null ? null : String.valueOf(directV4ParentId));
						return treeNode;

					}));
					teamModel.setDirectV4Children(v4Users.stream().filter(u -> {
						Long userId = u.getId();
						if (userId.equals(v.getId())) {
							return false;
						}
						Long directV4ParentId = getDirectV4ParentId(predicate, userMap, u);
						if (directV4ParentId != null && directV4ParentId.equals(v.getId())) {
							return true;
						}
						return false;
					}).collect(Collectors.toList()));
					return teamModel;
				}).collect(Collectors.toMap(v -> v.getUser().getId(), Function.identity()));
		TeamModel teamModel = teamMap.get(directV4ParentIdEQ);
		List<User> filterV4User = teamModel.getDirectV4Children().stream().filter(user -> {
			boolean result = true;
			String nicknameLK = v4OrderReportVoQueryModel.getNicknameLK();
			String phoneEQ = v4OrderReportVoQueryModel.getPhoneEQ();

			if (!StringUtils.isBlank(nicknameLK)) {
				result = result && StringUtils.contains(user.getNickname(), nicknameLK);
			}
			if (!StringUtils.isBlank(phoneEQ)) {
				result = result && phoneEQ.equals(user.getPhone());
			}
			return result;
		}).collect(Collectors.toList());
		filterV4User.add(0, userService.findOne(directV4ParentIdEQ));

		List<Long> userIds = filterV4User.stream().map(v -> v.getId()).collect(Collectors.toList());
		if(userIds.isEmpty()) {
			return new Grid<>(PageBuilder.empty(v4OrderReportVoQueryModel.getPageSize(), 0));
		}

		Map<Long, User> v4UserMap = v4Users.stream().collect(Collectors.toMap(User::getId, Function.identity()));

		List<Order> orders = localCacheComponent.getOrders();
		List<Order> filterOrders = orders.stream()
				.filter(order -> order.getOrderStatus() == Order.OrderStatus.已支付
						|| order.getOrderStatus() == Order.OrderStatus.已发货
						|| order.getOrderStatus() == Order.OrderStatus.已完成)
				.filter(order -> {
			boolean result = true;

			Date createdTimeGTE = v4OrderReportVoQueryModel.getCreatedTimeGTE();
			Date createdTimeLT = v4OrderReportVoQueryModel.getCreatedTimeLT();
			Date paidTimeGTE = v4OrderReportVoQueryModel.getPaidTimeGTE();
			Date paidTimeLT = v4OrderReportVoQueryModel.getPaidTimeLT();

			Date createdTime = order.getCreatedTime();
			Date paidTime = order.getPaidTime();
			if (createdTimeGTE != null) {
				if(createdTime == null) {
					result = false;
				} else {
					result = result && (createdTime.after(createdTimeGTE) || createdTime.equals(createdTimeGTE));
				}
			}
			if (createdTimeLT != null) {
				if(createdTime == null) {
					result = false;
				} else {
					result = result && createdTime.before(createdTimeLT);
				}
			}
			if (paidTimeGTE != null) {
				if(paidTime == null) {
					result = false;
				} else {
					result = result && (paidTime.after(paidTimeGTE) || paidTime.equals(paidTimeGTE));
				}
			}
			if (paidTimeLT != null) {
				if(paidTime == null) {
					result = false;
				} else {
					result = result && paidTime.before(paidTimeLT);
				}
			}
			return result;
		}).collect(Collectors.toList());

		int totalCount = filterV4User.size();
		Integer pageSize = v4OrderReportVoQueryModel.getPageSize();
		Integer pageNumber = v4OrderReportVoQueryModel.getPageNumber();
		if (pageSize == null) {
			pageSize = 20;
		} else if (pageSize == -1) {
			pageSize = totalCount + 1;
		}
		if (pageNumber == null) {
			pageNumber = 0;
		}
		int from = pageNumber * pageSize;
		if (from < totalCount) {
			int to = (from + pageSize) > totalCount ? totalCount : from + pageSize;
			for (int index = from; index < to; index++) {
				pageV4Users.add(filterV4User.get(index));
			}
		}

		Map<Long, Long> userInQuantityMap = filterOrders.stream().filter(v -> v.getBuyerUserRank() == User.UserRank.V4||(v.getQuantity()==3600&&v.getSellerId()==1)).collect(Collectors.toMap(Order::getUserId, Order::getQuantity, (x, y) -> x + y));
		Map<Long, Long> userOutQuantityMap = filterOrders.stream().filter(v -> v.getSellerUserRank() == User.UserRank.V4).collect(Collectors.toMap(Order::getSellerId, Order::getQuantity, (x, y) -> x + y));
		Map<Long, Long> teamInQuantityMap = pageV4Users.stream().collect(Collectors.toMap(User::getId, v -> {
			Long userId = v.getId();
			List<User> v4Childres = teamMap.get(userId).getV4Children();
			Long teamInQuantity = 0l;
			if(!v4Childres.isEmpty()) {
				teamInQuantity = v4Childres.stream()
						.map(u -> userInQuantityMap.get(u.getId()) == null ? 0L : userInQuantityMap.get(u.getId()))
						.reduce(0L, (x, y) -> x + y);
			}
			Long myInQuantity = userInQuantityMap.get(userId) == null ? 0L : userInQuantityMap.get(userId);
			return myInQuantity + teamInQuantity;
		}));
		Map<Long, Long> teamOutQuantityMap = pageV4Users.stream().collect(Collectors.toMap(User::getId, v -> {
			Long userId = v.getId();
			Long myOutQuantity = userOutQuantityMap.get(userId) == null ? 0L : userOutQuantityMap.get(userId);
			List<User> v4Childres = teamMap.get(userId).getV4Children();
			Long teamOutQuantity = 0L;
			if(!v4Childres.isEmpty()) {
				teamOutQuantity = v4Childres.stream()
						.map(u -> userOutQuantityMap.get(u.getId()) == null ? 0L : userOutQuantityMap.get(u.getId()))
						.reduce(0L, (x, y) -> x + y);
			}
			return myOutQuantity + teamOutQuantity;
		}));

		/* 团队 */
		List<V4OrderReportVo> v4OrderReportVos = pageV4Users.stream().map(v ->{
			Long userId = v.getId();
			V4OrderReportVo v4OrderReportVo = new V4OrderReportVo();
			v4OrderReportVo.setNickname(v.getNickname());
			v4OrderReportVo.setPhone(v.getPhone());
			v4OrderReportVo.setInQuantitySum(userInQuantityMap.get(userId) == null? 0L : userInQuantityMap.get(userId));
			v4OrderReportVo.setOutQuantitySum(userOutQuantityMap.get(userId) == null? 0L : userOutQuantityMap.get(userId));
			v4OrderReportVo.setTeamInQuantitySum(teamInQuantityMap.get(userId));
			v4OrderReportVo.setTeamOutQuantitySum(teamOutQuantityMap.get(userId));
			TeamModel teamModelInner = teamMap.get(userId);
			v4OrderReportVo.setDirectV4ChildrenCount(Long.valueOf(teamModelInner.getDirectV4Children().size()));
			v4OrderReportVo.setV4ChildrenCount(Long.valueOf(teamModelInner.getV4Children().size()));
			return v4OrderReportVo;
		}).collect(Collectors.toList());

		Page<V4OrderReportVo> page = new Page<>();
		page.setPageNumber(pageNumber);
		page.setPageSize(pageSize);
		page.setData(v4OrderReportVos);
		page.setTotal(Long.valueOf(filterV4User.size()));
		return new Grid<>(page);
	}

	private Long getDirectV4ParentId(Predicate<User> predicate, Map<Long, User> userMap, User u) {
		int times = 0;
		Long directV4ParentId = null;
		Long parentId = u.getParentId();
		while(parentId != null) {
			if (times > 1000) {
				throw new BizException(BizCode.ERROR, "循环引用");
			}
			User parent = userMap.get(parentId);
			if(parent != null) {
				if (predicate.test(parent)) {
					directV4ParentId = parentId;
					break;
				}
			}
			parentId = parent.getParentId();
			times ++;
		}
		return directV4ParentId;
	}
}
