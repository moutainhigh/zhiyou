package com.zy.component;

import static com.zy.util.GcUtils.formatDate;

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
import com.zy.vo.OrderItemVo;
import com.zy.vo.OrderListVo;

@Component
public class OrderComponent {
	
	@Autowired
	private OrderItemService orderItemService;
	
	private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	private static final String SIMPLE_TIME_PATTERN = "M月d日 HH:mm";
	
	public OrderAdminVo buildAdminVo(Order order) {
		OrderAdminVo orderAdminVo = new OrderAdminVo();
		BeanUtils.copyProperties(order, orderAdminVo);
		
		orderAdminVo.setCreatedTimeLabel(formatDate(order.getCreatedTime(), TIME_PATTERN));
		orderAdminVo.setExpiredTimeLabel(formatDate(order.getExpiredTime(), TIME_PATTERN));
		orderAdminVo.setPaidTimeLabel(formatDate(order.getPaidTime(), TIME_PATTERN));
		orderAdminVo.setRefundedTimeLabel(formatDate(order.getRefundedTime(), TIME_PATTERN));
		
		OrderItem orderItem = orderItemService.findByOrderId(order.getId()).get(0);
		if (orderItem != null) {
			orderAdminVo.setImageThumbnail(GcUtils.getThumbnail(orderItem.getImage()));
			orderAdminVo.setPrice(orderItem.getPrice());
			orderAdminVo.setQuantity(orderItem.getQuantity());
		}
		return orderAdminVo;
	}
	
	
	public OrderListVo buildListVo(Order order) {
		OrderListVo orderListVo = new OrderListVo();
		BeanUtils.copyProperties(order, orderListVo);
		
		orderListVo.setCreatedTimeLabel(formatDate(order.getCreatedTime(), SIMPLE_TIME_PATTERN));
		orderListVo.setExpiredTimeLabel(formatDate(order.getExpiredTime(), SIMPLE_TIME_PATTERN));
		orderListVo.setAmount(order.getAmount());
		
		List<OrderItem> orderItems = orderItemService.findByOrderId(order.getId());
		List<OrderItemVo> orderItemVos = orderItems.stream().map(v ->{
			OrderItemVo orderItemVo = new OrderItemVo();
			BeanUtils.copyProperties(v, orderItemVo);
			
			orderItemVo.setImageThumbnail(GcUtils.getThumbnail(v.getImage()));
			orderItemVo.setPrice(v.getPrice());
			orderItemVo.setAmount(v.getAmount());
			return orderItemVo;
		}).collect(Collectors.toList());
		orderListVo.setOrderItems((ArrayList<OrderItemVo>) orderItemVos);
		
		return orderListVo;
	}
	
	public OrderDetailVo buildDetailVo(Order order) {
		OrderDetailVo orderDetailVo = new OrderDetailVo();
		BeanUtils.copyProperties(order, orderDetailVo);
		
		orderDetailVo.setCreatedTimeLabel(formatDate(order.getCreatedTime(), TIME_PATTERN));
		if(order.getExpiredTime() != null){
			orderDetailVo.setExpiredTimeLabel(formatDate(order.getExpiredTime(), TIME_PATTERN));
		}
		if(order.getExpiredTime() != null){
			orderDetailVo.setPaidTimeLabel(formatDate(order.getPaidTime(), TIME_PATTERN));
		}
		if(order.getExpiredTime() != null){
		orderDetailVo.setRefundedTimeLabel(formatDate(order.getRefundedTime(), TIME_PATTERN));
		}
		orderDetailVo.setAmount(order.getAmount());
		orderDetailVo.setRefund(order.getAmount());
		
		List<OrderItem> orderItems = orderItemService.findByOrderId(order.getId());
		List<OrderItemVo> orderItemVos = orderItems.stream().map(v ->{
			OrderItemVo orderItemVo = new OrderItemVo();
			BeanUtils.copyProperties(v, orderItemVo);
			
			orderItemVo.setImageThumbnail(GcUtils.getThumbnail(v.getImage()));
			orderItemVo.setPrice(v.getPrice());
			orderItemVo.setAmount(v.getAmount());
			return orderItemVo;
		}).collect(Collectors.toList());
		orderDetailVo.setOrderItems((ArrayList<OrderItemVo>) orderItemVos);
		
		return orderDetailVo;
	}
}
