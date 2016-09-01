package com.gc.component;

import static org.apache.commons.lang3.time.DateFormatUtils.format;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zy.common.util.BeanUtils;
import com.gc.entity.mal.Order;
import com.gc.entity.mal.OrderItem;
import com.gc.service.OrderItemService;
import com.zy.util.GcUtils;
import com.gc.vo.OrderAdminVo;
import com.gc.vo.OrderItemAdminVo;

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
	
}
