package com.zy.admin.controller.rpt;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.zy.common.util.ExcelUtils;
import com.zy.common.util.WebUtils;
import com.zy.entity.fnc.*;
import com.zy.model.FinanceReportVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.component.LocalCacheComponent;
import com.zy.entity.mal.Order;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserRank;
import com.zy.model.OrderQuantityReportVo;
import com.zy.util.GcUtils;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/report/orderQuantity")
public class OrderQuantityReportController {

	@Autowired
	private LocalCacheComponent localCacheComponent;
	
	@RequiresPermissions("orderQuantity:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("userRankMap", Arrays.asList(User.UserRank.values()).stream().collect(Collectors.toMap(v->v, v-> GcUtils.getUserRankLabel(v),(u, v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); }, LinkedHashMap::new)) );
		return "rpt/orderQuantityReport";
	}
	
	@RequiresPermissions("orderQuantity:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<OrderQuantityReportVo> list(OrderQuantityReportVo.OrderQuantityReportVoQueryModel orderQuantityReportVoQueryModel) {
		
		List<User> date = new ArrayList<User>();
		List<User> users = localCacheComponent.getUsers();
		List<User> filterUser = users.stream().filter(user -> {
			boolean result = true;
			String nicknameLK = orderQuantityReportVoQueryModel.getNicknameLK();
			String phoneEQ = orderQuantityReportVoQueryModel.getPhoneEQ();
			UserRank userRankEQ = orderQuantityReportVoQueryModel.getUserRankEQ();
			
			if (!StringUtils.isBlank(nicknameLK)) {
				result = result && StringUtils.contains(user.getNickname(), nicknameLK);
			}
			if (!StringUtils.isBlank(phoneEQ)) {
				result = result && phoneEQ.equals(user.getPhone());
			}
			if(userRankEQ != null) {
				result = result && userRankEQ == user.getUserRank();
			}
			return result;
		}).collect(Collectors.toList());
		List<Long> userIds = filterUser.stream().map(v -> v.getId()).collect(Collectors.toList());
		if(userIds.isEmpty()) {
			return new Grid<>(PageBuilder.empty(orderQuantityReportVoQueryModel.getPageSize(), 0));
		}
		Map<Long, Boolean> userIdMap = userIds.stream().collect(Collectors.toMap(v -> v, v -> true));
		
		List<Order> orders = localCacheComponent.getOrders();
		List<Order> filterOrders = orders.stream().filter(order -> {
			boolean result = userIdMap.get(order.getUserId()) != null;
			
			Date createdTimeGTE = orderQuantityReportVoQueryModel.getCreatedTimeGTE();
			Date createdTimeLT = orderQuantityReportVoQueryModel.getCreatedTimeLT();
			Date paidTimeGTE = orderQuantityReportVoQueryModel.getPaidTimeGTE();
			Date paidTimeLT = orderQuantityReportVoQueryModel.getPaidTimeLT();
			
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
		
		int totalCount = filterUser.size();
		Integer pageSize = orderQuantityReportVoQueryModel.getPageSize();
		Integer pageNumber = orderQuantityReportVoQueryModel.getPageNumber();
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
				date.add(filterUser.get(index));
			}
		}
		
		Map<Long, OrderQuantityReportVo> orderQuantityReportMap = date.stream().collect(Collectors.toMap(v -> v.getId(), v -> {
			OrderQuantityReportVo orderQuantityReportVo = new OrderQuantityReportVo();
			orderQuantityReportVo.setNickname(v.getNickname());
			orderQuantityReportVo.setPhone(v.getPhone());
			orderQuantityReportVo.setCanceledSum(0L);
			orderQuantityReportVo.setDeliveredSum(0L);
			orderQuantityReportVo.setOrderedSum(0L);
			orderQuantityReportVo.setPaidSum(0L);
			orderQuantityReportVo.setReceivedSum(0L);
			orderQuantityReportVo.setRefundedSum(0L);
			return orderQuantityReportVo;
		}));
		
		for(Order order : filterOrders) {
			Long userId = order.getUserId();
			OrderQuantityReportVo orderQuantityReportVo = orderQuantityReportMap.get(userId);
			if(orderQuantityReportVo != null) {
				Long quantity = order.getQuantity();
				switch (order.getOrderStatus()) {
				case 待支付:
					orderQuantityReportVo.setOrderedSum(orderQuantityReportVo.getOrderedSum() + quantity);
					break;
				case 待确认:
					orderQuantityReportVo.setOrderedSum(orderQuantityReportVo.getOrderedSum() + quantity);
					break;
				case 已支付:
					orderQuantityReportVo.setOrderedSum(orderQuantityReportVo.getOrderedSum() + quantity);
					orderQuantityReportVo.setPaidSum(orderQuantityReportVo.getPaidSum() + quantity);
					break;
				case 已发货:
					orderQuantityReportVo.setOrderedSum(orderQuantityReportVo.getOrderedSum() + quantity);
					orderQuantityReportVo.setPaidSum(orderQuantityReportVo.getPaidSum() + quantity);
					orderQuantityReportVo.setDeliveredSum(orderQuantityReportVo.getDeliveredSum() + quantity);
					break;
				case 已完成:
					orderQuantityReportVo.setOrderedSum(orderQuantityReportVo.getOrderedSum() + quantity);
					orderQuantityReportVo.setPaidSum(orderQuantityReportVo.getPaidSum() + quantity);
					orderQuantityReportVo.setReceivedSum(orderQuantityReportVo.getReceivedSum() + + quantity);
					break;
				case 已退款:
					orderQuantityReportVo.setOrderedSum(orderQuantityReportVo.getOrderedSum() + quantity);
					orderQuantityReportVo.setPaidSum(orderQuantityReportVo.getPaidSum() + quantity);
					orderQuantityReportVo.setRefundedSum(orderQuantityReportVo.getRefundedSum() + quantity);
					break;
				case 已取消:
					orderQuantityReportVo.setOrderedSum(orderQuantityReportVo.getOrderedSum() + quantity);
					orderQuantityReportVo.setCanceledSum(orderQuantityReportVo.getCanceledSum() + quantity);
					break;
				default:
					break;
				}
				orderQuantityReportMap.put(userId, orderQuantityReportVo);
			}
		}
		
		List<OrderQuantityReportVo> orderQuantityReportVo = new ArrayList<>(orderQuantityReportMap.values());
		
		Page<OrderQuantityReportVo> page = new Page<>();
		page.setPageNumber(pageNumber);
		page.setPageSize(pageSize);
		page.setData(orderQuantityReportVo);
		page.setTotal(Long.valueOf(filterUser.size()));
		return new Grid<>(page);
	}

	@RequiresPermissions("orderQuantity:export")
	@RequestMapping("/export")
	public String export(OrderQuantityReportVo.OrderQuantityReportVoQueryModel orderQuantityReportVoQueryModel,
	                     HttpServletResponse response) throws IOException, ParseException {

		List<User> users = localCacheComponent.getUsers();
		List<User> filterUser = users.stream().filter(user -> {
			boolean result = true;
			String nicknameLK = orderQuantityReportVoQueryModel.getNicknameLK();
			String phoneEQ = orderQuantityReportVoQueryModel.getPhoneEQ();
			UserRank userRankEQ = orderQuantityReportVoQueryModel.getUserRankEQ();

			if (!StringUtils.isBlank(nicknameLK)) {
				result = result && StringUtils.contains(user.getNickname(), nicknameLK);
			}
			if (!StringUtils.isBlank(phoneEQ)) {
				result = result && phoneEQ.equals(user.getPhone());
			}
			if(userRankEQ != null) {
				result = result && userRankEQ == user.getUserRank();
			}
			return result;
		}).collect(Collectors.toList());
		List<Long> userIds = filterUser.stream().map(v -> v.getId()).collect(Collectors.toList());
		if(userIds.isEmpty()) {
			String fileName = "订单核算报表.xlsx";
			WebUtils.setFileDownloadHeader(response, fileName);
			OutputStream os = response.getOutputStream();
			ExcelUtils.exportExcel(new ArrayList<>(), OrderQuantityReportVo.class, os);
		}
		Map<Long, Boolean> userIdMap = userIds.stream().collect(Collectors.toMap(v -> v, v -> true));

		List<Order> orders = localCacheComponent.getOrders();
		List<Order> filterOrders = orders.stream().filter(order -> {
			boolean result = userIdMap.get(order.getUserId()) != null;

			Date createdTimeGTE = orderQuantityReportVoQueryModel.getCreatedTimeGTE();
			Date createdTimeLT = orderQuantityReportVoQueryModel.getCreatedTimeLT();
			Date paidTimeGTE = orderQuantityReportVoQueryModel.getPaidTimeGTE();
			Date paidTimeLT = orderQuantityReportVoQueryModel.getPaidTimeLT();

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

		Map<Long, OrderQuantityReportVo> orderQuantityReportMap = filterUser.stream().collect(Collectors.toMap(v -> v.getId(), v -> {
			OrderQuantityReportVo orderQuantityReportVo = new OrderQuantityReportVo();
			orderQuantityReportVo.setNickname(v.getNickname());
			orderQuantityReportVo.setPhone(v.getPhone());
			orderQuantityReportVo.setCanceledSum(0L);
			orderQuantityReportVo.setDeliveredSum(0L);
			orderQuantityReportVo.setOrderedSum(0L);
			orderQuantityReportVo.setPaidSum(0L);
			orderQuantityReportVo.setReceivedSum(0L);
			orderQuantityReportVo.setRefundedSum(0L);
			return orderQuantityReportVo;
		}));

		for(Order order : filterOrders) {
			Long userId = order.getUserId();
			OrderQuantityReportVo orderQuantityReportVo = orderQuantityReportMap.get(userId);
			if(orderQuantityReportVo != null) {
				Long quantity = order.getQuantity();
				switch (order.getOrderStatus()) {
					case 待支付:
						orderQuantityReportVo.setOrderedSum(orderQuantityReportVo.getOrderedSum() + quantity);
						break;
					case 待确认:
						orderQuantityReportVo.setOrderedSum(orderQuantityReportVo.getOrderedSum() + quantity);
						break;
					case 已支付:
						orderQuantityReportVo.setOrderedSum(orderQuantityReportVo.getOrderedSum() + quantity);
						orderQuantityReportVo.setPaidSum(orderQuantityReportVo.getPaidSum() + quantity);
						break;
					case 已发货:
						orderQuantityReportVo.setOrderedSum(orderQuantityReportVo.getOrderedSum() + quantity);
						orderQuantityReportVo.setPaidSum(orderQuantityReportVo.getPaidSum() + quantity);
						orderQuantityReportVo.setDeliveredSum(orderQuantityReportVo.getDeliveredSum() + quantity);
						break;
					case 已完成:
						orderQuantityReportVo.setOrderedSum(orderQuantityReportVo.getOrderedSum() + quantity);
						orderQuantityReportVo.setPaidSum(orderQuantityReportVo.getPaidSum() + quantity);
						orderQuantityReportVo.setReceivedSum(orderQuantityReportVo.getReceivedSum() + + quantity);
						break;
					case 已退款:
						orderQuantityReportVo.setOrderedSum(orderQuantityReportVo.getOrderedSum() + quantity);
						orderQuantityReportVo.setPaidSum(orderQuantityReportVo.getPaidSum() + quantity);
						orderQuantityReportVo.setRefundedSum(orderQuantityReportVo.getRefundedSum() + quantity);
						break;
					case 已取消:
						orderQuantityReportVo.setOrderedSum(orderQuantityReportVo.getOrderedSum() + quantity);
						orderQuantityReportVo.setCanceledSum(orderQuantityReportVo.getCanceledSum() + quantity);
						break;
					default:
						break;
				}
				orderQuantityReportMap.put(userId, orderQuantityReportVo);
			}
		}

		List<OrderQuantityReportVo> orderQuantityReportVo = new ArrayList<>(orderQuantityReportMap.values());

		String fileName = "订单核算报表.xlsx";
		WebUtils.setFileDownloadHeader(response, fileName);
		OutputStream os = response.getOutputStream();
		ExcelUtils.exportExcel(orderQuantityReportVo, OrderQuantityReportVo.class, os);
		return null;
	}
}
