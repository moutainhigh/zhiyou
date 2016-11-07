package com.zy.admin.controller.rpt;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.zy.common.model.query.Page;
import com.zy.common.model.ui.Grid;
import com.zy.common.util.ExcelUtils;
import com.zy.common.util.WebUtils;
import com.zy.component.LocalCacheComponent;
import com.zy.entity.mal.Order;
import com.zy.entity.mal.Order.OrderStatus;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserRank;
import com.zy.model.OrderReportVo;
import com.zy.model.OrderReportVo.OrderReportVoItem;
import com.zy.model.query.ReportQueryModel;
import com.zy.util.GcUtils;
import com.zy.vo.UserReportVo;

@Controller
@RequestMapping("/report/order/month")
public class OrderReportMonthController {

	@Autowired
	private LocalCacheComponent localCacheComponent;
	
	@RequiresPermissions("orderReport:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("timeLabels", getTimeLabels());
		model.addAttribute("userRankMap", Arrays.asList(User.UserRank.values()).stream().collect(Collectors.toMap(v->v, v-> GcUtils.getUserRankLabel(v),(u, v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); }, LinkedHashMap::new)) );
		model.addAttribute("rootNames", localCacheComponent.getRootNames());
		return "rpt/orderMonthReport";
	}
	
	@RequiresPermissions("orderReport:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<OrderReportVo> listMonth(OrderReportVo.OrderReportVoQueryModel orderReportVoQueryModel) {
		
		List<UserReportVo> data = new ArrayList<>();
		List<UserReportVo> all = localCacheComponent.getuserReportVos();
		List<UserReportVo> filtered = all.stream()
			.filter(v -> v.getUserRank() == UserRank.V4)
			.filter(userReportVo -> {
			boolean result = true;
			Long provinceIdEQ = orderReportVoQueryModel.getProvinceIdEQ();
			Long cityIdEQ = orderReportVoQueryModel.getCityIdEQ();
			Long districtIdEQ = orderReportVoQueryModel.getDistrictIdEQ();
			String nicknameLK = orderReportVoQueryModel.getNicknameLK();
			String phoneEQ = orderReportVoQueryModel.getPhoneEQ();
			String rootRootNameLK = orderReportVoQueryModel.getRootRootNameLK();
			String v4UserNicknameLK = orderReportVoQueryModel.getV4UserNicknameLK();
			
			if (provinceIdEQ != null) {
				result = result && provinceIdEQ.equals(userReportVo.getProvinceId());
			}
			if (cityIdEQ != null) {
				result = result && cityIdEQ.equals(userReportVo.getCityId());
			}
			if (districtIdEQ != null) {
				result = result && districtIdEQ.equals(userReportVo.getDistrictId());
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
		Integer pageSize = orderReportVoQueryModel.getPageSize();
		Integer pageNumber = orderReportVoQueryModel.getPageNumber();
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
				data.add(filtered.get(index));
			}
		}
		
		List<String> timeLabels = getTimeLabels();
		List<Order> allOrders = localCacheComponent.getOrders();
		List<OrderReportVo> result =  data.stream().map( v -> {
			Map<String, Long> map = timeLabels.stream().collect(Collectors.toMap(t -> t, t -> 0L ,(u, e)-> { throw new IllegalStateException(String.format("Duplicate key %s", u)); }, LinkedHashMap::new));
			List<Order> os = allOrders.stream().filter(order -> order.getUserId().equals(v.getId()) && (order.getOrderStatus() == OrderStatus.已完成 
					|| order.getOrderStatus() == OrderStatus.已支付 || order.getOrderStatus() == OrderStatus.已发货)).collect(Collectors.toList());
			UserRank userRankEQ = orderReportVoQueryModel.getUserRankEQ();
			if(userRankEQ == null) {
				os = os.stream().filter(order -> order.getBuyerUserRank() == UserRank.V4).collect(Collectors.toList());
			} else {
				os = os.stream().filter(order -> order.getBuyerUserRank() == userRankEQ).collect(Collectors.toList());
			}
			for(Order order : os) {
				Date createdTime = order.getCreatedTime();
				String formatDate = GcUtils.formatDate(createdTime, "yy/M");
				Long quantity  = map.get(formatDate);
				if(quantity != null) {
					quantity += order.getQuantity();
					map.put(formatDate, quantity);
				}
			}
			OrderReportVo orderReportVo = new OrderReportVo();
			orderReportVo.setNickname(v.getNickname());
			orderReportVo.setRootName(v.getRootRootName());
			orderReportVo.setPhone(v.getPhone());
			orderReportVo.setV4UserNickname(v.getV4UserNickname());
			
			List<OrderReportVo.OrderReportVoItem> orderReportVoItems = new ArrayList<>();
		    for(Map.Entry<String, Long> entry : map.entrySet()) {   
		    	OrderReportVo.OrderReportVoItem orderReportVoItem = new OrderReportVo.OrderReportVoItem();
		    	orderReportVoItem.setTimeLabel(entry.getKey());
		    	orderReportVoItem.setQuantity(entry.getValue());
		    	orderReportVoItems.add(orderReportVoItem);
		    }
		    orderReportVo.setOrderReportVoItems(orderReportVoItems);
			return orderReportVo;
		}).collect(Collectors.toList());
		
		/* 合计 */
		{
			OrderReportVo orderReportVo = new OrderReportVo();
			orderReportVo.setNickname("合计");
			Map<String, Long> map = new LinkedHashMap<String, Long>();
			for(OrderReportVo o : result) {
				List<OrderReportVoItem> orderReportVoItems = o.getOrderReportVoItems();
				for (OrderReportVoItem orderReportVoItem : orderReportVoItems) {
					Long quantity = map.get(orderReportVoItem.getTimeLabel());
					quantity = quantity == null ? orderReportVoItem.getQuantity() : quantity + orderReportVoItem.getQuantity();
					map.put(orderReportVoItem.getTimeLabel(), quantity);
				}
			}
			
			List<OrderReportVo.OrderReportVoItem> orderReportVoItems = new ArrayList<>();
		    for(Map.Entry<String, Long> entry : map.entrySet()) {   
		    	OrderReportVo.OrderReportVoItem orderReportVoItem = new OrderReportVo.OrderReportVoItem();
		    	orderReportVoItem.setTimeLabel(entry.getKey());
		    	orderReportVoItem.setQuantity(entry.getValue());
		    	orderReportVoItems.add(orderReportVoItem);
		    }
		    orderReportVo.setOrderReportVoItems(orderReportVoItems);
		    result.add(orderReportVo);
		}
		
		
		Page<OrderReportVo> page = new Page<>();
		page.setPageNumber(pageNumber);
		page.setPageSize(pageSize);
		page.setData(result);
		page.setTotal(Long.valueOf(filtered.size()));
		return new Grid<>(page);
	}
	
	@RequiresPermissions("orderReport:export")
	@RequestMapping("/export")
	public String export(ReportQueryModel reportQueryModel, OrderReportVo.OrderReportVoQueryModel orderReportVoQueryModel, 
			HttpServletResponse response) throws IOException {

		List<UserReportVo> all = localCacheComponent.getuserReportVos();
		List<UserReportVo> filtered = all.stream()
			.filter(v -> v.getUserRank() == UserRank.V4)
			.filter(userReportVo -> {
			boolean result = true;
			Long provinceIdEQ = orderReportVoQueryModel.getProvinceIdEQ();
			Long cityIdEQ = orderReportVoQueryModel.getCityIdEQ();
			Long districtIdEQ = orderReportVoQueryModel.getDistrictIdEQ();
			String nicknameLK = orderReportVoQueryModel.getNicknameLK();
			String phoneEQ = orderReportVoQueryModel.getPhoneEQ();
			String rootRootNameLK = orderReportVoQueryModel.getRootRootNameLK();
			String v4UserNicknameLK = orderReportVoQueryModel.getV4UserNicknameLK();
			
			if (provinceIdEQ != null) {
				result = result && provinceIdEQ.equals(userReportVo.getProvinceId());
			}
			if (cityIdEQ != null) {
				result = result && cityIdEQ.equals(userReportVo.getCityId());
			}
			if (districtIdEQ != null) {
				result = result && districtIdEQ.equals(userReportVo.getDistrictId());
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
		
		List<String> timeLabels = getTimeLabels();
		List<Order> allOrders = localCacheComponent.getOrders();
		List<OrderReportVo> result =  filtered.stream().map( v -> {
			Map<String, Long> map = timeLabels.stream().collect(Collectors.toMap(t -> t, t -> 0L ,(u, e)-> { throw new IllegalStateException(String.format("Duplicate key %s", u)); }, LinkedHashMap::new));
			List<Order> os = allOrders.stream().filter(order -> order.getUserId().equals(v.getId()) && (order.getOrderStatus() == OrderStatus.已完成 
					|| order.getOrderStatus() == OrderStatus.已支付 || order.getOrderStatus() == OrderStatus.已发货)).collect(Collectors.toList());
			UserRank userRankEQ = orderReportVoQueryModel.getUserRankEQ();
			if(userRankEQ == null) {
				os = os.stream().filter(order -> order.getBuyerUserRank() == UserRank.V4).collect(Collectors.toList());
			} else {
				os = os.stream().filter(order -> order.getBuyerUserRank() == userRankEQ).collect(Collectors.toList());
			}
			for(Order order : os) {
				Date createdTime = order.getCreatedTime();
				String formatDate = GcUtils.formatDate(createdTime, "yy/M");
				Long quantity  = map.get(formatDate);
				if(quantity != null) {
					quantity += order.getQuantity();
					map.put(formatDate, quantity);
				}
			}
			OrderReportVo orderReportVo = new OrderReportVo();
			orderReportVo.setNickname(v.getNickname());
			orderReportVo.setRootName(v.getRootRootName());
			orderReportVo.setPhone(v.getPhone());
			orderReportVo.setV4UserNickname(v.getV4UserNickname());
			
			List<OrderReportVo.OrderReportVoItem> orderReportVoItems = new ArrayList<>();
		    for(Map.Entry<String, Long> entry : map.entrySet()) {   
		    	OrderReportVo.OrderReportVoItem orderReportVoItem = new OrderReportVo.OrderReportVoItem();
		    	orderReportVoItem.setTimeLabel(entry.getKey());
		    	orderReportVoItem.setQuantity(entry.getValue());
		    	orderReportVoItems.add(orderReportVoItem);
		    }
		    orderReportVo.setOrderReportVoItems(orderReportVoItems);
			return orderReportVo;
		}).collect(Collectors.toList());
		
		/* 合计 */
		{
			OrderReportVo orderReportVo = new OrderReportVo();
			orderReportVo.setNickname("合计");
			Map<String, Long> map = new LinkedHashMap<String, Long>();
			for(OrderReportVo o : result) {
				List<OrderReportVoItem> orderReportVoItems = o.getOrderReportVoItems();
				for (OrderReportVoItem orderReportVoItem : orderReportVoItems) {
					Long quantity = map.get(orderReportVoItem.getTimeLabel());
					quantity = quantity == null ? orderReportVoItem.getQuantity() : quantity + orderReportVoItem.getQuantity();
					map.put(orderReportVoItem.getTimeLabel(), quantity);
				}
			}
			
			List<OrderReportVo.OrderReportVoItem> orderReportVoItems = new ArrayList<>();
		    for(Map.Entry<String, Long> entry : map.entrySet()) {   
		    	OrderReportVo.OrderReportVoItem orderReportVoItem = new OrderReportVo.OrderReportVoItem();
		    	orderReportVoItem.setTimeLabel(entry.getKey());
		    	orderReportVoItem.setQuantity(entry.getValue());
		    	orderReportVoItems.add(orderReportVoItem);
		    }
		    orderReportVo.setOrderReportVoItems(orderReportVoItems);
		    result.add(orderReportVo);
		}
		
		String fileName = "服务商进货(月)报表.xlsx";
		WebUtils.setFileDownloadHeader(response, fileName);

		OutputStream os = response.getOutputStream();
		List<Map<String, Object>> dataList = result.stream().map(v -> {
			Map<String, Object> columnMap = new LinkedHashMap<>();
			columnMap.put("服务商", v.getNickname());
			columnMap.put("系统", v.getRootName());
			columnMap.put("直属特级", v.getV4UserNickname());
			
			List<OrderReportVo.OrderReportVoItem> orderReportVoItems = v.getOrderReportVoItems();
			for(OrderReportVo.OrderReportVoItem ovt : orderReportVoItems) {
				columnMap.put(ovt.getTimeLabel(), ovt.getQuantity());
			}
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
	
	private List<String> getTimeLabels() {
		LocalDate begin = LocalDate.of(2016, 2, 1);
		LocalDate today = LocalDate.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yy/M");
		List<String> timeLabels = new ArrayList<>();
		for (LocalDate itDate = begin; itDate.isEqual(today) || itDate.isBefore(today); itDate = itDate.plusMonths(1)) {
			timeLabels.add(dateTimeFormatter.format(itDate));
		}
		return timeLabels;
	}
	
}
