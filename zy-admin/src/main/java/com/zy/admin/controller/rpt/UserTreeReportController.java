package com.zy.admin.controller.rpt;

import com.zy.common.model.tree.TreeHelper;
import com.zy.common.model.tree.TreeNode;
import com.zy.component.LocalCacheComponent;
import com.zy.entity.mal.Order;
import com.zy.entity.mal.OrderItem;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserRank;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequestMapping("/report/userTree")
@Controller
public class UserTreeReportController {

	@Autowired
	private LocalCacheComponent localCacheComponent;

	@RequiresPermissions("userTreeReport:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "rpt/userTreeReport";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("userTreeReport:view")
	public List<Map<String, Object>> listAjax(Long parentId) {
		List<User> allUsers = localCacheComponent.getUsers();

		List<User> users;
		if (parentId == null) {
			users = allUsers.stream().filter(v -> v.getUserRank() == UserRank.V4 && v.getParentId() == null).collect(Collectors.toList());
		} else {
			users = allUsers.stream().filter(v -> parentId.equals(v.getParentId())).collect(Collectors.toList());
		}
		
		return users.stream().map(user -> {
			Map<String, Object> map = new HashMap<>();

			UserRank userRank = user.getUserRank();
			if (userRank != null && userRank != UserRank.V0) {
				map.put("iconSkin", "rank" + userRank.getLevel());
			}

			map.put("id", user.getId());
			map.put("name", user.getNickname());
			map.put("isParent", allUsers.stream().filter(v -> user.getId().equals(v.getParentId())).findFirst().isPresent());
			return map;
		}).collect(Collectors.toList());
		
	}


	@RequestMapping("/order")
	@RequiresPermissions("userTreeReport:view")
	public String orderStastics(Long userId, Model model) {
		List<User> users = localCacheComponent.getUsers();
		List<Order> orders = localCacheComponent.getOrders();
		Map<Long, OrderItem> orderItemMap = localCacheComponent.getOrderItemMap();


		List<User> children = TreeHelper.sortBreadth(users, userId.toString(), v -> {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(v.getId().toString());
			treeNode.setParentId(v.getParentId() == null ? null : v.getParentId().toString());
			return treeNode;
		});
		Set<Long> childIds = children.stream().map(v -> v.getId()).collect(Collectors.toSet());

		Predicate<Order> predicate = v -> v.getOrderStatus() == Order.OrderStatus.已支付 || v.getOrderStatus() == Order.OrderStatus.已发货 || v.getOrderStatus() == Order.OrderStatus.已完成;

		Long inCount = orders.stream().filter(v -> v.getUserId().equals(userId)).filter(predicate).map(v -> orderItemMap.get(v.getId()).getQuantity()).reduce(0L, (u, v) -> u + v);
		Long outCount = orders.stream().filter(v -> v.getSellerId().equals(userId)).filter(predicate).map(v -> orderItemMap.get(v.getId()).getQuantity()).reduce(0L, (u, v) -> u + v);

		Long teamInCount = orders.stream().filter(v -> childIds.contains(v.getUserId())).filter(predicate).map(v -> orderItemMap.get(v.getId()).getQuantity()).reduce(0L, (u, v) -> u + v);
		Long teamOutCount = orders.stream().filter(v -> childIds.contains(v.getSellerId())).filter(predicate).map(v -> orderItemMap.get(v.getId()).getQuantity()).reduce(0L, (u, v) -> u + v);

		model.addAttribute("teamCount", children.size());
		model.addAttribute("inCount", inCount);
		model.addAttribute("outCount", outCount);
		model.addAttribute("teamInCount", teamInCount);
		model.addAttribute("teamOutCount", teamOutCount);

		return "rpt/orderStastics";
	}


	
}
