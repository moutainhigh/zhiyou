package com.zy.component;

import static org.apache.commons.lang3.time.DateFormatUtils.format;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.mal.Order;
import com.zy.entity.mal.OrderItem;
import com.zy.service.OrderItemService;
import com.zy.util.GcUtils;
import com.zy.vo.OrderAdminVo;
import com.zy.vo.OrderDetailVo;
import com.zy.vo.OrderItemAdminVo;
import com.zy.vo.OrderItemVo;
import com.zy.vo.OrderListVo;

@Component
public class OrderComponent {
	
	@Autowired
	private CacheComponent cacheComponent;

	@Autowired
	private OrderItemService orderItemService;
	
	private static final String TIME_LABEL = "yyyy-MM-dd HH:mm:ss";
	
	public OrderAdminVo buildAdminVo(Order order) {
		OrderAdminVo orderAdminVo = new OrderAdminVo();
		BeanUtils.copyProperties(order, orderAdminVo);
		
		orderAdminVo.setCreatedTimeLabel(format(order.getCreatedTime(), TIME_LABEL));
		orderAdminVo.setExpiredTimeLabel(format(order.getExpiredTime(), TIME_LABEL));
		orderAdminVo.setPaidTimeLabel(format(order.getPaidTime(), TIME_LABEL));
		orderAdminVo.setRefundedTimeLabel(format(order.getRefundedTime(), TIME_LABEL));
		
		List<OrderItem> orderItems = orderItemService.findByOrderId(order.getId());
		List<OrderItemAdminVo> orderItemAdminVos = orderItems.stream().map(v ->{
			OrderItemAdminVo orderItemAdminVo = new OrderItemAdminVo();
			BeanUtils.copyProperties(v, orderItemAdminVo);
			
			orderItemAdminVo.setImageThumbnail(GcUtils.getThumbnail(v.getImage()));
			orderItemAdminVo.setAmount(v.getAmount());
			orderItemAdminVo.setPrice(v.getPrice());
			return orderItemAdminVo;
		}).collect(Collectors.toList());
		orderAdminVo.setOrderItems((ArrayList<OrderItemAdminVo>) orderItemAdminVos);
		return orderAdminVo;
	}
	
	
	public OrderListVo buildListVo(Order order) {
		OrderListVo orderListVo = new OrderListVo();
		BeanUtils.copyProperties(order, orderListVo);
		
		orderListVo.setCreatedTimeLabel(format(order.getCreatedTime(), TIME_LABEL));
		orderListVo.setExpiredTimeLabel(format(order.getExpiredTime(), TIME_LABEL));
		orderListVo.setAmount(order.getAmount().toString());
		
		List<OrderItem> orderItems = orderItemService.findByOrderId(order.getId());
		List<OrderItemVo> orderItemVos = orderItems.stream().map(v ->{
			OrderItemVo orderItemVo = new OrderItemVo();
			BeanUtils.copyProperties(v, orderItemVo);
			
			orderItemVo.setImageThumbnail(GcUtils.getThumbnail(v.getImage()));
			orderItemVo.setPrice(v.getPrice().toString());
			orderItemVo.setAmount(v.getAmount().toString());
			return orderItemVo;
		}).collect(Collectors.toList());
		orderListVo.setOrderItems((ArrayList<OrderItemVo>) orderItemVos);
		
		return orderListVo;
	}
	
	public OrderDetailVo buildDetailVo(Order order) {
		OrderDetailVo orderDetailVo = new OrderDetailVo();
		BeanUtils.copyProperties(order, orderDetailVo);
		
		orderDetailVo.setCreatedTimeLabel(format(order.getCreatedTime(), TIME_LABEL));
		orderDetailVo.setExpiredTimeLabel(format(order.getExpiredTime(), TIME_LABEL));
		orderDetailVo.setPaidTimeLabel(format(order.getPaidTime(), TIME_LABEL));
		orderDetailVo.setRefundedTimeLabel(format(order.getRefundedTime(), TIME_LABEL));
		orderDetailVo.setAmount(order.getAmount().toString());
		orderDetailVo.setRefund(order.getAmount().toString());
		
		List<OrderItem> orderItems = orderItemService.findByOrderId(order.getId());
		List<OrderItemVo> orderItemVos = orderItems.stream().map(v ->{
			OrderItemVo orderItemVo = new OrderItemVo();
			BeanUtils.copyProperties(v, orderItemVo);
			
			orderItemVo.setImageThumbnail(GcUtils.getThumbnail(v.getImage()));
			orderItemVo.setPrice(v.getPrice().toString());
			orderItemVo.setAmount(v.getAmount().toString());
			return orderItemVo;
		}).collect(Collectors.toList());
		orderDetailVo.setOrderItems((ArrayList<OrderItemVo>) orderItemVos);
		
		return orderDetailVo;
	}
}
